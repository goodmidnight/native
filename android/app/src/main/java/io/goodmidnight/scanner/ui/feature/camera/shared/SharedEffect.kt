package io.goodmidnight.scanner.ui.feature.camera.shared

import io.goodmidnight.scanner.ui.core.viewmodel.BaseEffect

sealed interface SharedEffect : BaseEffect {
    data class ShowSnackBar(val message: String) : SharedEffect
}
