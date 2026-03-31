package tmg.hourglass

import android.app.Application
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.HiltAndroidApp
import tmg.hourglass.domain.model.ThemeSelection
import tmg.hourglass.domain.repositories.PreferencesManager
import tmg.hourglass.widgets.updateAllWidgets
import javax.inject.Inject

@HiltAndroidApp
class HourGlassApplication : Application() {

    @Inject
    lateinit var prefs: PreferencesManager

    override fun onCreate() {
        super.onCreate()
        
        // Night mode
        when (prefs.theme) {
            ThemeSelection.FollowSystem -> setDefaultNightMode(MODE_NIGHT_FOLLOW_SYSTEM)
            ThemeSelection.Light -> setDefaultNightMode(MODE_NIGHT_NO)
            ThemeSelection.Dark -> setDefaultNightMode(MODE_NIGHT_YES)
        }

        // Crash Reporting
        Log.i("HourGlass", "Crash reporting ${if (prefs.crashReporting) "enabled" else "disabled"}")
        FirebaseCrashlytics.getInstance().isCrashlyticsCollectionEnabled = prefs.crashReporting
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