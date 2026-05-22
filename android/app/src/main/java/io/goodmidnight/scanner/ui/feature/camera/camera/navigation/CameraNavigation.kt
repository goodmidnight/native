package io.goodmidnight.scanner.ui.feature.camera.camera.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.goodmidnight.scanner.ui.core.navigation.MainGraph
import io.goodmidnight.scanner.ui.core.navigation.sharedViewModel
import io.goodmidnight.scanner.ui.feature.camera.shared.SharedViewModel

fun NavGraphBuilder.cameraScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    route: String,
) {
    val popBackStack: () -> Unit = { navController.popBackStack() }
    val navigateToResult: () -> Unit = {
        navController.navigate(MainGraph.Scan.Result.buildNavigationRoute())
    }
    val navigateToSettings: () -> Unit = {
        navController.navigate(MainGraph.Settings.buildNavigationRoute())
    }

    composable(route) {
        val sharedViewModel = it.sharedViewModel<SharedViewModel>(navController)
        CameraRoute(modifier, popBackStack, navigateToResult, navigateToSettings, sharedViewModel)
    }
}
