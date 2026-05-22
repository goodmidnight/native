package io.goodmidnight.scanner.ui.feature.camera.camera.data

import io.goodmidnight.scanner.ui.core.viewmodel.BaseState
import io.goodmidnight.scanner.ui.core.viewmodel.ScreenState
import io.goodmidnight.scanner.ui.core.viewmodel.UiState

data class CameraState(
    val isLoading: Boolean = false,
    val zoomRatio: Float = 1.0f,
    val zoomRatioRange: ClosedFloatingPointRange<Float> = 1.0f..1.0f,
    override val uiState: UiState = UiState(),
    override val screenState: ScreenState = ScreenState.INITIAL,
) : BaseState
