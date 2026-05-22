package io.goodmidnight.scanner.core.camera

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.Matrix
import android.graphics.PixelFormat
import android.util.Size
import android.view.OrientationEventListener
import android.view.Surface
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.core.resolutionselector.ResolutionStrategy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Handles camera lifecycle, use case binding (Preview, Analysis, Capture), and image processing.
 */
@Singleton
class CameraController @Inject constructor(
    @ApplicationContext private val context: Context,
    private val cameraProviderFuture: ListenableFuture<ProcessCameraProvider>,
) {
    private val scope =
        CoroutineScope(SupervisorJob() + Dispatchers.Main + CoroutineName("CameraController"))

    private val _cameraState = MutableStateFlow(CameraState())
    val cameraState: StateFlow<CameraState> = _cameraState.asStateFlow()

    private val _cameraEvent = Channel<CameraEvent>(
        capacity = Channel.BUFFERED,
        onBufferOverflow = BufferOverflow.DROP_LATEST
    )

    private val _cameraSideEffect = MutableSharedFlow<CameraEffect>()
    val cameraSideEffect = _cameraSideEffect.asSharedFlow()

    private var cameraProvider: ProcessCameraProvider? = null
    private var camera: Camera? = null
    private var imageCapture: ImageCapture? = null
    private var imageAnalysis: ImageAnalysis? = null

    private val cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()

    private var currentPreviewView: PreviewView? = null
    private var currentLifecycleOwner: LifecycleOwner? = null

    private var orientationEventListener: OrientationEventListener? = null
    private var currentRotation: Int = Surface.ROTATION_0

    init {
        observeEvents()
    }

    fun processEvent(event: CameraEvent) {
        _cameraEvent.trySend(event)
    }

    private fun observeEvents() {
        scope.launch {
            _cameraEvent.receiveAsFlow().collect { event ->
                when (event) {
                    is CameraEvent.StartCamera -> handleStartCamera(
                        event.lifecycleOwner,
                        event.previewView
                    )
                    is CameraEvent.TakePicture -> handleTakePicture(event.processingMode)
                    is CameraEvent.Shutdown -> handleShutdown()
                }
            }
        }
    }

    private fun handleStartCamera(lifecycleOwner: LifecycleOwner, previewView: PreviewView) {
        currentLifecycleOwner = lifecycleOwner
        currentPreviewView = previewView

        startOrientationTracking()

        cameraProviderFuture.addListener({
            try {
                cameraProvider = cameraProviderFuture.get()
                bindCameraUseCases()
                _cameraState.update { it.copy(isCameraReady = true) }
            } catch (exc: Exception) {
                scope.launch { _cameraSideEffect.emit(CameraEffect.SendCameraError(exc)) }
            }
        }, ContextCompat.getMainExecutor(context))
    }

    private fun bindCameraUseCases() {
        val owner = currentLifecycleOwner ?: return
        val viewFinder = currentPreviewView ?: return
        val provider = cameraProvider ?: return

        val resolutionSelector = ResolutionSelector.Builder()
            .setResolutionStrategy(
                ResolutionStrategy(
                    Size(1920, 1080),
                    ResolutionStrategy.FALLBACK_RULE_CLOSEST_HIGHER_THEN_LOWER
                )
            )
            .build()

        val preview = Preview.Builder()
            .setResolutionSelector(resolutionSelector)
            .build().also {
                it.setSurfaceProvider(viewFinder.surfaceProvider)
            }

        val analysis = ImageAnalysis.Builder()
            .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
            .also { analysisUseCase ->
                analysisUseCase.setAnalyzer(cameraExecutor) { imageProxy ->
                    val rotation = imageProxy.imageInfo.rotationDegrees
                    val bitmap = imageProxyToBitmap(imageProxy)
                    scope.launch {
                        _cameraSideEffect.emit(CameraEffect.SendPreviewFrame(bitmap, rotation))
                    }
                    imageProxy.close()
                }
            }
        imageAnalysis = analysis

        val highResSelector = ResolutionSelector.Builder()
            .setResolutionStrategy(ResolutionStrategy.HIGHEST_AVAILABLE_STRATEGY)
            .build()

        imageCapture = ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
            .setResolutionSelector(highResSelector)
            .build()

        try {
            provider.unbindAll()
            val boundCamera = provider.bindToLifecycle(
                owner,
                CameraSelector.DEFAULT_BACK_CAMERA,
                preview,
                analysis,
                imageCapture
            )
            camera = boundCamera

            enableAutoFocus()

            boundCamera.cameraInfo.zoomState.observe(owner) { zoomState ->
                _cameraState.update {
                    it.copy(
                        zoomRatio = zoomState.zoomRatio,
                        zoomRatioRange = zoomState.minZoomRatio..zoomState.maxZoomRatio
                    )
                }
            }
        } catch (exc: Exception) {
            scope.launch { _cameraSideEffect.emit(CameraEffect.SendCameraError(exc)) }
        }
    }

    private fun enableAutoFocus() {
        val cam = camera ?: return
        cam.cameraControl.cancelFocusAndMetering()
    }

    private fun startOrientationTracking() {
        if (orientationEventListener == null) {
            orientationEventListener = object : OrientationEventListener(context) {
                override fun onOrientationChanged(orientation: Int) {
                    if (orientation == ORIENTATION_UNKNOWN) return
                    val newRotation = when (orientation) {
                        in 45 until 135 -> Surface.ROTATION_270
                        in 135 until 225 -> Surface.ROTATION_180
                        in 225 until 315 -> Surface.ROTATION_90
                        else -> Surface.ROTATION_0
                    }
                    if (newRotation != currentRotation) {
                        currentRotation = newRotation
                        updateUseCasesRotation(newRotation)
                    }
                }
            }
        }
        orientationEventListener?.enable()
    }

    private fun stopOrientationTracking() {
        orientationEventListener?.disable()
        orientationEventListener = null
    }

    private fun updateUseCasesRotation(rotation: Int) {
        imageCapture?.targetRotation = rotation
        imageAnalysis?.targetRotation = rotation
    }

    fun setZoomRatio(zoomRatio: Float) {
        camera?.cameraControl?.setZoomRatio(zoomRatio)
    }

    fun setTorchEnabled(enabled: Boolean) {
        camera?.cameraControl?.enableTorch(enabled)
        _cameraState.update { it.copy(isTorchEnabled = enabled) }
    }

    private fun handleTakePicture(processingMode: Int) {
        val capture = imageCapture ?: return
        _cameraState.update { it.copy(isCapturing = true) }

        capture.takePicture(
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    val rotation = image.imageInfo.rotationDegrees
                    val bitmap = imageProxyToBitmap(image)
                    
                    // 회전 정보를 바탕으로 비트맵을 물리적으로 회전시킴
                    val rotatedBitmap = if (rotation != 0) {
                        rotateBitmap(bitmap, rotation.toFloat())
                    } else {
                        bitmap
                    }

                    scope.launch {
                        _cameraSideEffect.emit(
                            CameraEffect.SendCapturedImage(
                                rotatedBitmap,
                                0, // 이미 회전됨
                                processingMode,
                            )
                        )
                    }
                    image.close()
                    _cameraState.update { it.copy(isCapturing = false) }
                }

                override fun onError(exception: ImageCaptureException) {
                    _cameraState.update { it.copy(isCapturing = false) }
                    scope.launch { _cameraSideEffect.emit(CameraEffect.SendCameraError(exception)) }
                }
            }
        )
    }

    private fun handleShutdown() {
        stopOrientationTracking()
        cameraProvider?.unbindAll()
        currentPreviewView = null
        currentLifecycleOwner = null
    }

    private fun imageProxyToBitmap(image: ImageProxy): Bitmap {
        return when (image.format) {
            PixelFormat.RGBA_8888 -> {
                val buffer = image.planes[0].buffer
                buffer.rewind()
                val bitmap = Bitmap.createBitmap(image.width, image.height, Bitmap.Config.ARGB_8888)
                bitmap.copyPixelsFromBuffer(buffer)
                bitmap
            }
            ImageFormat.JPEG -> {
                val buffer = image.planes[0].buffer
                buffer.rewind()
                val bytes = ByteArray(buffer.remaining())
                buffer.get(bytes)
                BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                    ?: throw IllegalArgumentException("Failed to decode JPEG.")
            }
            else -> throw IllegalArgumentException("Unsupported image format: ${image.format}")
        }
    }

    private fun rotateBitmap(bitmap: Bitmap, degrees: Float): Bitmap {
        val matrix = Matrix().apply { postRotate(degrees) }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }
}
