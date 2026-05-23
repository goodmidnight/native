package io.goodmidnight.scanner.ui.feature.camera.camera.data

import io.goodmidnight.scanner.ui.core.viewmodel.BaseEffect

sealed interface CameraEffect : BaseEffect {
    data class ShowSnackBar(val message: String) : CameraEffect
    data object PopBackStack : CameraEffect

    data object NativeToCrop: CameraEffect
    data object TriggerShutterFeedback : CameraEffect
}
