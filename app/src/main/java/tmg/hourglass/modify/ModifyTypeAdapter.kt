package tmg.hourglass.modify

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tmg.hourglass.R
import tmg.hourglass.data.CountdownType
import tmg.hourglass.utils.Selected

class ModifyTypeAdapter(
    private val itemSelected: (countdownType: CountdownType) -> Unit
): RecyclerView.Adapter<ModifyTypeViewHolder>() {

    var list: List<Selected<CountdownType>> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ModifyTypeViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.element_modify_type, parent, false), itemSelected)

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ModifyTypeViewHolder, position: Int) = holder.bind(list[position])
}