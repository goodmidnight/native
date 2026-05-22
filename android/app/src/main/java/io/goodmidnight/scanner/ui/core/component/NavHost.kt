package io.goodmidnight.scanner.ui.core.component

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import io.goodmidnight.scanner.ui.core.navigation.MainGraph
import io.goodmidnight.scanner.ui.core.navigation.graph.scanGraphBuilder
import io.goodmidnight.scanner.ui.feature.settings.navigation.settingsScreen

@Composable
fun NavHost(
    modifier: Modifier = Modifier,
    startDestination: String,
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
        enterTransition = { fadeIn(tween(100)) },
        exitTransition = { fadeOut(tween(100)) },
        popEnterTransition = { fadeIn(tween(100)) },
        popExitTransition = { fadeOut(tween(100)) },
    ) {
        scanGraphBuilder(
            modifier = modifier,
            navController = navController,
            graphName = MainGraph.Scan.route
        )
        settingsScreen(
            navController = navController,
            route = MainGraph.Settings.destinationRoute
        )
    }
}
