package tmg.passage.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import tmg.passage.R
import tmg.passage.data.models.Passage
import tmg.passage.home.views.HeaderViewHolder
import tmg.passage.home.views.ItemViewHolder
import tmg.utilities.extensions.toEnum

private enum class HomeViewType(
    @LayoutRes val layoutId: Int
) {
    HEADER(R.layout.element_passage_header),
    ITEM(R.layout.element_passage_item)
}

class HomeAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var list: List<HomeItemType> = emptyList()

    override fun getItemCount(): Int = list.size

    override fun getItemViewType(position: Int): Int {
        return when (list[position]) {
            HomeItemType.Header -> HomeViewType.HEADER.ordinal
            is HomeItemType.Item -> HomeViewType.ITEM.ordinal
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder{
        val view = LayoutInflater.from(parent.context).inflate(viewType.toEnum<HomeViewType>()!!.layoutId, parent, false)
        return when (viewType.toEnum<HomeViewType>()!!) {
            HomeViewType.HEADER -> HeaderViewHolder(view)
            HomeViewType.ITEM -> ItemViewHolder(view)
        }
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> {

            }
            is ItemViewHolder -> {
                holder.bind(list[position] as HomeItemType.Item)
            }
        }
    }

}