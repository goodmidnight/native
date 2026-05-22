package io.goodmidnight.scanner.ui.core.activity

import androidx.compose.runtime.Immutable
import io.goodmidnight.scanner.model.Theme
import io.goodmidnight.scanner.ui.core.viewmodel.BaseState
import io.goodmidnight.scanner.ui.core.viewmodel.ScreenState
import io.goodmidnight.scanner.ui.core.viewmodel.UiState
import io.goodmidnight.scanner.ui.feature.settings.data.SettingsState

@Immutable
data class ActivityState(
    override val uiState: UiState = UiState(),
    override val screenState: ScreenState = ScreenState.INITIAL,
    val theme: Theme = Theme.SYSTEM,
) : BaseState
