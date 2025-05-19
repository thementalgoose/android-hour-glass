package tmg.hourglass.domain

import tmg.hourglass.domain.model.CountdownNotifications
import tmg.hourglass.domain.model.WidgetReference

fun WidgetReference.Companion.model(
    appWidgetId: Int = 9,
    countdownId: String = "countdownId",
    openAppOnClick: Boolean = true
): WidgetReference = WidgetReference(
    appWidgetId = appWidgetId,
    countdownId = countdownId,
    openAppOnClick = openAppOnClick
)