package io.goodmidnight.scanner.data.repository

import io.goodmidnight.scanner.data.datasource.SettingsDataSource
import io.goodmidnight.scanner.domain.repository.SettingsRepository
import io.goodmidnight.scanner.model.ImageQuality
import io.goodmidnight.scanner.model.SettingsData
import io.goodmidnight.scanner.model.Theme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class DefaultSettingsRepository @Inject constructor(
    private val settings: SettingsDataSource,
) : SettingsRepository {

    override fun getSettings(): Flow<SettingsData> {
        return combine(
            settings.imageQuality,
            settings.autoSaveToGallery,
            settings.theme,
            settings.showGridLines
        ) { imageQuality, autoSaveToGallery, theme, showGridLines ->
            SettingsData(
                imageQuality = ImageQuality.valueOf(imageQuality),
                autoSaveToGallery = autoSaveToGallery,
                theme = Theme.valueOf(theme),
                showGridLines = showGridLines
            )
        }
    }

    override suspend fun setImageQuality(quality: ImageQuality) =
        settings.setImageQuality(quality.name)

    override suspend fun setAutoSaveToGallery(enabled: Boolean) =
        settings.setAutoSaveToGallery(enabled)

    override suspend fun setTheme(theme: Theme) = settings.setTheme(theme.name)
    override suspend fun setShowGridLines(enabled: Boolean) = settings.setShowGridLines(enabled)
}
