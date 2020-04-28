package tmg.hourglass.analytics

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics

class FirebaseAnalyticsManager(val context: Context): AnalyticsManager {

    override fun log(data: String) {
        FirebaseAnalytics.getInstance(context).logEvent(data, null)
    }
}