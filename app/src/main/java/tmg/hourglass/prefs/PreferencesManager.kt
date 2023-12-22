package tmg.hourglass.prefs

import tmg.hourglass.presentation.ThemePref

interface PreferencesManager {
    var analyticsEnabled: Boolean
    var crashReporting: Boolean
    var shakeToReport: Boolean
    var version: Int

    var widgetShowUpdate: Boolean

    var theme: ThemePref

    val deviceUdid: String
}