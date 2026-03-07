package tmg.hourglass.domain.repositories

import tmg.hourglass.domain.model.TagOrdering
import tmg.hourglass.domain.model.ThemeSelection

interface PreferencesManager {
    var analyticsEnabled: Boolean

    var crashReporting: Boolean

    var version: Int

    var theme: ThemeSelection

    val deviceUdid: String

    var sortOrder: TagOrdering
}