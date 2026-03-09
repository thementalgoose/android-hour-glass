package tmg.hourglass.core.crashlytics.screenview

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import tmg.hourglass.core.crashlytics.AnalyticsManager
import javax.inject.Inject

@HiltViewModel
class ScreenViewViewModel @Inject constructor(
    private val analyticsManager: AnalyticsManager,
): ViewModel() {

    fun viewScreen(name: String, args: Map<String, String>) {
        analyticsManager.viewScreen(name, args)
    }
}