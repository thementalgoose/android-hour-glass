package tmg.hourglass.settings

import android.view.View
import tmg.components.prefs.AppPreferencesAdapter
import tmg.components.prefs.AppPreferencesItem
import tmg.hourglass.R

class SettingsAdapter(
    prefClicked: (prefKey: String) -> Unit = { _ -> },
    prefSwitchClicked: (prefKey: String, newState: Boolean) -> Unit = { _, _ -> }
): AppPreferencesAdapter(prefClicked, prefSwitchClicked) {

    override val categoryLayoutId: Int = R.layout.element_settings_category
    override val preferenceLayoutId: Int = R.layout.element_settings_preference
    override val preferenceSwitchLayoutId: Int = R.layout.element_settings_preference_switch

    override fun bindCategory(view: View, model: AppPreferencesItem.Category) {
        view.apply {
            tvHeader.setText(model.title)
        }
    }

    override fun bindPreference(view: View, model: AppPreferencesItem.Preference) {
        view.apply {
            llMain.setOnClickListener {
                prefClicked(model.prefKey)
            }
            tvTitle.setText(model.title)
            tvDescription.setText(model.description)
        }
    }

    override fun bindPreferenceSwitch(view: View, model: AppPreferencesItem.SwitchPreference) {
        view.apply {
            llPrefSwitchMain.setOnClickListener {
                switchChecked(model.prefKey, !model.isChecked)
            }

            tvPrefSwitchTitle.setText(model.title)
            tvPrefSwitchDesc.setText(model.description)
            checkbox.isChecked = model.isChecked
        }
    }
}