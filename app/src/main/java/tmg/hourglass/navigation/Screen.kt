package tmg.hourglass.navigation

import tmg.hourglass.presentation.navigation.NavigationDestination

object Screen {
    val Home = NavigationDestination(
        route = "home",
        launchSingleTop = true
    )
    val Settings = NavigationDestination(
        route = "settings",
        launchSingleTop = true
    )
}