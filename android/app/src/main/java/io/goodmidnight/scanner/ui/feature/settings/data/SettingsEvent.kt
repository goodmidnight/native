package io.goodmidnight.scanner.ui.feature.settings.data

import io.goodmidnight.scanner.model.DefaultFilter
import io.goodmidnight.scanner.model.ImageQuality
import io.goodmidnight.scanner.model.Theme
import io.goodmidnight.scanner.ui.core.viewmodel.BaseEvent

sealed interface SettingsEvent : BaseEvent {
    data class OnImageQualityChanged(val quality: ImageQuality) : SettingsEvent
    data class OnAutoSaveToGalleryChanged(val enabled: Boolean) : SettingsEvent
    data class OnDefaultFilterChanged(val filter: DefaultFilter) : SettingsEvent
    data class OnThemeChanged(val theme: Theme) : SettingsEvent
    data class OnShowGridLinesChanged(val enabled: Boolean) : SettingsEvent
    data object OnClearCacheClicked : SettingsEvent
    data object OnOpenSourceLicensesClicked : SettingsEvent
    data object OnPrivacyPolicyClicked : SettingsEvent
}
