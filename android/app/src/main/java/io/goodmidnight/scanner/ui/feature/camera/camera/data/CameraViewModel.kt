package io.goodmidnight.scanner.ui.feature.camera.camera.data

import android.util.Log
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.lifecycle.viewModelScope
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.lifecycle.HiltViewModel
import io.goodmidnight.scanner.ui.core.exception.AppError
import io.goodmidnight.scanner.ui.core.viewmodel.BaseViewModel
import kotlinx.coroutines.guava.await
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
) : BaseViewModel<CameraState, CameraEvent, CameraEffect, AppError>(
    CameraState()
) {

    private var camera: Camera? = null
    private var cameraProvider: ProcessCameraProvider? = null
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>

    init {
        bindEvent { event ->
            when (event) {
                is CameraEvent.OnInitCamera -> initCamera(event)
                is CameraEvent.OnTakePicture -> takePicture()
                is CameraEvent.OnShutdownCamera -> shutdownCamera()
                is CameraEvent.OnZoomRatioChanged -> setZoomRatio(event.zoomRatio)
                CameraEvent.OnBack -> back()
                CameraEvent.OnNavigateToResult -> navigateToResult()
            }
        }
        bindError { error ->
            error.handleError()
            error.uiMessage?.let { emitEffect(CameraEffect.ShowSnackBar(it)) }
        }
    }

    private fun initCamera(event: CameraEvent.OnInitCamera) {
        viewModelScope.launch {
            cameraProviderFuture = ProcessCameraProvider.getInstance(event.previewView.context)
            cameraProvider = cameraProviderFuture.await()

            val preview = Preview.Builder().build().also {
                it.surfaceProvider = event.previewView.surfaceProvider
            }

            val imageCapture = ImageCapture.Builder().build()

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider?.unbindAll()
                camera = cameraProvider?.bindToLifecycle(
                    event.lifecycleOwner,
                    cameraSelector,
                    preview,
                    imageCapture
                )

                camera?.cameraInfo?.zoomState?.observe(event.lifecycleOwner) { zoomState ->
                    val minRatio = zoomState.minZoomRatio
                    val maxRatio = zoomState.maxZoomRatio
                    val currentRatio = zoomState.zoomRatio
                    updateState {
                        copy(
                            zoomRatio = currentRatio,
                            zoomRatioRange = minRatio..maxRatio
                        )
                    }
                }
            } catch (e: Exception) {
                Log.w("CameraViewModel", "Use case binding failed", e)
            }
        }
    }

    private fun setZoomRatio(zoomRatio: Float) {
        camera?.cameraControl?.setZoomRatio(zoomRatio)
    }

    private fun takePicture() {
        // ... take picture logic
    }

    private fun shutdownCamera() {
        cameraProvider?.unbindAll()
    }

    private fun back() {
        viewModelScope.launch {
            emitEffect(CameraEffect.PopBackStack)
        }
    }

    private fun navigateToResult() {
        viewModelScope.launch {
            emitEffect(CameraEffect.NativeToResult)
        }
    }

    override fun onCleared() {
        super.onCleared()
        shutdownCamera()
    }
}
