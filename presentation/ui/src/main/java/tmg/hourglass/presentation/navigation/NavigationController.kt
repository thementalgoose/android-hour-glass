package tmg.hourglass.presentation.navigation

import androidx.navigation.NavHostController

interface NavigationController {
    var navHostController: NavHostController
    fun navigate(destination: NavigationDestination)
}