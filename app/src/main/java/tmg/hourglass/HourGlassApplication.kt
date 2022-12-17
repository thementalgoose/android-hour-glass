package tmg.hourglass

import android.app.Application
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate.*
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.jakewharton.threetenabp.AndroidThreeTen
import com.linkedin.android.shaky.EmailShakeDelegate
import com.linkedin.android.shaky.Shaky
import io.realm.Realm
import io.realm.RealmConfiguration
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import tmg.hourglass.di.hourGlassModule
import tmg.hourglass.prefs.PreferencesManager
import tmg.hourglass.prefs.ThemePref
import tmg.hourglass.realm.di.realmModule
import tmg.hourglass.realm.migrations.RealmDBMigration

class HourGlassApplication : Application() {

    private val prefs: PreferencesManager by inject()

    override fun onCreate() {
        super.onCreate()

        // Realm
        Realm.init(this)
        val config = RealmConfiguration.Builder()
            .schemaVersion(3L)
            .migration(RealmDBMigration())
            .allowWritesOnUiThread(true)
            .build()
        Realm.setDefaultConfiguration(config)

        // Koin
        startKoin {
            androidContext(this@HourGlassApplication)
            modules(hourGlassModule)
            modules(realmModule)
        }

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