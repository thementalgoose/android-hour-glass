package tmg.hourglass.core.crashlytics

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoogleAnalyticsManager @Inject constructor(
    @ApplicationContext
    val context: Context
): AnalyticsManager {

    override fun log(data: String) {
        FirebaseAnalytics.getInstance(context).logEvent(data, null)
    }
}