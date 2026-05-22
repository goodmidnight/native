package io.goodmidnight.scanner.ui.feature.camera.camera.data

import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import io.goodmidnight.scanner.ui.core.viewmodel.BaseEvent

sealed interface CameraEvent : BaseEvent {
    data class OnInitCamera(
        val lifecycleOwner: LifecycleOwner,
        val previewView: PreviewView
    ) : CameraEvent
    data object OnTakePicture : CameraEvent
    data object OnShutdownCamera : CameraEvent
    data class OnZoomRatioChanged(val zoomRatio: Float) : CameraEvent
    data object OnToggleTorch : CameraEvent
    data object OnToggleGridLines : CameraEvent
    data object OnBack : CameraEvent
    data object OnNavigateToResult : CameraEvent
}
