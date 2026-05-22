package io.goodmidnight.scanner.model

data class SettingsData(
    val imageQuality: ImageQuality,
    val autoSaveToGallery: Boolean,
    val defaultFilter: DefaultFilter,
    val theme: Theme,
    val showGridLines: Boolean,
)
