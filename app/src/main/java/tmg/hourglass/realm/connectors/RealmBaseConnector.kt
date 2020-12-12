package tmg.hourglass.realm.connectors

import io.realm.Realm
import io.realm.RealmChangeListener
import io.realm.RealmObject
import io.realm.RealmQuery
import io.realm.RealmResults
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneOffset

open class RealmBaseConnector {

    //region Utils

    protected val now: Long
        get() = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli()

    protected fun realm(): Realm = Realm.getDefaultInstance()

    //endregion

    protected fun <E : RealmObject, T> flowableList(
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

    protected fun <E : RealmObject, T> flowable(
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