package tmg.hourglass.realm.connectors

import io.realm.*
import io.realm.kotlin.where
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneOffset
import tmg.hourglass.data.connectors.CountdownConnector
import tmg.hourglass.data.models.Countdown
import tmg.hourglass.realm.models.RealmCountdown
import tmg.hourglass.realm.models.convert
import tmg.hourglass.realm.models.saveSync

class RealmCountdownConnector : CountdownConnector {

    override fun allCurrent(): Flow<List<Countdown>> = flowableList(
        realmClass = RealmCountdown::class.java,
        where = {
            it
                .greaterThanOrEqualTo("end", now)
                .lessThanOrEqualTo("start", now)
        },
        convert = { it.convert() }
    )

    override fun allDone(): Flow<List<Countdown>> = flowableList(
        realmClass = RealmCountdown::class.java,
        where = { it.lessThanOrEqualTo("end", now) },
        convert = { it.convert() }
    )

    override fun all(): Flow<List<Countdown>> = flowableList(
        realmClass = RealmCountdown::class.java,
        where = { it },
        convert = { it.convert() }
    )

    override fun getSync(id: String): Countdown? {
        val realm: Realm = realm()
        val realmPassage = realm.where<RealmCountdown>().equalTo("id", id).findFirst()
        val passage = if (realmPassage != null) {
            realm.copyFromRealm(realmPassage)?.convert()
        } else {
            null
        }
        realm.close()
        return passage
    }

    override fun get(id: String): Flow<Countdown?> = flowable(
        realmClass = RealmCountdown::class.java,
        where = { it.equalTo("id", id) },
        convert = { it.convert() }
    )

    override fun saveSync(countdown: Countdown) {
        val realm: Realm = realm()
        countdown.saveSync(realm)
        realm.close()
    }

    override fun deleteAll() {
        val realm: Realm = realm()
        realm.executeTransaction {
            it.where<RealmCountdown>().findAll().deleteAllFromRealm()
        }
        realm.close()
    }

    override fun deleteDone() {
        val realm: Realm = realm()
        realm.executeTransaction { transRealm ->
            val results = transRealm.where<RealmCountdown>().lessThan("end", now).findAll()
            results.deleteAllFromRealm()
        }
    }

    override fun delete(id: String) {
        val realm: Realm = realm()
        realm.executeTransaction { transRealm ->
            transRealm.where<RealmCountdown>().equalTo("id", id).findFirst()?.deleteFromRealm()
        }
        realm.close()
    }

    //region Utils

    private val now: Long
        get() = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli()

    private fun realm(): Realm = Realm.getDefaultInstance()

    //endregion

    private fun <E : RealmObject, T> flowableList(
        realmClass: Class<E>,
        where: (q: RealmQuery<E>) -> RealmQuery<E>,
        convert: (model: E) -> T
    ): Flow<List<T>> = callbackFlow {
        val realm: Realm = realm()
        val query = where(realm.where(realmClass))
        val listener = RealmChangeListener<RealmResults<E>> { t ->
            offer(t.map { convert(it) })
        }
        val results = query.findAllAsync()
        results.addChangeListener(listener)
        awaitClose {
            results.removeAllChangeListeners()
            realm.close()
        }
    }

    private fun <E : RealmObject, T> flowable(
        realmClass: Class<E>,
        where: (q: RealmQuery<E>) -> RealmQuery<E>,
        convert: (model: E) -> T
    ): Flow<T?> = callbackFlow {
        val realm: Realm = realm()
        val query = where(realm.where(realmClass))
        @Suppress("SENSELESS_COMPARISON")
        val listener = RealmChangeListener<E> { t ->
            if (t != null) {
                offer(convert(t))
            } else {
                offer(null)
            }
        }
        val results = query.findFirstAsync()
        results.addChangeListener(listener)
        awaitClose {
            results.removeAllChangeListeners()
            realm.close()
        }
    }
}