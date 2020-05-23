package tmg.hourglass.realm.connectors

import io.realm.Realm
import io.realm.kotlin.where
import tmg.hourglass.data.connectors.CountdownConnector
import tmg.hourglass.data.connectors.WidgetConnector
import tmg.hourglass.data.models.Countdown
import tmg.hourglass.data.models.WidgetReference
import tmg.hourglass.realm.models.RealmWidgetReference
import tmg.hourglass.realm.models.convert
import tmg.hourglass.realm.models.saveSync

class RealmWidgetConnector(
    private val countdownConnector: CountdownConnector
): RealmBaseConnector(), WidgetConnector {
    override fun saveSync(appWidgetId: Int, countdownId: String) {
        val widgetReference = WidgetReference(appWidgetId, countdownId)
        val realm: Realm = realm()
        widgetReference.saveSync(realm)
        realm.close()
    }

    override fun getWidgetReferenceSync(appWidgetId: Int): WidgetReference? {
        val realm: Realm = realm()
        val realmRef = realm.where<RealmWidgetReference>().equalTo("appWidgetId", appWidgetId).findFirst()
        val widgetReference = if (realmRef != null) {
            realm.copyFromRealm(realmRef)?.convert()
        } else null
        realm.close()
        return widgetReference
    }

    override fun getCountdownModelSync(appWidgetId: Int): Countdown? {
        val ref = getWidgetReferenceSync(appWidgetId)
        return ref?.let { countdownConnector.getSync(it.countdownId) }
    }
}