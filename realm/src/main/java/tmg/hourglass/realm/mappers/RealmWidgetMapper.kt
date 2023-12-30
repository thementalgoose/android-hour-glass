package tmg.hourglass.realm.mappers

import android.util.Log
import tmg.hourglass.domain.model.WidgetReference
import tmg.hourglass.realm.models.RealmWidgetReference
import javax.inject.Inject

class RealmWidgetMapper @Inject constructor() {

    fun deserialize(input: RealmWidgetReference): WidgetReference {
        Log.i("Mapper", "Deserializing widget ${input.appWidgetId} (${input.countdownId})")
        return WidgetReference(
            appWidgetId = input.appWidgetId,
            countdownId = input.countdownId
        )
    }

    fun serialize(model: RealmWidgetReference, data: WidgetReference) {
        Log.i("Mapper", "Serializing widget ${data.appWidgetId} (${data.countdownId})")
        if (model.appWidgetId != data.appWidgetId) {
            model.appWidgetId = data.appWidgetId
        }
        model.countdownId = data.countdownId
    }
}