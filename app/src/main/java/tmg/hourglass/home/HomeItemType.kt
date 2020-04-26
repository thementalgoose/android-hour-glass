package tmg.hourglass.home

import androidx.annotation.DrawableRes
import tmg.hourglass.R
import tmg.hourglass.data.models.Countdown

enum class HomeItemAction(
    @DrawableRes val id: Int
) {
    EDIT(R.drawable.ic_edit),
    DELETE(R.drawable.ic_delete)
}

sealed class HomeItemType {
    object Header: HomeItemType()
    data class Item(
        val countdown: Countdown,
        val action: HomeItemAction
    ): HomeItemType()
}