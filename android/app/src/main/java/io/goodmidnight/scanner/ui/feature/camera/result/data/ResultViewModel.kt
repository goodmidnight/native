package io.goodmidnight.scanner.ui.feature.camera.result.data

import android.graphics.Bitmap
import dagger.hilt.android.lifecycle.HiltViewModel
import io.goodmidnight.scanner.ui.core.exception.AppError
import io.goodmidnight.scanner.ui.core.viewmodel.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class ResultViewModel @Inject constructor(
) : BaseViewModel<ResultState, ResultEvent, ResultEffect, AppError>(
    ResultState()
) {

    init {
        bindEvent { event ->
            when (event) {
                ResultEvent.OnBack -> back()
                is ResultEvent.OnShare -> share(event.bitmap)
                is ResultEvent.OnSaveToFolder -> saveToFolder(event.bitmap)
                ResultEvent.OnTextCopy -> showCopySuccessMessage()
            }
        }
        bindError { error ->
            error.handleError()
            error.uiMessage?.let { emitEffect(ResultEffect.ShowSnackBar(it)) }
        }
    }

    private suspend fun showCopySuccessMessage() {
        emitEffect(ResultEffect.ShowSnackBar("복사되었습니다."))
    }

    private suspend fun back() {
        emitEffect(ResultEffect.PopBackStack)
    }

    private suspend fun share(bitmap: Bitmap) {
        emitEffect(ResultEffect.ShareImage(bitmap))
    }

    private suspend fun saveToFolder(bitmap: Bitmap) {
        emitEffect(ResultEffect.SaveToFolder(bitmap))
    }
}
