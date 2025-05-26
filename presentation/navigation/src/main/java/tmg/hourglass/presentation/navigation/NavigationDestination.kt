package tmg.hourglass.presentation.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface NavigationDestination: NavKey {
    data object Home: NavigationDestination
    data object AddCountdown: NavigationDestination
    data class ModifyCountdown(val id: String): NavigationDestination
    data object Settings: NavigationDestination
    data object PrivacyPolicy: NavigationDestination

    data object Unknown: NavigationDestination
}

internal val NavigationDestination.loggedScreenName: String
    get() = when (this) {
        is NavigationDestination.Home -> "Home"
        is NavigationDestination.AddCountdown -> "AddCountdown"
        is NavigationDestination.ModifyCountdown -> "ModifyCountdown"
        is NavigationDestination.Settings -> "Settings"
        is NavigationDestination.PrivacyPolicy -> "PrivacyPolicy"
        NavigationDestination.Unknown -> "Unknown"
    }