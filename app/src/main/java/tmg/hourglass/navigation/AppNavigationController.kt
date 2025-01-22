package tmg.hourglass.navigation

import androidx.navigation.NavHostController
import tmg.hourglass.core.googleanalytics.CrashReporter
import tmg.hourglass.presentation.navigation.NavigationController
import tmg.hourglass.presentation.navigation.NavigationDestination
import tmg.hourglass.presentation.navigation.navigateTo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppNavigationController @Inject constructor(
    private val crashlyticsManager: CrashReporter
): NavigationController {

    override lateinit var navHostController: NavHostController

    override fun navigate(destination: NavigationDestination) {
        crashlyticsManager.log("Navigating to ${destination.route}")
        navHostController.navigateTo(destination)
    }
}