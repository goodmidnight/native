package io.goodmidnight.scanner.ui.core.navigation

sealed class MainGraph(
    val route: String,
) {
    data object Scan : MainGraph("scan") {
        data object Camera : MainGraph("scan/camera"), NavigationRoute {
            override val destinationRoute: String = route
            fun buildNavigationRoute(): String = super.buildNavigationRoute(route)
        }

        data object Crop : MainGraph("scan/crop"), NavigationRoute {
            override val destinationRoute: String = route
            fun buildNavigationRoute(): String = super.buildNavigationRoute(route)
        }

        data object Result : MainGraph("scan/result"), NavigationRoute {
            override val destinationRoute = route
            fun buildNavigationRoute(): String = super.buildNavigationRoute(route)
        }
    }

    data object Settings : MainGraph("settings"), NavigationRoute {
        override val destinationRoute: String = route
        fun buildNavigationRoute(): String = super.buildNavigationRoute(route)
    }
}
