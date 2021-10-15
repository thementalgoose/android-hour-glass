package tmg.hourglass.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import tmg.hourglass.R
import tmg.hourglass.databinding.ElementSettingsCategoryBinding
import tmg.hourglass.databinding.ElementSettingsPreferenceBinding
import tmg.hourglass.databinding.ElementSettingsPreferenceSwitchBinding

class SettingsAdapter(
    private val clickPref: (model: SettingsModel) -> Unit
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var list: List<SettingsModel> = emptyList()
        set(value) {
            val result = DiffUtil.calculateDiff(DiffCallback(field, value))
            field = value
            result.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            R.layout.element_settings_category -> SettingsHeaderViewHolder(
                ElementSettingsCategoryBinding.inflate(layoutInflater, parent, false)
            )
            R.layout.element_settings_preference -> SettingsPreferenceViewHolder(
                ElementSettingsPreferenceBinding.inflate(layoutInflater, parent, false),
                clickPref
            )
            R.layout.element_settings_preference_switch -> SettingsPreferenceSwitchViewHolder(
                ElementSettingsPreferenceSwitchBinding.inflate(layoutInflater, parent, false),
                clickPref,
            )
            else -> throw RuntimeException("View type is not supported!")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = list[position]) {
            is SettingsModel.Header -> (holder as SettingsHeaderViewHolder).bind(item)
            is SettingsModel.Pref -> (holder as SettingsPreferenceViewHolder).bind(item)
            is SettingsModel.SwitchPref -> (holder as SettingsPreferenceSwitchViewHolder).bind(item)
        }
    }

    override fun getItemCount() = list.size

    override fun getItemViewType(position: Int) = list[position].layoutId

    inner class DiffCallback(
        private val oldList: List<SettingsModel>,
        private val newList: List<SettingsModel>
    ): DiffUtil.Callback() {
        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return areItemsTheSame(oldItemPosition, newItemPosition) &&
                    areContentsTheSameSwitch(oldItemPosition, newItemPosition)
        }

        private fun areContentsTheSameSwitch(old: Int, new: Int): Boolean {
            val oldItem = oldList[old] as? SettingsModel.SwitchPref
            val newItem = newList[new] as? SettingsModel.SwitchPref
            return oldItem == null || newItem == null || oldItem.initialState == newItem.initialState
        }

    }
}