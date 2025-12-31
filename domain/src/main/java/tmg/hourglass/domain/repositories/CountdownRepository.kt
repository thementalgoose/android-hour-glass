package tmg.hourglass.domain.repositories

import kotlinx.coroutines.flow.Flow
import tmg.hourglass.domain.model.Countdown

interface CountdownRepository {
    fun allCurrent(): Flow<List<Countdown>>
    fun allDone(): Flow<List<Countdown>>
    fun all(): Flow<List<Countdown>>

    fun getSync(id: String): Countdown?
    fun get(id: String): Flow<Countdown?>

    fun saveSync(countdown: Countdown)
    fun saveAll(countdowns: List<Countdown>)

    fun deleteAll()
    fun delete(id: String)
}