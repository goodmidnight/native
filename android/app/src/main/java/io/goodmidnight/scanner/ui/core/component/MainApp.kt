package io.goodmidnight.scanner.ui.core.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import io.goodmidnight.scanner.designsystem.component.SSnackbar
import io.goodmidnight.scanner.designsystem.theme.Theme
import io.goodmidnight.scanner.ui.core.navigation.MainGraph
import io.goodmidnight.scanner.ui.core.utils.LocalSnackbarHostState

@Composable
fun MainApp(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }

    CompositionLocalProvider(
        LocalSnackbarHostState provides snackbarHostState,
    ) {
        Scaffold(
            modifier = modifier,
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            snackbarHost = { SSnackbar(snackbarHostState) },
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Theme.colorScheme.background)
                    .padding(it)
            ) {
                NavHost(
                    modifier = modifier,
                    startDestination = MainGraph.Scan.route,
                    navController = navController
                )
            }
        }
    }
}


