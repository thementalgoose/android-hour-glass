package tmg.hourglass.domain.model

data class WidgetReference(
    val appWidgetId: Int,
    val countdownId: String,
    val openAppOnClick: Boolean
) {
    companion object
}