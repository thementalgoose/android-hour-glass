package tmg.hourglass.core.crashlytics

interface AnalyticsManager {
    fun viewScreen(name: String, args: Map<String, String>)
    fun event(name: String, data: Map<String, String>)
}