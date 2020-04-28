package tmg.hourglass.prefs

import android.content.Context
import tmg.utilities.extensions.toEnum
import tmg.utilities.prefs.SharedPrefManager

interface PreferencesManager {
    var crashReporting: Boolean
    var shakeToReport: Boolean
    var version: Int

    var theme: ThemePref
}