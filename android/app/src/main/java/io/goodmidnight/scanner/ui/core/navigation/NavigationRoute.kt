package io.goodmidnight.scanner.ui.core.navigation

/**
 * [NavigationRoute]
 * - 네비게이션을 정의하는 라우트
 * @property destinationRoute 네비게이션 목적지 경로
 */
interface NavigationRoute {
    val destinationRoute: String

    /**
     * [buildNavigationRoute]
     * - 해당 목적지에 대한 네비게이션 라우트 구성
     * - 별도 파라미터가 존재할 경우 목적지에 쿼리 스트링을 이어 붙임
     * @param route 경로
     * @param args 파라미터 key value 집합
     */
    fun buildNavigationRoute(route: String, vararg args: Pair<String, String>) =
        route + (args.takeIf { it.isNotEmpty() }?.let { "?" + args.joinToString("&") { "${it.first}=${it.second}" } }
            ?: "")
}
