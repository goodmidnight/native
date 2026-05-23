package io.goodmidnight.scanner.ui.feature.camera.camera.data

import io.goodmidnight.scanner.ui.core.viewmodel.BaseEvent

sealed interface CameraEvent : BaseEvent {
    data class OnZoomRatioChanged(val zoomRatio: Float) : CameraEvent
    data object OnToggleTorch : CameraEvent
    data object OnToggleGridLines : CameraEvent
    data object OnBack : CameraEvent
    data object OnNavigateToResult : CameraEvent
}
