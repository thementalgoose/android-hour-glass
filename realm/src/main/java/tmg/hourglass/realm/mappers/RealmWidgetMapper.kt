package tmg.hourglass.realm.mappers

import android.util.Log
import tmg.hourglass.domain.model.WidgetReference
import tmg.hourglass.realm.models.RealmWidgetReference
import javax.inject.Inject

class RealmWidgetMapper @Inject constructor() {

    fun deserialize(input: RealmWidgetReference): WidgetReference {
        return WidgetReference(
            appWidgetId = input.appWidgetId,
            countdownId = input.countdownId
        )
    }

    fun serialize(model: RealmWidgetReference, data: WidgetReference) {
        if (model.appWidgetId != data.appWidgetId) {
            model.appWidgetId = data.appWidgetId
        }
        model.countdownId = data.countdownId
    }
}