package io.goodmidnight.scanner.data.repository

import io.goodmidnight.scanner.data.datasource.SettingsDataSource
import io.goodmidnight.scanner.domain.repository.SettingsRepository
import io.goodmidnight.scanner.model.DefaultFilter
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
            settings.defaultFilter,
            settings.theme,
            settings.showGridLines
        ) { values ->
            val imageQuality = values[0] as String
            val autoSaveToGallery = values[1] as Boolean
            val defaultFilter = values[2] as String
            val theme = values[3] as String
            val showGridLines = values[4] as Boolean

            SettingsData(
                imageQuality = ImageQuality.valueOf(imageQuality),
                autoSaveToGallery = autoSaveToGallery,
                defaultFilter = DefaultFilter.valueOf(defaultFilter),
                theme = Theme.valueOf(theme),
                showGridLines = showGridLines
            )
        }
    }

    override suspend fun setImageQuality(quality: ImageQuality) =
        settings.setImageQuality(quality.name)

    override suspend fun setAutoSaveToGallery(enabled: Boolean) =
        settings.setAutoSaveToGallery(enabled)

    override suspend fun setDefaultFilter(filter: DefaultFilter) =
        settings.setDefaultFilter(filter.name)

    override suspend fun setTheme(theme: Theme) = settings.setTheme(theme.name)
    override suspend fun setShowGridLines(enabled: Boolean) = settings.setShowGridLines(enabled)
}
