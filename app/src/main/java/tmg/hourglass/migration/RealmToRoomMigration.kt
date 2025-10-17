package tmg.hourglass.migration

import kotlinx.coroutines.flow.firstOrNull
import tmg.hourglass.domain.repositories.CountdownRepository
import tmg.hourglass.domain.repositories.WidgetRepository
import tmg.hourglass.realm.repositories.CountdownRealmRepository
import tmg.hourglass.realm.repositories.WidgetRealmRepository
import javax.inject.Inject

class RealmToRoomMigration @Inject constructor(
    private val realmCountdownRepository: CountdownRealmRepository,
    private val realmWidgetRepository: WidgetRealmRepository,
    private val roomCountdownRepository: CountdownRepository,
    private val roomWidgetRepository: WidgetRepository,
) {
    suspend fun migrate() {
        migrateWidgets()
        migrateCountdowns()
    }
    private suspend fun migrateCountdowns() {
        val realmRecords = realmCountdownRepository.all().firstOrNull()
        if (realmRecords != null) {
            // Realm migration required
            for (x in realmRecords) {
                roomCountdownRepository.saveSync(x)
            }
            realmCountdownRepository.deleteAll()
        }
    }
    private suspend fun migrateWidgets() {
        val realmRecords = realmWidgetRepository.all().firstOrNull()
        if (realmRecords != null) {
            // Realm migration required
            for (x in realmRecords) {
                roomWidgetRepository.saveSync(x)
            }
            realmWidgetRepository.deleteAll()
        }
    }
}