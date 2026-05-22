package io.goodmidnight.scanner.ui.core.activity

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.goodmidnight.scanner.domain.repository.SettingsRepository
import io.goodmidnight.scanner.ui.core.exception.AppError
import io.goodmidnight.scanner.ui.core.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ActivityViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
) : BaseViewModel<ActivityState, ActivityEvent, ActivityEffect, AppError>(
    ActivityState()
) {
    init {
        bindError { error ->
            error.handleError()
        }
        observeSettings()
    }

    private fun observeSettings() {
        settingsRepository.getSettings()
            .onEach { settings ->
                updateState {
                    copy(
                        theme = settings.theme,
                    )
                }
            }.launchIn(viewModelScope)
    }
}
