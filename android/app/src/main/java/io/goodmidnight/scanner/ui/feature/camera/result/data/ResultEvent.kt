package io.goodmidnight.scanner.ui.feature.camera.result.data

import android.graphics.Bitmap
import io.goodmidnight.scanner.ui.core.viewmodel.BaseEvent

sealed interface ResultEvent : BaseEvent {
    data object OnBack : ResultEvent
    data class OnShare(val bitmap: Bitmap) : ResultEvent
    data class OnSaveToFolder(val bitmap: Bitmap) : ResultEvent
    data object OnTextCopy : ResultEvent
}
