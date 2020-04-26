package tmg.passage.data.connectors

import kotlinx.coroutines.flow.Flow
import tmg.passage.data.models.Passage

interface PassageConnector {
    fun allCurrent(): Flow<List<Passage>>
    fun allDone(): Flow<List<Passage>>
    fun allEndingToday(): Flow<List<Passage>>

    fun getSync(id: String): Passage?
    fun get(id: String): Flow<Passage?>

    fun saveSync(passage: Passage)

    fun deleteAll()
    fun deleteDone()
    fun delete(id: String)
}