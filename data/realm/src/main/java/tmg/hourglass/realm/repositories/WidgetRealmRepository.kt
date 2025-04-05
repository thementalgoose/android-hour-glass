package tmg.hourglass.realm.repositories

import android.util.Log
import io.realm.kotlin.where
import tmg.hourglass.domain.repositories.WidgetRepository
import tmg.hourglass.domain.model.WidgetReference
import tmg.hourglass.realm.mappers.RealmWidgetMapper
import tmg.hourglass.realm.models.RealmWidgetReference
import javax.inject.Inject
import javax.inject.Singleton

@Suppress("EXPERIMENTAL_API_USAGE")
@Singleton
class WidgetRealmRepository @Inject constructor(
    private val widgetMapper: RealmWidgetMapper
): BaseRealmRepository(), WidgetRepository {

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