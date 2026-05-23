package io.goodmidnight.scanner.domain.usecase.settings

import io.goodmidnight.scanner.domain.repository.SettingsRepository
import io.goodmidnight.scanner.model.ImageQuality
import io.goodmidnight.scanner.model.Theme
import javax.inject.Inject

class UpdateSettingsUseCase @Inject constructor(
    private val repository: SettingsRepository,
) {
    sealed interface SettingType {
        data class UpdateImageQuality(val quality: ImageQuality) : SettingType
        data class AutoSaveToGallery(val enabled: Boolean) : SettingType
        data class UpdateTheme(val theme: Theme) : SettingType
        data class ShowGridLines(val enabled: Boolean) : SettingType
    }

    suspend operator fun invoke(type: SettingType) {
        when (type) {
            is SettingType.UpdateImageQuality -> repository.setImageQuality(type.quality)
            is SettingType.AutoSaveToGallery -> repository.setAutoSaveToGallery(type.enabled)
            is SettingType.UpdateTheme -> repository.setTheme(type.theme)
            is SettingType.ShowGridLines -> repository.setShowGridLines(type.enabled)
        }
    }
}
