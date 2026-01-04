package tmg.hourglass.prefs

import tmg.hourglass.presentation.ThemePref
import tmg.hourglass.presentation.home.SortOrder

interface PreferencesManager {
    var analyticsEnabled: Boolean

    var crashReporting: Boolean

    var version: Int

    var theme: ThemePref

    val deviceUdid: String

    var realmMigrationRan: Boolean

    var sortOrder: SortOrder
}