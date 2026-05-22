package io.goodmidnight.scanner.domain.repository

import io.goodmidnight.scanner.model.DefaultFilter
import io.goodmidnight.scanner.model.ImageQuality
import io.goodmidnight.scanner.model.SettingsData
import io.goodmidnight.scanner.model.Theme
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun getSettings(): Flow<SettingsData>
    suspend fun setImageQuality(quality: ImageQuality)
    suspend fun setAutoSaveToGallery(enabled: Boolean)
    suspend fun setDefaultFilter(filter: DefaultFilter)
    suspend fun setTheme(theme: Theme)
    suspend fun setShowGridLines(enabled: Boolean)
}
