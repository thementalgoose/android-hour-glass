package tmg.hourglass.core.crashlytics

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class GoogleAnalyticsManager @Inject constructor(
    @param:ApplicationContext
    val context: Context
): AnalyticsManager {

    private val instance: FirebaseAnalytics
        get() = FirebaseAnalytics.getInstance(context)

    override fun viewScreen(
        name: String,
        args: Map<String, String>
    ) {
        instance.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, name)
            args.forEach { (key, value) ->
                param(key, value)
            }
        }
    }

    override fun event(name: String, data: Map<String, String>) {
        instance.logEvent(name) {
            data.forEach { (key, value) ->
                param(key, value)
            }
        }
    }
}