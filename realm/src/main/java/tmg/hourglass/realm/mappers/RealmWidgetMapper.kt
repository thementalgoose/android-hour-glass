package tmg.hourglass.realm.mappers

import tmg.hourglass.domain.model.WidgetReference
import tmg.hourglass.realm.models.RealmWidgetReference

class RealmWidgetMapper {

    fun deserialize(input: RealmWidgetReference): WidgetReference {
        return WidgetReference(
            appWidgetId = input.appWidgetId,
            countdownId = input.countdownId
        )
    }

    fun serialize(model: RealmWidgetReference, data: WidgetReference) {
        model.appWidgetId = data.appWidgetId
        model.countdownId = data.countdownId
    }
}