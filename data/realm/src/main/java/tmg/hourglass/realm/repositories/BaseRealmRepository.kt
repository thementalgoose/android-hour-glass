package tmg.hourglass.realm.repositories

import io.realm.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.time.LocalDateTime
import java.time.ZoneOffset

open class BaseRealmRepository {

    //region Utils
    protected val now: Long
        get() = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli()

    protected fun realm(): Realm = Realm.getDefaultInstance()

    protected fun realm(closure: (Realm) -> Unit) {
        val realm = realm()
        closure(realm)
        realm.close()
    }

    protected fun <T> realmGet(closure: (Realm) -> T): T {
        val realm = realm()
        val result = closure(realm)
        realm.close()
        return result
    }

    protected fun realmAsync(closure: (Realm) -> Unit) {
        val realm = realm()
        realm.executeTransactionAsync(closure)
        realm.close()
    }

    protected fun realmSync(closure: (Realm) -> Unit) {
        val realm = realm()
        realm.executeTransaction(closure)
        realm.close()
    }

    //endregion

    protected fun <E : RealmObject, T> flowableList(
        realmClass: Class<E>,
        where: (q: RealmQuery<E>) -> RealmQuery<E>,
        convert: (model: E) -> T
    ): Flow<List<T>> = callbackFlow {
        val realm: Realm = realm()
        val query = where(realm.where(realmClass))
        val listener = RealmChangeListener<RealmResults<E>> { t ->
            trySend(t.map { convert(it) })
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
        where: (q: RealmQuery<E>) -> RealmQuery<E> ,
        convert: (model: E) -> T
    ): Flow<T?> = callbackFlow {
        val realm: Realm = realm()
        val query = where(realm.where(realmClass))
        @Suppress("SENSELESS_COMPARISON")
        val listener = RealmChangeListener<E> { t ->
            if (t != null && t.isValid()) {
                trySend(convert(realm.copyFromRealm(t)))
            } else {
                trySend(null)
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