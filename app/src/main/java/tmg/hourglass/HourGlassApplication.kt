package tmg.hourglass

import android.app.Application
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.jakewharton.threetenabp.AndroidThreeTen
import com.linkedin.android.shaky.EmailShakeDelegate
import com.linkedin.android.shaky.Shaky
import dagger.hilt.android.HiltAndroidApp
import io.realm.Realm
import io.realm.RealmConfiguration
import tmg.hourglass.prefs.PreferencesManager
import tmg.hourglass.presentation.ThemePref
import tmg.hourglass.realm.migrations.RealmDBMigration
import javax.inject.Inject

@HiltAndroidApp
class HourGlassApplication : Application() {

    @Inject
    protected lateinit var prefs: PreferencesManager

    override fun onCreate() {
        super.onCreate()

        // Realm
        Realm.init(this)
        val config = RealmConfiguration.Builder()
            .schemaVersion(4L)
            .migration(RealmDBMigration())
            .allowWritesOnUiThread(true)
            .build()
        Realm.setDefaultConfiguration(config)

        // Night mode
        when (prefs.theme) {
            ThemePref.AUTO -> setDefaultNightMode(MODE_NIGHT_FOLLOW_SYSTEM)
            ThemePref.LIGHT -> setDefaultNightMode(MODE_NIGHT_NO)
            ThemePref.DARK -> setDefaultNightMode(MODE_NIGHT_YES)
        }

        // ThreeTen
        AndroidThreeTen.init(this)

        // Shake to report a bug
        if (prefs.shakeToReport) {
            Log.i("Startup", "Enabling shake to report")

            Shaky.with(this, object : EmailShakeDelegate("thementalgoose@gmail.com") {
                override fun getTheme() = super.getTheme()
                override fun getPopupTheme() = super.getPopupTheme()
            })
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
    }
}