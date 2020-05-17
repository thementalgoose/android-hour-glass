package tmg.hourglass.crash

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import tmg.hourglass.BuildConfig

class FirebaseCrashReporter: CrashReporter {
    override fun log(msg: String) {
        if (!BuildConfig.DEBUG) {
            FirebaseCrashlytics.getInstance().log(msg)
        }
        else {
            Log.e("HourGlass", "Exception thrown: $msg")
        }
    }

    override fun logException(throwable: Throwable) {
        if (!BuildConfig.DEBUG) {
            FirebaseCrashlytics.getInstance().recordException(throwable)
        }
        else {
            Log.e("HourGlass", "Exception thrown: ${throwable.message}")
            throwable.printStackTrace()
        }
    }
}