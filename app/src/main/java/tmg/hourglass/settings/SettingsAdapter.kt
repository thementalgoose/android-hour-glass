package tmg.hourglass.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import tmg.components.prefs.AppPreferencesAdapter
import tmg.components.prefs.AppPreferencesItem
import tmg.hourglass.databinding.ElementSettingsCategoryBinding
import tmg.hourglass.databinding.ElementSettingsPreferenceBinding
import tmg.hourglass.databinding.ElementSettingsPreferenceSwitchBinding

class SettingsAdapter(
    prefClicked: (prefKey: String) -> Unit = { _ -> },
    prefSwitchClicked: (prefKey: String, newState: Boolean) -> Unit = { _, _ -> }
): AppPreferencesAdapter(prefClicked, prefSwitchClicked) {

    override fun categoryLayoutId(viewGroup: ViewGroup) =
        ElementSettingsCategoryBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

    override fun preferenceLayoutId(viewGroup: ViewGroup) =
        ElementSettingsPreferenceBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

    override fun preferenceSwitchLayoutId(viewGroup: ViewGroup) =
        ElementSettingsPreferenceSwitchBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

    override fun bindCategory(binding: ViewBinding, model: AppPreferencesItem.Category) {
        (binding as? ElementSettingsCategoryBinding)?.apply {
            tvHeader.setText(model.title)
        }
    }

    override fun bindPreference(binding: ViewBinding, model: AppPreferencesItem.Preference) {
        (binding as? ElementSettingsPreferenceBinding)?.apply {
            llMain.setOnClickListener {
                prefClicked(model.prefKey)
            }
            tvTitle.setText(model.title)
            tvDescription.setText(model.description)
        }
    }

    override fun bindPreferenceSwitch(binding: ViewBinding, model: AppPreferencesItem.SwitchPreference) {
        (binding as? ElementSettingsPreferenceSwitchBinding)?.apply {
            llPrefSwitchMain.setOnClickListener {
                switchChecked(model.prefKey, !model.isChecked)
            }

            tvPrefSwitchTitle.setText(model.title)
            tvPrefSwitchDesc.setText(model.description)
            checkbox.isChecked = model.isChecked
        }
    }
}