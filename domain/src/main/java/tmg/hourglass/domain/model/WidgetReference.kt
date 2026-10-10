package tmg.hourglass.domain.model

sealed interface WidgetReference {
    val appWidgetId: Int
    val openAppOnClick: Boolean

    data class Single(
        override val appWidgetId: Int,
        val countdownId: String,
        override val openAppOnClick: Boolean = false,
    ) : WidgetReference

    data class Multiple(
        override val appWidgetId: Int,
        val tagId: String?,
        override val openAppOnClick: Boolean = false,
    ) : WidgetReference

    companion object
}