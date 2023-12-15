package tmg.hourglass.presentation.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import tmg.hourglass.presentation.R
import tmg.hourglass.strings.R.string

data class NavigationItem(
    val id: String,
    @StringRes
    val label: Int,
    @DrawableRes
    val icon: Int,
    val isSelected: Boolean? = false,
)

internal val fakeNavigationItems: List<NavigationItem> = listOf(
    NavigationItem(
        id = "menu",
        label = string.ab_menu,
        icon = R.drawable.ic_menu,
        isSelected = true
    ),
    NavigationItem(
        id = "back",
        label = string.ab_back,
        icon = R.drawable.ic_back
    )
)