package tmg.hourglass.modify_old

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tmg.hourglass.databinding.ElementModifyTypeBinding
import tmg.hourglass.utils.Selected

class ModifyTypeAdapter<T>(
    private val itemSelected: (key: String) -> Unit
): RecyclerView.Adapter<ModifyTypeViewHolder>() {

    var list: List<Selected<T>> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModifyTypeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ElementModifyTypeBinding.inflate(inflater, parent, false)
        return ModifyTypeViewHolder(binding, itemSelected)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ModifyTypeViewHolder, position: Int) = holder.bind(list[position])
}