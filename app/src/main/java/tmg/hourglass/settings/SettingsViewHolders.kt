package tmg.hourglass.settings

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import tmg.hourglass.databinding.ElementSettingsCategoryBinding
import tmg.hourglass.databinding.ElementSettingsPreferenceBinding
import tmg.hourglass.databinding.ElementSettingsPreferenceSwitchBinding
import tmg.utilities.extensions.views.getString

class SettingsHeaderViewHolder(
    private val binding: ElementSettingsCategoryBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(model: SettingsModel.Header) {
        binding.tvHeader.text = getString(model.title)
    }
}

class SettingsPreferenceViewHolder(
    private val binding: ElementSettingsPreferenceBinding,
    private val clickPref: (model: SettingsModel) -> Unit
): RecyclerView.ViewHolder(binding.root), View.OnClickListener {

    private lateinit var model: SettingsModel.Pref

    init {
        binding.llMain.setOnClickListener(this)
    }

    fun bind(model: SettingsModel.Pref) {
        this.model = model
        binding.tvTitle.setText(model.title)
        binding.tvDescription.setText(model.description)

    }

    override fun onClick(p0: View?) {
        clickPref(model)
    }
}

class SettingsPreferenceSwitchViewHolder(
    private val binding: ElementSettingsPreferenceSwitchBinding,
    private val clickPref: (model: SettingsModel) -> Unit
): RecyclerView.ViewHolder(binding.root), View.OnClickListener {

    private lateinit var model: SettingsModel.SwitchPref

    init {
        binding.llPrefSwitchMain.setOnClickListener(this)
    }

    fun bind(model: SettingsModel.SwitchPref) {
        this.model = model
        binding.tvPrefSwitchTitle.setText(model.title)
        binding.tvPrefSwitchDesc.setText(model.description)
        binding.checkbox.isChecked = model.getState()
        binding.checkbox.isEnabled = false
    }

    override fun onClick(p0: View?) {
        clickPref(model)
    }
}