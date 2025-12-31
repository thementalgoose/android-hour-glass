package tmg.hourglass.room.repositories

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import tmg.hourglass.domain.model.Countdown
import tmg.hourglass.domain.repositories.CountdownRepository
import tmg.hourglass.room.BuildConfig
import tmg.hourglass.room.dao.CountdownDao
import tmg.hourglass.room.mappers.CountdownMapper
import java.time.LocalDateTime
import javax.inject.Inject

internal class CountdownRepositoryImpl @Inject constructor(
    private val countdownDao: CountdownDao,
    private val countdownMapper: CountdownMapper,
): CountdownRepository {
    override fun allCurrent(): Flow<List<Countdown>> {
        val nowLocalDateTime = LocalDateTime.now()
        return countdownDao.getCountdowns()
            .map { list -> list
                .map { countdownMapper.deserialize(it) }
                .filter { it.endDate > nowLocalDateTime || it is Countdown.Recurring }
                .sortedBy { it.name.lowercase() }
                .also {
                    if (BuildConfig.DEBUG) {
                        Log.d("Room", "allCurrent() Returning $it")
                    }
                }
            }
    }

    override fun allDone(): Flow<List<Countdown>> {
        val nowLocalDateTime = LocalDateTime.now()
        return countdownDao.getCountdowns()
            .map { list -> list
                .map { countdownMapper.deserialize(it) }
                .filter { it.endDate <= nowLocalDateTime && it is Countdown.Static }
                .sortedBy { it.endDate }
                .also {
                    if (BuildConfig.DEBUG) {
                        Log.d("Room", "allDone() Returning $it")
                    }
                }
            }
    }

    override fun all(): Flow<List<Countdown>> {
        return countdownDao.getCountdowns()
            .map { list -> list
                .map { countdownMapper.deserialize(it) }
                .sortedBy { it.endDate }
                .also {
                    if (BuildConfig.DEBUG) {
                        Log.d("Room", "all() Returning $it")
                    }
                }
            }
    }

    override fun getSync(id: String): Countdown? {
        return runBlocking {
            val model = countdownDao.getCountdown(id).firstOrNull() ?: return@runBlocking null
            return@runBlocking countdownMapper.deserialize(model)
        }
    }

    override fun get(id: String): Flow<Countdown?> {
        return countdownDao.getCountdown(id).map {
            it?.let { countdownMapper.deserialize(it) }
        }
    }

    override fun saveSync(countdown: Countdown) {
        runBlocking {
            Log.d("Room", "Saving $countdown")
            countdownDao.insertCountdown(countdownMapper.serialize(countdown))
        }
    }

    override fun saveAll(countdowns: List<Countdown>) {
        runBlocking {
            Log.d("Room", "Saving ${countdowns.joinToString { it.id }}")
            countdownDao.insertAllCountdown(
                countdowns.map { countdownMapper.serialize(it) }
            )
        }
    }

    override fun deleteAll() {
        runBlocking { countdownDao.deleteAllCountdowns() }
    }

    override fun delete(id: String) {
        runBlocking { countdownDao.deleteCountdown(id) }
    }

}