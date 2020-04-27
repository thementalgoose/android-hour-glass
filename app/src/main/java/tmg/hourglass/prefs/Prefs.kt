package tmg.hourglass.prefs

import android.content.Context
import tmg.utilities.extensions.toEnum
import tmg.utilities.prefs.SharedPrefManager

interface IPrefs {
    var crashReporting: Boolean
    var shakeToReport: Boolean
    var version: Int

    var theme: ThemePref
}

private const val keyCrashReporting: String = "crashReporting"
private const val keyShakeToReport: String = "shakeToReport"
private const val keyVersion: String = "version"
private const val keyThemePref: String = "themePref"

class Prefs(context: Context): SharedPrefManager(context), IPrefs {

    override val prefsKey: String = "tmg.passage"

    override var crashReporting: Boolean
        get() = getBoolean(keyCrashReporting, true)
        set(value) = save(keyCrashReporting, value)

    override var shakeToReport: Boolean
        get() = getBoolean(keyShakeToReport, true)
        set(value) = save(keyShakeToReport, value)

    override var version: Int
        get() = getInt(keyVersion, 0)
        set(value) = save(keyVersion, value)

    override var theme: ThemePref
        get() = getString(keyThemePref, ThemePref.AUTO.key)?.toEnum<ThemePref> { it.key } ?: ThemePref.AUTO
        set(value) = save(keyThemePref, value.key)
}