package io.goodmidnight.scanner.ui.feature.camera.crop.data

import android.graphics.Bitmap
import io.goodmidnight.scanner.ui.core.viewmodel.BaseEffect

sealed interface CropEditEffect : BaseEffect {
    data object TriggerLongPressHaptic : CropEditEffect
    data object TriggerSnapHaptic : CropEditEffect
    data object PopBackStack : CropEditEffect
    data class NavigateToResult(val image: Bitmap) : CropEditEffect
    data class ShowSnackBar(val message: String) : CropEditEffect
}
