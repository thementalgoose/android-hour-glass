package tmg.hourglass.domain

import tmg.hourglass.domain.model.WidgetReference

fun WidgetReference.Companion.model(
    appWidgetId: Int = 9,
    countdownId: String = "countdownId",
    openAppOnClick: Boolean = true
): WidgetReference.Single = WidgetReference.Single(
    appWidgetId = appWidgetId,
    countdownId = countdownId,
    openAppOnClick = openAppOnClick
)

fun WidgetReference.Companion.modelMultiple(
    appWidgetId: Int = 9,
    tagId: String? = "tagId",
    openAppOnClick: Boolean = true
): WidgetReference.Multiple = WidgetReference.Multiple(
    appWidgetId = appWidgetId,
    tagId = tagId,
    openAppOnClick = openAppOnClick
)