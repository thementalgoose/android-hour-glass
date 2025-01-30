package tmg.hourglass.prefs

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import tmg.hourglass.presentation.ThemePref
import tmg.utilities.extensions.toEnum
import tmg.utilities.prefs.SharedPrefManager
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

private const val keyCrashReporting: String = "crashReporting"
private const val keyAnalyticsReporting: String = "analyticsReporting"
private const val keyDeviceUdid: String = "deviceUdid"
private const val keyShakeToReport: String = "shakeToReport"
private const val keyVersion: String = "version"
private const val keyThemePref: String = "themePref"

@Singleton
class AppPreferencesManager @Inject constructor(
    @ApplicationContext context: Context
): SharedPrefManager(context), PreferencesManager {

    override val prefsKey: String = "tmg.passage"

    override var crashReporting: Boolean
        get() = getBoolean(keyCrashReporting, true)
        set(value) = save(keyCrashReporting, value)

    override var analyticsEnabled: Boolean
        get() = getBoolean(keyAnalyticsReporting, true)
        set(value) = save(keyAnalyticsReporting, value)

    override val deviceUdid: String
        get() {
            val existing = getString(keyDeviceUdid, "")
            if (existing.isNullOrEmpty()) {
                save(keyDeviceUdid, UUID.randomUUID().toString())
            }
            return getString(keyDeviceUdid, "") ?: UUID.randomUUID().toString()
        }

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