package tmg.hourglass

import android.app.Application
import android.util.Log
import com.github.stkent.bugshaker.BugShaker
import com.github.stkent.bugshaker.flow.dialog.AlertDialogType
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.jakewharton.threetenabp.AndroidThreeTen
import io.realm.Realm
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import tmg.hourglass.di.hourGlassModule
import tmg.hourglass.prefs.IPrefs

val releaseNotes: Map<Int, Int> = mapOf(
    1 to R.string.release_1,
    2 to R.string.release_2
)

class HourGlassApplication: Application() {

    private val prefs: IPrefs by inject()

    override fun onCreate() {
        super.onCreate()

        // Realm
        Realm.init(this)

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