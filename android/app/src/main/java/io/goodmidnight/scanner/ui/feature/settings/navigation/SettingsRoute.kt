package io.goodmidnight.scanner.ui.feature.settings.navigation

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import io.goodmidnight.scanner.ui.core.utils.LocalSnackbarHostState
import io.goodmidnight.scanner.ui.feature.settings.data.SettingsEffect
import io.goodmidnight.scanner.ui.feature.settings.data.SettingsEvent
import io.goodmidnight.scanner.ui.feature.settings.data.SettingsViewModel
import io.goodmidnight.scanner.ui.feature.settings.ui.SettingsScreen

@Composable
fun SettingsRoute(
    onBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = LocalSnackbarHostState.current
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.bindEffect(this) { effect ->
            when (effect) {
                SettingsEffect.NavigateToOpenSourceLicenses -> {
                    OssLicensesMenuActivity.setActivityTitle("Open Source Licenses")
                    context.startActivity(Intent(context, OssLicensesMenuActivity::class.java))
                }
                is SettingsEffect.ShowSnackbar -> snackbarHostState.showSnackbar(effect.message)
            }
        }
    }

    BackHandler(onBack = onBack)

    SettingsScreen(
        state = state,
        onBack = onBack,
        onImageQualityChanged = remember {
            {
                viewModel.onEvent(
                    SettingsEvent.OnImageQualityChanged(
                        it
                    )
                )
            }
        },
        onAutoSaveToGalleryChanged = remember {
            {
                viewModel.onEvent(
                    SettingsEvent.OnAutoSaveToGalleryChanged(
                        it
                    )
                )
            }
        },
        onDefaultFilterChanged = remember {
            {
                viewModel.onEvent(
                    SettingsEvent.OnDefaultFilterChanged(
                        it
                    )
                )
            }
        },
        onThemeChanged = remember { { viewModel.onEvent(SettingsEvent.OnThemeChanged(it)) } },
        onShowGridLinesChanged = remember {
            {
                viewModel.onEvent(
                    SettingsEvent.OnShowGridLinesChanged(
                        it
                    )
                )
            }
        },
        onOpenSourceLicensesClicked = remember { { viewModel.onEvent(SettingsEvent.OnOpenSourceLicensesClicked) } },
    )
}
