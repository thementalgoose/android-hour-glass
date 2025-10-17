package tmg.hourglass.migration

import kotlinx.coroutines.flow.firstOrNull
import tmg.hourglass.domain.repositories.CountdownRepository
import tmg.hourglass.domain.repositories.RealmRepo
import tmg.hourglass.domain.repositories.RoomRepo
import tmg.hourglass.domain.repositories.WidgetRepository
import javax.inject.Inject

class RealmToRoomMigration @Inject constructor(
    @param:RealmRepo
    private val realmCountdownRepository: CountdownRepository,
    @param:RealmRepo
    private val realmWidgetRepository: WidgetRepository,
    @param:RoomRepo
    private val roomCountdownRepository: CountdownRepository,
    @param:RoomRepo
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
            roomWidgetRepository.deleteAll()
        }
    }
}