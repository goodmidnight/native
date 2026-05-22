package io.goodmidnight.scanner.ui.feature.settings.data

import io.goodmidnight.scanner.ui.core.viewmodel.BaseEffect

sealed interface SettingsEffect : BaseEffect {
    data object NavigateToOpenSourceLicenses : SettingsEffect
    data object NavigateToPrivacyPolicy : SettingsEffect
    data class ShowSnackbar(val message: String) : SettingsEffect
}
