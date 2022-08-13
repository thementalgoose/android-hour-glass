package tmg.hourglass.settings

import androidx.annotation.StringRes

sealed class SettingsModel(
    val id: Int
) {

    data class Header(
        @StringRes
        val title: Int
    ): SettingsModel(id = title)

    data class SwitchPref(
        @StringRes
        val title: Int,
        @StringRes
        val description: Int,
        val getState: () -> Boolean,
        val saveState: (value: Boolean) -> Unit
    ): SettingsModel(id = title) {
        var initialState: Boolean = getState()
    }

    data class Pref(
        @StringRes
        val title: Int,
        @StringRes
        val description: Int,
        val onClick: (() -> Unit)?
    ): SettingsModel(id = title)
}