package io.goodmidnight.scanner.ui.core.navigation.graph

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.navigation
import androidx.navigation.navigation
import io.goodmidnight.scanner.ui.core.navigation.MainGraph
import io.goodmidnight.scanner.ui.feature.camera.camera.navigation.cameraScreen
import io.goodmidnight.scanner.ui.feature.camera.crop.navigation.cropEditScreen
import io.goodmidnight.scanner.ui.feature.camera.result.navigation.resultScreen

fun NavGraphBuilder.scanGraphBuilder(
    modifier: Modifier = Modifier,
    navController: NavController,
    graphName: String,
) {
    navigation(
        route = graphName,
        startDestination = MainGraph.Scan.Camera.destinationRoute,
        enterTransition = {
            slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(400)) + fadeIn(
                animationSpec = tween(400)
            )
        },
        exitTransition = {
            slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(400)) + fadeOut(
                animationSpec = tween(400)
            )
        },
        popEnterTransition = {
            slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(400)) + fadeIn(
                animationSpec = tween(400)
            )
        },
        popExitTransition = {
            slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(400)) + fadeOut(
                animationSpec = tween(400)
            )
        }
    ) {
        cameraScreen(
            modifier = modifier,
            navController = navController,
            route = MainGraph.Scan.Camera.destinationRoute
        )
        cropEditScreen(
            modifier = modifier,
            navController = navController,
            route = MainGraph.Scan.Crop.destinationRoute
        )
        resultScreen(
            modifier = modifier,
            navController = navController,
            route = MainGraph.Scan.Result.destinationRoute
        )
    }
}