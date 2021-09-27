package tmg.hourglass.settings.release

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import tmg.hourglass.ReleaseNotes
import tmg.hourglass.databinding.ElementReleaseNotesBinding

class ReleaseAdapter: RecyclerView.Adapter<ReleaseViewHolder>() {

    var list: List<ReleaseNotes> = emptyList()
        set(value) {
            val diff = DiffUtil.calculateDiff(ReleaseDiff(field, value))
            field = value
            diff.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReleaseViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        return ReleaseViewHolder(ElementReleaseNotesBinding.inflate(layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: ReleaseViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    inner class ReleaseDiff(
        private val oldList: List<ReleaseNotes>,
        private val newList: List<ReleaseNotes>
    ): DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = oldList[oldItemPosition] == newList[newItemPosition]

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = oldList[oldItemPosition] == newList[newItemPosition]
    }
}