package tmg.hourglass.presentation.navigation

import androidx.navigation3.runtime.NavBackStack
import tmg.hourglass.core.googleanalytics.CrashReporter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigationController @Inject constructor(
    private val crashReporter: CrashReporter
) {
    val navStack = NavBackStack().apply {
        this.add(NavigationDestination.Home)
    }

    fun navigate(destination: NavigationDestination) {
        crashReporter.log("Navigated to ${destination.loggedScreenName}")

        // TODO: Maybe bundle this into NavigationDestination as an 'isRootListPane'? Hmm
        when (destination) {
            NavigationDestination.Home,
            NavigationDestination.Settings -> navStack.apply {
                this.clear()
                this.add(destination)
            }
            else -> navStack.add(destination)
        }
    }

    fun popBack() {
        crashReporter.log("Navigated back")
        navStack.removeLastOrNull()
    }
}