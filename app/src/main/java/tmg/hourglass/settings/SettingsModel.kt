package tmg.hourglass.settings

import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import tmg.hourglass.R

sealed class SettingsModel(
    @LayoutRes val layoutId: Int,
    val id: Int
) {

    data class Header(
        @StringRes
        val title: Int
    ): SettingsModel(R.layout.element_settings_category, id = title)

    data class SwitchPref(
        @StringRes
        val title: Int,
        @StringRes
        val description: Int,
        val getState: () -> Boolean,
        val saveState: (value: Boolean) -> Unit
    ): SettingsModel(R.layout.element_settings_preference_switch, id = title) {
        var initialState: Boolean = getState()
    }

    data class Pref(
        @StringRes
        val title: Int,
        @StringRes
        val description: Int,
        val onClick: (() -> Unit)?
    ): SettingsModel(R.layout.element_settings_preference, id = title)
}