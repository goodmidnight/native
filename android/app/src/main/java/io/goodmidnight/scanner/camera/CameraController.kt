package io.goodmidnight.scanner.camera

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.PixelFormat
import android.util.Size
import androidx.camera.core.CameraSelector
import androidx.camera.core.FocusMeteringAction
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.core.SurfaceOrientedMeteringPointFactory
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
import java.nio.ByteBuffer
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject

/**
 * Handles camera lifecycle, use case binding (Preview, Analysis, Capture), and image processing.
 */
class CameraController @Inject constructor(
    @ApplicationContext private val context: Context,
    private val cameraProviderFuture: ListenableFuture<ProcessCameraProvider>,
) {
    private val scope =
        CoroutineScope(SupervisorJob() + Dispatchers.Main + CoroutineName("CameraController"))

    // State flow representing the current camera status
    private val _cameraState = MutableStateFlow(CameraState())
    val cameraState: StateFlow<CameraState> = _cameraState.asStateFlow()

    // Channel for incoming camera events to ensure sequential processing
    private val _cameraEvent = Channel<CameraEvent>(
        capacity = Channel.BUFFERED,
        onBufferOverflow = BufferOverflow.DROP_LATEST
    )

    // Shared flow for emitting one-time side effects like errors or frames
    private val _cameraSideEffect = MutableSharedFlow<CameraEffect>()
    val cameraSideEffect = _cameraSideEffect.asSharedFlow()

    private var cameraProvider: ProcessCameraProvider? = null
    private var imageCapture: ImageCapture? = null

    // Dedicated executor for image analysis to avoid blocking the main thread
    private val cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()

    private var currentPreviewView: PreviewView? = null
    private var currentLifecycleOwner: LifecycleOwner? = null

    init {
        observeEvents()
    }

    /**
     * [processEvent]
     * - Entry point for processing camera-related actions.
     */
    fun processEvent(event: CameraEvent) {
        _cameraEvent.trySend(event)
    }

    /**
     * [observeEvents]
     * - Collects and handles events from the internal event channel.
     */
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

    /**
     * [handleStartCamera]
     * - Initializes the CameraProvider and triggers use case binding.
     */
    private fun handleStartCamera(lifecycleOwner: LifecycleOwner, previewView: PreviewView) {
        currentLifecycleOwner = lifecycleOwner
        currentPreviewView = previewView

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

    /**
     * [bindCameraUseCases]
     * - Binds Preview, ImageAnalysis, and ImageCapture use cases to the lifecycle.
     */
    private fun bindCameraUseCases() {
        val owner = currentLifecycleOwner ?: return
        val viewFinder = currentPreviewView ?: return
        val provider = cameraProvider ?: return

        val preview = Preview.Builder().build().also {
            it.setSurfaceProvider(viewFinder.surfaceProvider)
        }

        val imageAnalysis = ImageAnalysis.Builder()
            .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
            .also { analysis ->
                analysis.setAnalyzer(cameraExecutor) { imageProxy ->
                    val rotation = imageProxy.imageInfo.rotationDegrees
                    val bitmap = imageProxyToBitmap(imageProxy)

                    scope.launch {
                        _cameraSideEffect.emit(CameraEffect.SendPreviewFrame(bitmap, rotation))
                    }
                    imageProxy.close()
                }
            }

        imageCapture = ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
            .build()

        try {
            provider.unbindAll()
            provider.bindToLifecycle(
                owner,
                CameraSelector.DEFAULT_BACK_CAMERA,
                preview,
                imageAnalysis,
                imageCapture
            )
        } catch (exc: Exception) {
            scope.launch { _cameraSideEffect.emit(CameraEffect.SendCameraError(exc)) }
        }
    }

    /**
     * [handleTakePicture]
     * - Captures a high-resolution image and processes it.
     */
    private fun handleTakePicture(processingMode: Int) {
        val capture = imageCapture ?: return
        currentPreviewView ?: return

        _cameraState.update { it.copy(isCapturing = true) }

        capture.takePicture(
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    val rotation = image.imageInfo.rotationDegrees
                    val bitmap = imageProxyToBitmap(image)

                    scope.launch {
                        _cameraSideEffect.emit(
                            CameraEffect.SendCapturedImage(
                                bitmap,
                                rotation,
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

    /**
     * [handleShutdown]
     * Unbinds camera use cases and clears references.
     */
    private fun handleShutdown() {
        cameraProvider?.unbindAll()
        currentPreviewView = null
        currentLifecycleOwner = null
    }

    /**
     * [imageProxyToBitmap]
     * - Converts an ImageProxy to a Bitmap based on its pixel format.
     *  1. Processing real-time raw pixels (RGBA_8888) from ImageAnalysis
     *  2. Processing high-resolution compressed photo (JPEG) from ImageCapture
     */
    private fun imageProxyToBitmap(image: ImageProxy): Bitmap {
        return when (image.format) {
            //
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
            else -> {
                throw IllegalArgumentException("Unsupported image format: ${image.format}")
            }
        }
    }
}
