package tmg.hourglass.widget

import tmg.hourglass.domain.model.Countdown

sealed class ItemWidgetPickerItem {

    object Placeholder: ItemWidgetPickerItem()

    data class Item(
        val countdown: Countdown,
        val isEnabled: Boolean,
    ): ItemWidgetPickerItem()
}