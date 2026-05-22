package io.goodmidnight.scanner.ui.feature.camera.result.data

import androidx.compose.runtime.Immutable
import android.graphics.Bitmap
import io.goodmidnight.scanner.ui.core.viewmodel.BaseState
import io.goodmidnight.scanner.ui.core.viewmodel.ScreenState
import io.goodmidnight.scanner.ui.core.viewmodel.UiState

@Immutable
data class ResultState(
    override val uiState: UiState = UiState(),
    override val screenState: ScreenState = ScreenState.INITIAL,
    val showFormatDialog: Boolean = false,
    val pendingSaveBitmap: Bitmap? = null,
) : BaseState {


}
