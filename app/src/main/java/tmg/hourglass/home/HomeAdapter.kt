package tmg.hourglass.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import tmg.hourglass.R
import tmg.hourglass.home.views.HeaderViewHolder
import tmg.hourglass.home.views.ItemViewHolder
import tmg.hourglass.home.views.PlaceholderViewHolder
import tmg.utilities.extensions.toEnum

private enum class HomeViewType(
    @LayoutRes val layoutId: Int
) {
    HEADER(R.layout.element_countdown_header),
    ITEM(R.layout.element_countdown_item),
    PLACEHOLDER(R.layout.element_countdown_placeholder)
}

class HomeAdapter(
    private val actionItem: (id: String, action: HomeItemAction) -> Unit
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var list: List<HomeItemType> = emptyList()
        set(value) {
            val diff = DiffUtil.calculateDiff(HomeDiff(field, value))
            field = value
            diff.dispatchUpdatesTo(this)
        }

    override fun getItemCount(): Int = list.size

    override fun getItemViewType(position: Int): Int {
        return when (list[position]) {
            HomeItemType.Header -> HomeViewType.HEADER.ordinal
            is HomeItemType.Item -> HomeViewType.ITEM.ordinal
            HomeItemType.Placeholder -> HomeViewType.PLACEHOLDER.ordinal
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder{
        val view = LayoutInflater.from(parent.context).inflate(viewType.toEnum<HomeViewType>()!!.layoutId, parent, false)
        return when (viewType.toEnum<HomeViewType>()!!) {
            HomeViewType.HEADER -> HeaderViewHolder(view)
            HomeViewType.ITEM -> ItemViewHolder(view, actionItem)
            HomeViewType.PLACEHOLDER -> PlaceholderViewHolder(view)
        }
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> {

            }
            is ItemViewHolder -> {
                holder.bind(list[position] as HomeItemType.Item)
            }
            is PlaceholderViewHolder -> {
                holder.bind()
            }
        }
    }

    inner class HomeDiff(
        private val oldList: List<HomeItemType>,
        private val newList: List<HomeItemType>
    ): DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = oldList[oldItemPosition] == newList[newItemPosition]

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = areItemsTheSame(oldItemPosition, newItemPosition)

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size
    }

}