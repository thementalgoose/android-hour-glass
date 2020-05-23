package tmg.hourglass.home

import androidx.annotation.DrawableRes
import tmg.hourglass.R
import tmg.hourglass.data.models.Countdown

enum class HomeItemAction(
    @DrawableRes val id: Int
) {
    EDIT(
        R.drawable.ic_edit
    ),
    DELETE(
        R.drawable.ic_delete
    ),
    CHECK(
        R.drawable.ic_tick
    ),
}

sealed class HomeItemType {
    object Header: HomeItemType()
    object Placeholder: HomeItemType()
    data class Item(
        val countdown: Countdown,
        val action: HomeItemAction,
        val isEnabled: Boolean = false,
        val showDescription: Boolean = true,
        val clickBackground: Boolean = false,
        val animateBar: Boolean = true
    ): HomeItemType()
}