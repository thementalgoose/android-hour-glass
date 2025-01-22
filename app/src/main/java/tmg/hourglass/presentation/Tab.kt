package tmg.hourglass.presentation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import tmg.hourglass.presentation.navigation.NavigationItem
import tmg.hourglass.strings.R.string

enum class Tab(
    @DrawableRes
    val icon: Int,
    @StringRes
    val label: Int
) {
    DASHBOARD(
        icon = tmg.hourglass.R.drawable.ic_menu_now,
        label = string.menu_dashboard
    ),
    SETTINGS(
        icon = tmg.hourglass.R.drawable.ic_menu_settings,
        label = string.menu_settings
    );
}

fun Tab.toNavigationItem(isSelected: Boolean? = null): NavigationItem {
    return NavigationItem(
        id = this.name,
        label = this.label,
        icon = this.icon,
        isSelected = isSelected
    )
}