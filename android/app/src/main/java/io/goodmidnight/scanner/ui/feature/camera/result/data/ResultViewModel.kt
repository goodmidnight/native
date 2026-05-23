package io.goodmidnight.scanner.ui.feature.camera.result.data

import android.graphics.Bitmap
import dagger.hilt.android.lifecycle.HiltViewModel
import io.goodmidnight.scanner.ui.core.exception.AppError
import io.goodmidnight.scanner.ui.core.viewmodel.BaseViewModel
import javax.inject.Inject

import io.goodmidnight.scanner.core.save.DocumentSaveController

@HiltViewModel
class ResultViewModel @Inject constructor(
    val documentSaveController: DocumentSaveController
) : BaseViewModel<ResultState, ResultEvent, ResultEffect, AppError>(
    ResultState()
) {

    init {
        bindEvent { event ->
            when (event) {
                ResultEvent.OnBack -> back()
                is ResultEvent.OnShare -> share(event.bitmap)
                is ResultEvent.OnSaveClick -> showSaveDialog(event.bitmap)
                is ResultEvent.OnSelectFormat -> handleSelectFormat(event.format)
                ResultEvent.OnDismissDialog -> dismissDialog()
                ResultEvent.OnTextCopy -> showCopySuccessMessage()
            }
        }
        bindError { error ->
            error.handleError()
            error.uiMessage?.let { emitEffect(ResultEffect.ShowSnackBar(it)) }
        }
    }

    private suspend fun showCopySuccessMessage() {
        emitEffect(ResultEffect.ShowSnackBar("Copied to clipboard."))
    }

    private suspend fun back() {
        emitEffect(ResultEffect.PopBackStack)
    }

    private suspend fun share(bitmap: Bitmap) {
        emitEffect(ResultEffect.ShareImage(bitmap))
    }

    private fun showSaveDialog(bitmap: Bitmap) {
        updateState {
            copy(
                showFormatDialog = true,
                pendingSaveBitmap = bitmap
            )
        }
    }

    private fun dismissDialog() {
        updateState {
            copy(
                showFormatDialog = false,
                pendingSaveBitmap = null
            )
        }
    }

    private suspend fun handleSelectFormat(format: DocumentSaveController.SaveFormat) {
        val bitmap = state.value.pendingSaveBitmap
        if (bitmap != null) {
            updateState {
                copy(
                    showFormatDialog = false,
                    pendingSaveBitmap = null
                )
            }
            emitEffect(ResultEffect.SaveToFolder(bitmap, format))
        }
    }
}
