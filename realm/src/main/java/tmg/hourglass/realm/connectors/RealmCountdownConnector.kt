package tmg.hourglass.realm.connectors

import android.util.Log
import io.realm.RealmList
import io.realm.kotlin.where
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import tmg.hourglass.domain.connectors.CountdownConnector
import tmg.hourglass.domain.model.Countdown
import tmg.hourglass.realm.mappers.RealmCountdownMapper
import tmg.hourglass.realm.mappers.RealmCountdownNotificationMapper
import tmg.hourglass.realm.models.RealmCountdown
import tmg.hourglass.realm.models.RealmCountdownNotifications
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RealmCountdownConnector @Inject constructor(
    private val countdownMapper: RealmCountdownMapper,
    private val countdownNotificationMapper: RealmCountdownNotificationMapper
): RealmBaseConnector(), CountdownConnector {

    override fun allCurrent(): Flow<List<Countdown>> = flowableList(
        realmClass = RealmCountdown::class.java,
        where = { it.greaterThanOrEqualTo("end", now) },
        convert = { countdownMapper.deserialize(it) }
    ).map { list ->
        val nowLocalDateTime = LocalDateTime.now()
        mutableListOf<Countdown>().apply {
            addAll(list
                .filter { it.start <= nowLocalDateTime }
                .sortedBy { it.end })
            addAll(list
                .filter { it.start > nowLocalDateTime }
                .sortedBy { it.start })
        }
    }

    override fun allDone(): Flow<List<Countdown>> = flowableList(
        realmClass = RealmCountdown::class.java,
        where = { it.lessThanOrEqualTo("end", now) },
        convert = {
            countdownMapper.deserialize(it)
        }
    )

    override fun all(): Flow<List<Countdown>> = flowableList(
        realmClass = RealmCountdown::class.java,
        where = { it },
        convert = { countdownMapper.deserialize(it) }
    ).map { list ->
        list.sortedBy { it.end }
            .sortedBy { it.isFinished }
    }

    override fun getSync(id: String): Countdown? = realmGet { realm ->
        val model: RealmCountdown? = realm
            .where<RealmCountdown>()
            .equalTo("id", id)
            .findFirst()

        return@realmGet when {
            model != null -> countdownMapper.deserialize(realm.copyFromRealm(model))
            else -> null
        }
    }

    override fun get(id: String): Flow<Countdown?> = flowable(
        realmClass = RealmCountdown::class.java,
        where = { it.equalTo("id", id) },
        convert = { countdownMapper.deserialize(it) }
    )

    override fun saveSync(countdown: Countdown) = realmSync { realm ->
        val model = realm
            .where<RealmCountdown>()
            .equalTo("id", countdown.id)
            .findFirst()
            ?: realm.createObject(RealmCountdown::class.java, countdown.id)

        countdownMapper.serialize(model, countdown)

        model.notifications.deleteAllFromRealm()
        val notifications = countdown.notifications
            .map { c ->
                val notificationModel = realm
                    .where<RealmCountdownNotifications>()
                    .equalTo("id", c.id)
                    .findFirst()
                    ?: realm.createObject(RealmCountdownNotifications::class.java, c.id)
                countdownNotificationMapper.serialize(notificationModel, c)
                return@map notificationModel
            }

        model.notifications = RealmList(*notifications.toTypedArray())
    }

    override fun deleteAll() = realmSync { realm ->
        realm.where<RealmCountdown>().findAll().deleteAllFromRealm()
    }

    override fun deleteDone() = realmSync { realm ->
        realm.where<RealmCountdown>().lessThan("end", now).findAll().deleteAllFromRealm()
    }

    override fun delete(id: String) = realmSync { realm ->
        val model = realm.where<RealmCountdown>().equalTo("id", id).findFirst()
        model?.notifications?.deleteAllFromRealm()
        model?.deleteFromRealm()
    }
}