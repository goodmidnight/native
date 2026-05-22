package io.goodmidnight.scanner.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import io.goodmidnight.scanner.data.datasource.SettingsDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingsDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : SettingsDataSource {

    private object PreferencesKeys {
        val IMAGE_QUALITY = stringPreferencesKey("image_quality")
        val AUTO_SAVE_TO_GALLERY = booleanPreferencesKey("auto_save_to_gallery")
        val DEFAULT_FILTER = stringPreferencesKey("default_filter")
        val THEME = stringPreferencesKey("theme")
        val SHOW_GRID_LINES = booleanPreferencesKey("show_grid_lines")
    }

    override val imageQuality: Flow<String> = dataStore.data.map { it[PreferencesKeys.IMAGE_QUALITY] ?: "MEDIUM" }
    override suspend fun setImageQuality(quality: String) {
        dataStore.edit { it[PreferencesKeys.IMAGE_QUALITY] = quality }
    }

    override val autoSaveToGallery: Flow<Boolean> = dataStore.data.map { it[PreferencesKeys.AUTO_SAVE_TO_GALLERY] ?: false }
    override suspend fun setAutoSaveToGallery(enabled: Boolean) {
        dataStore.edit { it[PreferencesKeys.AUTO_SAVE_TO_GALLERY] = enabled }
    }

    override val defaultFilter: Flow<String> = dataStore.data.map { it[PreferencesKeys.DEFAULT_FILTER] ?: "ORIGINAL" }
    override suspend fun setDefaultFilter(filter: String) {
        dataStore.edit { it[PreferencesKeys.DEFAULT_FILTER] = filter }
    }

    override val theme: Flow<String> = dataStore.data.map { it[PreferencesKeys.THEME] ?: "SYSTEM" }
    override suspend fun setTheme(theme: String) {
        dataStore.edit { it[PreferencesKeys.THEME] = theme }
    }

    override val showGridLines: Flow<Boolean> = dataStore.data.map { it[PreferencesKeys.SHOW_GRID_LINES] ?: false }
    override suspend fun setShowGridLines(enabled: Boolean) {
        dataStore.edit { it[PreferencesKeys.SHOW_GRID_LINES] = enabled }
    }
}