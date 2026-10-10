package tmg.hourglass.room.mappers

import tmg.hourglass.domain.model.WidgetReference
import javax.inject.Inject

internal class WidgetMapper @Inject constructor() {
    fun serialize(model: WidgetReference) = when (model) {
        is WidgetReference.Single -> tmg.hourglass.room.models.WidgetReference(
            appWidgetId = model.appWidgetId,
            countdownId = model.countdownId,
            tagId = null,
            openAppOnClick = model.openAppOnClick
        )
        is WidgetReference.Multiple -> tmg.hourglass.room.models.WidgetReference(
            appWidgetId = model.appWidgetId,
            countdownId = null,
            tagId = model.tagId,
            openAppOnClick = model.openAppOnClick
        )
    }

    fun deserialize(model: tmg.hourglass.room.models.WidgetReference): WidgetReference =
        if (model.countdownId != null) {
            WidgetReference.Single(
                appWidgetId = model.appWidgetId,
                countdownId = model.countdownId,
                openAppOnClick = model.openAppOnClick
            )
        } else {
            WidgetReference.Multiple(
                appWidgetId = model.appWidgetId,
                tagId = model.tagId,
                openAppOnClick = model.openAppOnClick
            )
        }
}