package tmg.hourglass.data.connectors

import kotlinx.coroutines.flow.Flow
import tmg.hourglass.data.models.Countdown

interface CountdownConnector {
    fun allCurrent(): Flow<List<Countdown>>
    fun allDone(): Flow<List<Countdown>>
    fun all(): Flow<List<Countdown>>

    fun getSync(id: String): Countdown?
    fun get(id: String): Flow<Countdown?>

    fun saveSync(countdown: Countdown)

    fun deleteAll()
    fun deleteDone()
    fun delete(id: String)
}