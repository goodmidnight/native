package io.goodmidnight.scanner.ui.feature.camera.result.data

import android.graphics.Bitmap
import io.goodmidnight.scanner.core.save.DocumentSaveController
import io.goodmidnight.scanner.ui.core.viewmodel.BaseEvent


sealed interface ResultEvent : BaseEvent {
    data object OnBack : ResultEvent
    data class OnShare(val bitmap: Bitmap) : ResultEvent
    data class OnSaveClick(val bitmap: Bitmap) : ResultEvent
    data class OnSelectFormat(val format: DocumentSaveController.SaveFormat) : ResultEvent
    data object OnDismissDialog : ResultEvent
    data object OnTextCopy : ResultEvent
}
