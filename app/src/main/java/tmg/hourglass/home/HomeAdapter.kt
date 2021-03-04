package tmg.hourglass.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import tmg.hourglass.R
import tmg.hourglass.databinding.ElementCountdownHeaderBinding
import tmg.hourglass.databinding.ElementCountdownItemBinding
import tmg.hourglass.databinding.ElementCountdownPlaceholderBinding
import tmg.hourglass.home.views.HeaderViewHolder
import tmg.hourglass.home.views.ItemViewHolder
import tmg.hourglass.home.views.PlaceholderViewHolder
import tmg.utilities.extensions.toEnum

class HomeAdapter(
    private val actionItem: (id: String, action: HomeItemAction) -> Unit
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var list: List<HomeItemType> = emptyList()
        set(value) {
            val diff = DiffUtil.calculateDiff(HomeDiff(field, value))
            field = value
            diff.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            R.layout.element_countdown_header -> HeaderViewHolder(ElementCountdownHeaderBinding.inflate(layoutInflater, parent, false))
            R.layout.element_countdown_item -> ItemViewHolder(ElementCountdownItemBinding.inflate(layoutInflater, parent, false), actionItem)
            R.layout.element_countdown_placeholder -> PlaceholderViewHolder(ElementCountdownPlaceholderBinding.inflate(layoutInflater, parent, false))
            else -> throw RuntimeException("View type not supported")
        }
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> { }
            is ItemViewHolder -> holder.bind(list[position] as HomeItemType.Item)
            is PlaceholderViewHolder -> holder.bind()
        }
    }

    override fun getItemViewType(position: Int) = list[position].layoutId

    override fun getItemCount(): Int = list.size

    inner class HomeDiff(
        private val oldList: List<HomeItemType>,
        private val newList: List<HomeItemType>
    ): DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition] == newList[newItemPosition] ||
                    areSameItems(oldList[oldItemPosition], newList[newItemPosition])

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition] == newList[newItemPosition]

        private fun areSameItems(oldItem: HomeItemType, newItem: HomeItemType): Boolean {
            return oldItem is HomeItemType.Item && newItem is HomeItemType.Item && oldItem.countdown.id == newItem.countdown.id
        }
    }
}