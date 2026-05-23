package io.goodmidnight.scanner.ui.feature.camera.crop.data

import android.graphics.Bitmap
import androidx.compose.ui.geometry.Offset
import io.goodmidnight.scanner.ui.core.viewmodel.BaseEvent

import io.goodmidnight.scanner.ui.feature.camera.shared.SharedState

sealed interface CropEditEvent : BaseEvent {
    data class OnInitialize(
        val image: Bitmap,
        val detectedFrame: SharedState.DocumentFrameState?
    ) : CropEditEvent

    data class OnPinTouchDown(val index: Int) : CropEditEvent

    data class OnPinDrag(
        val index: Int,
        val newPosition: Offset
    ) : CropEditEvent

    data object OnPinTouchUp : CropEditEvent

    data class OnSelectFilter(val filterIndex: Int) : CropEditEvent

    data class OnCompleteCrop(val points: List<Offset>) : CropEditEvent

    data object OnBack : CropEditEvent
}
