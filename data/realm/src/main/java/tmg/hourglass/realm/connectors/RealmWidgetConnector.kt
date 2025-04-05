package tmg.hourglass.realm.connectors

import android.util.Log
import io.realm.Realm
import io.realm.RealmChangeListener
import io.realm.RealmObject
import io.realm.RealmQuery
import io.realm.kotlin.where
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import tmg.hourglass.domain.connectors.CountdownConnector
import tmg.hourglass.domain.connectors.WidgetConnector
import tmg.hourglass.domain.model.Countdown
import tmg.hourglass.domain.model.WidgetReference
import tmg.hourglass.realm.mappers.RealmWidgetMapper
import tmg.hourglass.realm.models.RealmWidgetReference
import javax.inject.Inject
import javax.inject.Singleton

@Suppress("EXPERIMENTAL_API_USAGE")
@Singleton
class RealmWidgetConnector @Inject constructor(
    private val widgetMapper: RealmWidgetMapper
): RealmBaseConnector(), WidgetConnector {

    override fun saveSync(widgetReference: WidgetReference) = realmSync { realm ->
        Log.i("Realm", "Widgets: Saving $widgetReference")
        val model: RealmWidgetReference = realm
            .where<RealmWidgetReference>()
            .equalTo("appWidgetId", widgetReference.appWidgetId)
            .findFirst() ?: realm.createObject(
            RealmWidgetReference::class.java,
            widgetReference.appWidgetId
        )
        widgetMapper.serialize(model, widgetReference)
    }

    override fun getSync(appWidgetId: Int): WidgetReference? = realmGet { realm ->
        Log.i("Realm", "Widgets: Getting $appWidgetId")
        val realmRef = realm
            .where(RealmWidgetReference::class.java)
            .equalTo("appWidgetId", appWidgetId)
            .findFirst()

        return@realmGet when {
            realmRef != null -> widgetMapper.deserialize(realm.copyFromRealm(realmRef))
            else -> null
        }
    }
}