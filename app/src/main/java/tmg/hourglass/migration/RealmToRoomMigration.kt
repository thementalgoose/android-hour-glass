package tmg.hourglass.migration

import android.util.Log
import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.coroutines.flow.firstOrNull
import tmg.hourglass.domain.repositories.CountdownRepository
import tmg.hourglass.domain.repositories.WidgetRepository
import tmg.hourglass.prefs.PreferencesManager
import tmg.hourglass.realm.mappers.RealmCountdownMapper
import tmg.hourglass.realm.mappers.RealmWidgetMapper
import tmg.hourglass.realm.models.RealmCountdown
import tmg.hourglass.realm.models.RealmWidgetReference
import tmg.hourglass.realm.repositories.CountdownRealmRepository
import tmg.hourglass.realm.repositories.WidgetRealmRepository
import javax.inject.Inject

/**
 * Migration code to migrate realm to room DB
 * To be removed sometime in 2026 to avoid loss of data
 */
class RealmToRoomMigration @Inject constructor(
    private val realmCountdownMapper: RealmCountdownMapper,
    private val realmWidgetMapper: RealmWidgetMapper,
    private val roomCountdownRepository: CountdownRepository,
    private val roomWidgetRepository: WidgetRepository,
    private val preferencesManager: PreferencesManager
) {
    fun migrate() {
        if (!preferencesManager.realmMigrationRan) {
            Log.d("RealmMigration", "Checking Realm -> Room migration")
            val realm = Realm.getDefaultInstance()
            realm.executeTransaction { realm ->
                val widgets = realm.where<RealmWidgetReference>().findAll()
                val widgetModels = widgets.map { realmWidgetMapper.deserialize(it) }
                for (x in widgetModels) {
                    roomWidgetRepository.saveSync(x)
                }
                widgets.deleteAllFromRealm()

                val countdowns = realm.where<RealmCountdown>().findAll()
                val countdownModels = countdowns.map { realmCountdownMapper.deserialize(it) }
                roomCountdownRepository.saveAll(countdownModels)
                countdowns.deleteAllFromRealm()
            }
            realm.close()
            Log.d("RealmMigration", "Realm -> Room migration complete")
            preferencesManager.realmMigrationRan = true
        }
    }
}