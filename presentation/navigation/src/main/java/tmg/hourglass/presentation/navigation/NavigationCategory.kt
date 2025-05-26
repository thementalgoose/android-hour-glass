package tmg.hourglass.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface NavigationCategory {
    data object Home: NavigationCategory
    data object Settings: NavigationCategory
}