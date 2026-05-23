package io.goodmidnight.scanner.ui.feature.settings.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import io.goodmidnight.scanner.designsystem.component.SScaffold
import io.goodmidnight.scanner.designsystem.component.STitleTopBar
import io.goodmidnight.scanner.designsystem.preview.ComponentPreview
import io.goodmidnight.scanner.designsystem.theme.Theme
import io.goodmidnight.scanner.model.DefaultFilter
import io.goodmidnight.scanner.model.ImageQuality
import io.goodmidnight.scanner.model.Theme.DARK
import io.goodmidnight.scanner.ui.core.utils.LocalSnackbarHostState
import io.goodmidnight.scanner.ui.feature.settings.data.SettingsState
import io.goodmidnight.scanner.ui.feature.settings.ui.component.RadioGroupSettingsItem
import io.goodmidnight.scanner.ui.feature.settings.ui.component.SettingsHeader
import io.goodmidnight.scanner.ui.feature.settings.ui.component.SettingsItem
import io.goodmidnight.scanner.ui.feature.settings.ui.component.SwitchSettingsItem

@Composable
fun SettingsScreen(
    state: SettingsState,
    onBack: () -> Unit,
    onImageQualityChanged: (ImageQuality) -> Unit,
    onAutoSaveToGalleryChanged: (Boolean) -> Unit,
    onDefaultFilterChanged: (DefaultFilter) -> Unit,
    onThemeChanged: (io.goodmidnight.scanner.model.Theme) -> Unit,
    onShowGridLinesChanged: (Boolean) -> Unit,
    onOpenSourceLicensesClicked: () -> Unit,
) {
    SScaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        topBar = {
            STitleTopBar(
                title = "Settings",
                onBack = onBack
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Theme.colorScheme.background),
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + WindowInsets.statusBars.asPaddingValues().calculateTopPadding(),
                bottom = padding.calculateBottomPadding() + WindowInsets.navigationBars.asPaddingValues()
                    .calculateBottomPadding()
            )
        ) {
            item { SettingsHeader("Features") }
            item {
                RadioGroupSettingsItem(
                    title = "Image Quality",
                    options = ImageQuality.entries.map { it.name to it },
                    selectedOption = state.imageQuality,
                    onOptionSelected = onImageQualityChanged
                )
            }
            item {
                SwitchSettingsItem(
                    title = "Auto-save to Gallery",
                    checked = state.autoSaveToGallery,
                    onCheckedChange = onAutoSaveToGalleryChanged
                )
            }
            item {
                RadioGroupSettingsItem(
                    title = "Default Filter",
                    options = DefaultFilter.entries.map { it.name to it },
                    selectedOption = state.defaultFilter,
                    onOptionSelected = onDefaultFilterChanged
                )
            }

            item { SettingsHeader("General") }
            item {
                RadioGroupSettingsItem(
                    title = "Theme",
                    options = io.goodmidnight.scanner.model.Theme.entries.map { it.name to it },
                    selectedOption = state.theme,
                    onOptionSelected = onThemeChanged
                )
            }
            item {
                SwitchSettingsItem(
                    title = "Show Grid Lines",
                    checked = state.showGridLines,
                    onCheckedChange = onShowGridLinesChanged
                )
            }

            item { SettingsHeader("About") }
            item {
                SettingsItem(title = "Open Source Licenses", onClick = onOpenSourceLicensesClicked)
            }
            item {
                SettingsItem(title = "App Version", subtitle = state.appVersion, onClick = {})
            }
        }
    }
}

@Composable
@ComponentPreview
private fun SettingsScreenPreview() {
    CompositionLocalProvider(
        LocalSnackbarHostState provides SnackbarHostState(),
    ) {
        Theme {
            SettingsScreen(
                state = SettingsState(
                    imageQuality = ImageQuality.HIGH,
                    autoSaveToGallery = false,
                    defaultFilter = DefaultFilter.BLACK_AND_WHITE,
                    theme = DARK,
                    showGridLines = true,
                    appVersion = "1.0.0"
                ),
                onBack = {},
                onImageQualityChanged = {},
                onAutoSaveToGalleryChanged = {},
                onDefaultFilterChanged = {},
                onThemeChanged = {},
                onShowGridLinesChanged = {},
                onOpenSourceLicensesClicked = {},
            )
        }
    }
}
