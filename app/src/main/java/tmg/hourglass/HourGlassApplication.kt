package tmg.hourglass

import android.app.Application
import android.util.Log
import com.github.stkent.bugshaker.BugShaker
import com.github.stkent.bugshaker.flow.dialog.AlertDialogType
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.jakewharton.threetenabp.AndroidThreeTen
import io.realm.Realm
import io.realm.RealmConfiguration
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import tmg.hourglass.di.hourGlassModule
import tmg.hourglass.prefs.PreferencesManager
import tmg.hourglass.realm.RealmDBMigration

val releaseNotes: Map<Int, Int> = mapOf(
    23 to R.string.release_23,
    22 to R.string.release_22,
    20 to R.string.release_20,
    19 to R.string.release_19,
    18 to R.string.release_18,
    16 to R.string.release_16,
    15 to R.string.release_15,
    14 to R.string.release_14,
    13 to R.string.release_13,
    12 to R.string.release_12,
    11 to R.string.release_11,
    10 to R.string.release_10,
    9 to R.string.release_9,
    8 to R.string.release_8,
    7 to R.string.release_7,
    6 to R.string.release_6,
    5 to R.string.release_5,
    4 to R.string.release_4,
    3 to R.string.release_3,
    2 to R.string.release_2,
    1 to R.string.release_1
)

class HourGlassApplication : Application() {

    private val prefs: PreferencesManager by inject()

    override fun onCreate() {
        super.onCreate()

        // Realm
        Realm.init(this)
        val config = RealmConfiguration.Builder()
            .schemaVersion(2L)
            .migration(RealmDBMigration())
            .build()
        Realm.setDefaultConfiguration(config)

        // Koin
        startKoin {
            androidContext(this@HourGlassApplication)
            modules(hourGlassModule)
        }

        // ThreeTen
        AndroidThreeTen.init(this)

        // Shake to report a bug
        if (prefs.shakeToReport) {
            BugShaker.get(this)
                .setEmailAddresses("thementalgoose@gmail.com")
                .setEmailSubjectLine("${getString(R.string.app_name)} - App Feedback")
                .setAlertDialogType(AlertDialogType.APP_COMPAT)
                .setLoggingEnabled(BuildConfig.DEBUG)
                .assemble()
                .start()
        }

        // Crash Reporting
        Log.i("HourGlass", "Crash reporting ${if (prefs.crashReporting) "enabled" else "disabled"}")
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(prefs.crashReporting)
        if (!prefs.crashReporting) {
            FirebaseCrashlytics.getInstance().deleteUnsentReports()
        }
    }
}