package io.goodmidnight.scanner.ui.feature.camera.crop.data

import android.graphics.Bitmap
import io.goodmidnight.scanner.ui.core.viewmodel.BaseEffect

sealed interface CropEditEffect : BaseEffect {
    data object TriggerLongPressHaptic : CropEditEffect
    data object TriggerSnapHaptic : CropEditEffect
    data object PopBackStack : CropEditEffect
    data class NavigateToResult(val points: FloatArray, val filterIndex: Int) : CropEditEffect {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            other as NavigateToResult
            if (!points.contentEquals(other.points)) return false
            if (filterIndex != other.filterIndex) return false
            return true
        }

        override fun hashCode(): Int {
            var result = points.contentHashCode()
            result = 31 * result + filterIndex
            return result
        }
    }
    data class ShowSnackBar(val message: String) : CropEditEffect
}
