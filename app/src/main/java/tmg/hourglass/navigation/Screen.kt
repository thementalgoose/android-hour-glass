package tmg.hourglass.navigation

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

inline fun <reified T: NavKey> NavBackStack<NavKey>.replaceDetail(with: T) {
    this.removeDetail()
    this.add(with)
}

fun NavBackStack<NavKey>.navigateToSettings() {
    this.clear()
    this.add(Home)
    this.add(Settings)
}

fun NavBackStack<NavKey>.navigateToHome() {
    this.clear()
    this.add(Home)
}

fun NavBackStack<NavKey>.removeDetail() {
    this.removeAll { key -> !key.isList() }
}

fun NavKey.isList() = when (this) {
    is Home -> true
    is Modify -> false
    is Add -> false
    is Tags -> false
    is Settings -> true
    is SettingsBackup -> false
    is SettingsPrivacyPolicy -> false
    else -> true
}

@Serializable
data object Home: NavKey

@Serializable
data class Modify(
    val id: String
): NavKey

@Serializable
data object Tags: NavKey

@Serializable
data object Add: NavKey

@Serializable
data object Settings: NavKey

@Serializable
data object SettingsPrivacyPolicy: NavKey

@Serializable
data object SettingsBackup: NavKey