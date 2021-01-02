package tmg.hourglass.home

import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import tmg.hourglass.R
import tmg.hourglass.data.models.Countdown

enum class HomeItemAction(
    @DrawableRes val id: Int
) {
    EDIT(R.drawable.ic_edit),
    DELETE(R.drawable.ic_delete),
    CHECK(R.drawable.ic_tick);
}

sealed class HomeItemType(
    @LayoutRes val layoutId: Int
) {
    object Header: HomeItemType(R.layout.element_countdown_header)
    object Placeholder: HomeItemType(R.layout.element_countdown_placeholder)
    data class Item(
        val countdown: Countdown,
        val action: HomeItemAction,
        val isEnabled: Boolean = false,
        val showDescription: Boolean = true,
        val clickBackground: Boolean = false,
        val animateBar: Boolean = true
    ): HomeItemType(R.layout.element_countdown_item)
}