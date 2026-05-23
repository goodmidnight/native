package io.goodmidnight.scanner.ui.feature.camera.camera.data

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.goodmidnight.scanner.core.camera.CameraController
import io.goodmidnight.scanner.core.camera.CameraEffect as CoreCameraEffect
import io.goodmidnight.scanner.ui.core.exception.AppError
import io.goodmidnight.scanner.ui.core.viewmodel.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val cameraController: CameraController
) : BaseViewModel<CameraState, CameraEvent, CameraEffect, AppError>(
    CameraState()
) {

    init {
        viewModelScope.launch {
            cameraController.cameraState.collect { ctrlState ->
                updateState {
                    copy(
                        zoomRatio = ctrlState.zoomRatio,
                        zoomRatioRange = ctrlState.zoomRatioRange,
                        isTorchEnabled = ctrlState.isTorchEnabled
                    )
                }
            }
        }

        viewModelScope.launch {
            cameraController.cameraSideEffect.collect { sideEffect ->
                if (sideEffect is CoreCameraEffect.SendCapturedImage) {
                    emitEffect(CameraEffect.TriggerShutterFeedback)
                }
            }
        }

        bindEvent { event ->
            when (event) {
                is CameraEvent.OnZoomRatioChanged -> {
                    cameraController.setZoomRatio(event.zoomRatio)
                }
                is CameraEvent.OnToggleTorch -> {
                    cameraController.setTorchEnabled(!state.value.isTorchEnabled)
                }
                is CameraEvent.OnToggleGridLines -> {
                    updateState {
                        copy(showGridLines = !showGridLines)
                    }
                }
                CameraEvent.OnBack -> back()
                CameraEvent.OnNavigateToResult -> navigateToResult()
            }
        }
        bindError { error ->
            error.handleError()
            error.uiMessage?.let { emitEffect(CameraEffect.ShowSnackBar(it)) }
        }
    }

    private fun back() {
        viewModelScope.launch {
            emitEffect(CameraEffect.PopBackStack)
        }
    }

    private fun navigateToResult() {
        viewModelScope.launch {
            emitEffect(CameraEffect.NativeToCrop)
        }
    }
}

