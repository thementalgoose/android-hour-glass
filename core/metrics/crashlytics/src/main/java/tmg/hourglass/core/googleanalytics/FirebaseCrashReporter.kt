package tmg.hourglass.core.googleanalytics

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class FirebaseCrashReporter @Inject constructor(): CrashReporter {
    override fun log(msg: String) {
        FirebaseCrashlytics.getInstance().log(msg)
        Log.e("HourGlass", "Exception thrown: $msg")
    }

    override fun logException(throwable: Throwable) {
        FirebaseCrashlytics.getInstance().recordException(throwable)
        Log.e("HourGlass", "Exception thrown: ${throwable.message}")
        if (BuildConfig.DEBUG) {
            throwable.printStackTrace()
        }
    }
}