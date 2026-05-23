package io.goodmidnight.scanner.ui.feature.camera.crop.navigation

import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.compose.runtime.getValue
import io.goodmidnight.scanner.ui.core.navigation.MainGraph
import io.goodmidnight.scanner.ui.core.navigation.sharedViewModel
import io.goodmidnight.scanner.ui.feature.camera.crop.data.CropEditViewModel
import io.goodmidnight.scanner.ui.feature.camera.crop.ui.CropEditScreen
import io.goodmidnight.scanner.ui.feature.camera.shared.SharedViewModel

fun NavGraphBuilder.cropEditScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    route: String,
) {
    val popBackStack: () -> Unit = {
        navController.popBackStack()
    }
    val navigateToResult: () -> Unit = {
        navController.navigate(MainGraph.Scan.Result.buildNavigationRoute()) {
            popUpTo(MainGraph.Scan.Camera.destinationRoute) { inclusive = false }
        }
    }


    composable(route) { backStackEntry ->
        val sharedViewModel = backStackEntry.sharedViewModel<SharedViewModel>(navController)

        CropEditRoute(
            modifier = modifier,
            popBackStack = popBackStack,
            navigateToResult = navigateToResult,
            sharedViewModel = sharedViewModel
        )
    }
}
