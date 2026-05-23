package io.goodmidnight.scanner.data.datasource

import kotlinx.coroutines.flow.Flow

interface SettingsDataSource {
    val imageQuality: Flow<String>
    suspend fun setImageQuality(quality: String)

    val autoSaveToGallery: Flow<Boolean>
    suspend fun setAutoSaveToGallery(enabled: Boolean)

    val theme: Flow<String>
    suspend fun setTheme(theme: String)

    val showGridLines: Flow<Boolean>
    suspend fun setShowGridLines(enabled: Boolean)
}