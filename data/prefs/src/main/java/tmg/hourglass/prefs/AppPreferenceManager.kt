package tmg.hourglass.prefs

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import tmg.hourglass.domain.model.TagOrdering
import tmg.hourglass.domain.model.ThemeSelection
import tmg.hourglass.domain.repositories.PreferencesManager
import tmg.utilities.extensions.toEnum
import tmg.utilities.prefs.SharedPrefManager
import tmg.utilities.prefs.SharedPrefManagerConfig
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

private const val keyCrashReporting: String = "crashReporting"
private const val keyAnalyticsReporting: String = "analyticsReporting"
private const val keyDeviceUdid: String = "deviceUdid"
private const val keyVersion: String = "version"
private const val keyThemePref: String = "themePref"
private const val keySortOrder: String = "sortOrder"

@Singleton
class AppPreferencesManager @Inject constructor(
    @ApplicationContext context: Context
): SharedPrefManager(context), PreferencesManager {

    override val prefConfig: SharedPrefManagerConfig
        get() = SharedPrefManagerConfig.Custom(
            prefsKey = "tmg.passage"
        )

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

    override var version: Int
        get() = getInt(keyVersion, 0)
        set(value) = save(keyVersion, value)

    override var theme: ThemeSelection
        get() = getString(keyThemePref, ThemeSelection.FollowSystem.key).toEnum<ThemeSelection> { it.key }
            ?: ThemeSelection.FollowSystem
        set(value) = save(keyThemePref, value.key)

    override var sortOrder: TagOrdering
        get() = getString(keySortOrder)?.toEnum<TagOrdering> { it.key } ?: TagOrdering.ALPHABETICAL
        set(value) = save(keySortOrder, value.key)

    private val ThemeSelection.key: String
        get() = when (this) {
            ThemeSelection.FollowSystem -> "AUTO"
            ThemeSelection.Light -> "LIGHT"
            ThemeSelection.Dark -> "DARK"
        }

    private val TagOrdering.key: String
        get() = when (this) {
            TagOrdering.ALPHABETICAL -> "alphabetical"
            TagOrdering.FINISHING_SOONEST -> "finishing_soonest"
            TagOrdering.FINISHING_LATEST -> "finishing_latest"
            TagOrdering.PROGRESS -> "progress"
        }
}