package io.goodmidnight.scanner.ui.feature.settings.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

fun NavGraphBuilder.settingsScreen(
    navController: NavController,
    route: String,
) {
    composable(route) {
        SettingsRoute(
            onNavigateToOpenSource = { /* TODO */ },
            onNavigateToPrivacyPolicy = { /* TODO */ },
            onBack = { navController.popBackStack() }
        )
    }
}
