package io.goodmidnight.scanner.ui.feature.settings.data

import io.goodmidnight.scanner.model.DefaultFilter
import io.goodmidnight.scanner.model.ImageQuality
import io.goodmidnight.scanner.model.Theme
import io.goodmidnight.scanner.ui.core.viewmodel.BaseState
import io.goodmidnight.scanner.ui.core.viewmodel.ScreenState
import io.goodmidnight.scanner.ui.core.viewmodel.UiState

data class SettingsState(
    val imageQuality: ImageQuality = ImageQuality.MEDIUM,
    val autoSaveToGallery: Boolean = false,
    val defaultFilter: DefaultFilter = DefaultFilter.ORIGINAL,
    val theme: Theme = Theme.SYSTEM,
    val showGridLines: Boolean = false,
    val appVersion: String = "",
    override val uiState: UiState = UiState(),
    override val screenState: ScreenState = ScreenState.SUCCESS,
) : BaseState
