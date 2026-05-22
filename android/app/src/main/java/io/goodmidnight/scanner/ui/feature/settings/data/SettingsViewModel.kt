package io.goodmidnight.scanner.ui.feature.settings.data

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.goodmidnight.scanner.domain.usecase.ClearCacheUseCase
import io.goodmidnight.scanner.domain.usecase.settings.GetSettingsUseCase
import io.goodmidnight.scanner.domain.usecase.settings.UpdateSettingsUseCase
import io.goodmidnight.scanner.domain.usecase.settings.UpdateSettingsUseCase.SettingType
import io.goodmidnight.scanner.model.DefaultFilter
import io.goodmidnight.scanner.model.ImageQuality
import io.goodmidnight.scanner.ui.core.exception.AppError
import io.goodmidnight.scanner.ui.core.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    getSettingsUseCase: GetSettingsUseCase,
    private val updateSettingsUseCase: UpdateSettingsUseCase,
    private val clearCacheUseCase: ClearCacheUseCase,
) : BaseViewModel<SettingsState, SettingsEvent, SettingsEffect, AppError>(SettingsState()) {

    init {
        getSettingsUseCase().onEach { settings ->
            updateState {
                copy(
                    imageQuality = settings.imageQuality,
                    autoSaveToGallery = settings.autoSaveToGallery,
                    defaultFilter = settings.defaultFilter,
                    theme = settings.theme,
                    showGridLines = settings.showGridLines,
                )
            }
        }.launchIn(viewModelScope)

        bindEvent { event ->
            onEvent(event)
        }
    }

    private fun onEvent(event: SettingsEvent) {
        viewModelScope.launch {
            when (event) {
                is SettingsEvent.OnImageQualityChanged -> updateSettingsUseCase(
                    SettingType.UpdateImageQuality(
                        event.quality
                    )
                )

                is SettingsEvent.OnAutoSaveToGalleryChanged -> updateSettingsUseCase(
                    SettingType.AutoSaveToGallery(
                        event.enabled
                    )
                )

                is SettingsEvent.OnDefaultFilterChanged -> updateSettingsUseCase(
                    SettingType.UpdateDefaultFilter(
                        event.filter
                    )
                )

                is SettingsEvent.OnThemeChanged -> updateSettingsUseCase(SettingType.UpdateTheme(event.theme))
                is SettingsEvent.OnShowGridLinesChanged -> updateSettingsUseCase(
                    SettingType.ShowGridLines(
                        event.enabled
                    )
                )

                SettingsEvent.OnClearCacheClicked -> {
                    val success = clearCacheUseCase()
                    val message = if (success) "Cache cleared successfully" else "Failed to clear cache"
                    emitEffect(SettingsEffect.ShowSnackbar(message))
                }

                SettingsEvent.OnOpenSourceLicensesClicked -> emitEffect(SettingsEffect.NavigateToOpenSourceLicenses)
                SettingsEvent.OnPrivacyPolicyClicked -> emitEffect(SettingsEffect.NavigateToPrivacyPolicy)
            }
        }
    }
}
