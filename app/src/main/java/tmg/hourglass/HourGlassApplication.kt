package tmg.hourglass

import android.app.Application
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.Lazy
import dagger.hilt.android.HiltAndroidApp
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import tmg.hourglass.migration.RealmToRoomMigration
import tmg.hourglass.prefs.PreferencesManager
import tmg.hourglass.presentation.ThemePref
import tmg.hourglass.realm.migrations.RealmDBMigration
import tmg.hourglass.widgets.updateAllWidgets
import javax.inject.Inject

@HiltAndroidApp
class HourGlassApplication : Application() {

    @Inject
    lateinit var prefs: PreferencesManager

    @Inject
    lateinit var realmToRoomMigration: Lazy<RealmToRoomMigration>

    override fun onCreate() {
        super.onCreate()

        // Realm
        Realm.init(this)
        val config = RealmConfiguration.Builder()
            .schemaVersion(5L)
            .migration(RealmDBMigration(
                realmToRoomMigration = {
                    GlobalScope.launch {
                        Log.d("Realm", "Beginning Migration to Room")
                        realmToRoomMigration.get().migrate()
                        Log.d("Realm", "Migration to Room complete")
                    }
                }
            ))
            .allowWritesOnUiThread(true)
            .build()

        Realm.setDefaultConfiguration(config)

        // Night mode
        when (prefs.theme) {
            ThemePref.AUTO -> setDefaultNightMode(MODE_NIGHT_FOLLOW_SYSTEM)
            ThemePref.LIGHT -> setDefaultNightMode(MODE_NIGHT_NO)
            ThemePref.DARK -> setDefaultNightMode(MODE_NIGHT_YES)
        }

        // Crash Reporting
        Log.i("HourGlass", "Crash reporting ${if (prefs.crashReporting) "enabled" else "disabled"}")
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(prefs.crashReporting)
        if (!prefs.crashReporting) {
            FirebaseCrashlytics.getInstance().deleteUnsentReports()
        }

        // Analytics
        Log.i("HourGlass", "Analytics reporting ${if (prefs.analyticsEnabled) "enabled" else "disabled"}")
        FirebaseAnalytics.getInstance(this).setAnalyticsCollectionEnabled(prefs.analyticsEnabled)
        FirebaseAnalytics.getInstance(this).setUserId(prefs.deviceUdid)

        this.updateAllWidgets()
    }
}