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
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return when (viewType) {
            R.layout.element_countdown_header -> HeaderViewHolder(view)
            R.layout.element_countdown_item -> ItemViewHolder(view, actionItem)
            R.layout.element_countdown_placeholder -> PlaceholderViewHolder(view)
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
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = oldList[oldItemPosition] == newList[newItemPosition]

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = areItemsTheSame(oldItemPosition, newItemPosition)

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size
    }
}