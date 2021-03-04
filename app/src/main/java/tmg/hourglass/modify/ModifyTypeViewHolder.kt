package tmg.hourglass.modify

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import tmg.hourglass.R
import tmg.hourglass.data.CountdownInterpolator
import tmg.hourglass.data.CountdownType
import tmg.hourglass.databinding.ElementModifyTypeBinding
import tmg.hourglass.extensions.label
import tmg.hourglass.utils.Selected

class ModifyTypeViewHolder(
    private val binding: ElementModifyTypeBinding,
    private val itemSelected: (key: String) -> Unit
) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

    private lateinit var current: String

    init {
        binding.clMain.setOnClickListener(this)
    }

    fun bind(selected: Selected<*>) {
        when (selected.data) {
            is CountdownType -> {
                this.current = selected.data.key
                binding.apply {
                    tvTitle.setText(selected.data.label())
                    imgSelected.setBackgroundResource(if (selected.isSelected) R.drawable.background_selected else 0)
                    imgSelected.setImageResource(if (selected.isSelected) R.drawable.ic_settings_check else 0)
                }
            }
            is CountdownInterpolator -> {
                this.current = selected.data.key
                binding.apply {
                    tvTitle.setText(selected.data.label())
                    imgSelected.setBackgroundResource(if (selected.isSelected) R.drawable.background_selected else 0)
                    imgSelected.setImageResource(if (selected.isSelected) R.drawable.ic_settings_check else 0)
                }
            }
        }
    }

    override fun onClick(p0: View?) {
        itemSelected(current)
    }
}