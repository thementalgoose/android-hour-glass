package tmg.hourglass.room.mappers

import tmg.hourglass.domain.model.WidgetReference
import javax.inject.Inject

internal class WidgetMapper @Inject constructor() {
    fun serialize(model: WidgetReference) = tmg.hourglass.room.models.WidgetReference(
        appWidgetId = model.appWidgetId,
        countdownId = model.countdownId,
        openAppOnClick = model.openAppOnClick
    )

    fun deserialize(model: tmg.hourglass.room.models.WidgetReference) = WidgetReference(
        appWidgetId = model.appWidgetId,
        countdownId = model.countdownId,
        openAppOnClick = model.openAppOnClick
    )
}