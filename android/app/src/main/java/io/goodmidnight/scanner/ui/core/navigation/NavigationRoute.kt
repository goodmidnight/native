package io.goodmidnight.scanner.ui.core.navigation

/**
 * [NavigationRoute]
 * - Defines a navigation route.
 * @property destinationRoute The destination path for navigation.
 */
interface NavigationRoute {
    val destinationRoute: String

    /**
     * [buildNavigationRoute]
     * - Constructs a navigation route for the given destination.
     * - Appends query string parameters to the destination if any are provided.
     * @param route The destination path.
     * @param args A collection of key-value parameter pairs.
     */
    fun buildNavigationRoute(route: String, vararg args: Pair<String, String>) =
        route + (args.takeIf { it.isNotEmpty() }?.let { "?" + args.joinToString("&") { "${it.first}=${it.second}" } }
            ?: "")
}
