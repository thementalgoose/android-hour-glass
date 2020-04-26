package tmg.passage.realm.connectors

import io.realm.*
import io.realm.kotlin.where
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneOffset
import tmg.passage.data.connectors.PassageConnector
import tmg.passage.data.models.Passage
import tmg.passage.realm.models.RealmPassage
import tmg.passage.realm.models.convert
import tmg.passage.realm.models.saveSync
import kotlin.coroutines.resume

class RealmPassageConnector : PassageConnector {

    override fun allCurrent(): Flow<List<Passage>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun allDone(): Flow<List<Passage>> {
        asFlow()
    }

    override fun allEndingToday(): Flow<List<Passage>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getSync(id: String): Passage? {
        val realm: Realm = realm()
        val realmPassage = realm.where<RealmPassage>().equalTo("id", id).findFirst()
        val passage = if (realmPassage != null) {
            realm.copyFromRealm(realmPassage)?.convert()
        } else {
            null
        }
        realm.close()
        return passage
    }

    override fun get(id: String): Flow<Passage?> {
        return flow {
            val result = asFlow(RealmPassage::class.java,
                query = { it.equalTo("id", id) },
                convert = { it.convert() })

        }
    }

    override fun saveSync(passage: Passage) {
        val realm: Realm = realm()
        passage.saveSync(realm)
        realm.close()
    }

    override fun deleteAll() {
        val realm: Realm = realm()
        realm.executeTransaction {
            it.where<RealmPassage>().findAll().deleteAllFromRealm()
        }
        realm.close()
    }

    override fun deleteDone() {
        val realm: Realm = realm()
        realm.executeTransaction { transRealm ->
            val results = transRealm.where<RealmPassage>().lessThan("end", now).findAll()
            results.deleteAllFromRealm()
        }
    }

    override fun delete(id: String) {
        val realm: Realm = realm()
        realm.executeTransaction { transRealm ->
            transRealm.where<RealmPassage>().equalTo("id", id).findFirst()?.deleteFromRealm()
        }
        realm.close()
    }

    private val now: Long
        get() = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli()

    private fun realm(): Realm = Realm.getDefaultInstance()

    suspend fun <E : RealmObject, R> asFlow(
        zClass: Class<E>,
        query: (realm: RealmQuery<E>) -> RealmQuery<E>,
        convert: (model: E) -> R
    ) = suspendCancellableCoroutine<R> { continuation ->
        val realm: Realm = this@RealmPassageConnector.realm
        val listener = RealmChangeListener<E> { t -> continuation.resume(convert(t)) }
        val results = realm.where(zClass).findFirstAsync()
        results.addChangeListener(listener)
        continuation.invokeOnCancellation {
            results.removeAllChangeListeners()
            realm.close()
        }
    }

    suspend fun <E : RealmObject, R> asFlowList(
        realmQuery: RealmQuery<E>,
        convert: (model: E) -> R
    ) = suspendCancellableCoroutine<List<R>> { continuation ->
        val realm: Realm = realm
        val listener =
            RealmChangeListener<RealmResults<E>> { t -> continuation.resume(t.map { convert(it) }) }
        val results = realmQuery.findAllAsync()
        results.addChangeListener(listener)
        continuation.invokeOnCancellation {
            results.removeAllChangeListeners()
            realm.close()
        }
    }
}