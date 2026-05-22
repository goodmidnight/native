package io.goodmidnight.scanner.ui.feature.camera.result.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.goodmidnight.scanner.ui.core.navigation.sharedViewModel
import io.goodmidnight.scanner.ui.feature.camera.shared.SharedViewModel

fun NavGraphBuilder.resultScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    route: String,
) {
    val popBackStack: () -> Unit = { navController.popBackStack() }

    composable(route) {
        val sharedViewModel = it.sharedViewModel<SharedViewModel>(navController)
        ResultRoute(modifier, popBackStack, sharedViewModel)
    }
}
