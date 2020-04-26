package tmg.hourglass.realm.models

import io.realm.Realm
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.kotlin.where
import org.threeten.bp.ZoneOffset
import tmg.hourglass.data.CountdownType
import tmg.hourglass.data.models.Countdown
import tmg.hourglass.extensions.toLocalDateTime
import tmg.utilities.extensions.toEnum

private const val dateFormat: String = "yyyy/MM/dd HH:mm"

open class RealmCountdown(
    @PrimaryKey var id: String = "",
    var name: String = "",
    var description: String = "",
    var colour: String = "",

    var start: Long = 0L, // Date
    var end: Long = 0L, // Date
    var initial: String = "",
    var finishing: String = "",

    var passageType: String = ""
): RealmObject()

fun RealmCountdown.convert(): Countdown {
    return Countdown(
        id = this.id,
        name = this.name,
        description = this.description,
        colour = this.colour,
        start = this.start.toLocalDateTime(),
        end = this.end.toLocalDateTime(),
        initial = this.initial,
        finishing = this.finishing,
        countdownType = this.passageType.toEnum<CountdownType> { it.key } ?: CountdownType.NUMBER
    )
}

fun Countdown.saveSync(realm: Realm) {
    realm.executeTransaction { transRealm ->
        val realmCountdown: RealmCountdown = transRealm.where<RealmCountdown>().equalTo("id", this.id).findFirst() ?: transRealm.createObject(RealmCountdown::class.java, this.id)
        realmCountdown.applyData(this)
    }
}

fun Countdown.save(realm: Realm) {
    realm.executeTransactionAsync { transRealm ->
        val realmCountdown: RealmCountdown = transRealm.where<RealmCountdown>().equalTo("id", this.id).findFirst() ?: transRealm.createObject(RealmCountdown::class.java, this.id)
        realmCountdown.applyData(this)
    }
}

private fun RealmCountdown.applyData(countdown: Countdown) {
    this.name = countdown.name
    this.description = countdown.description
    this.colour = countdown.colour
    this.start = countdown.start.toInstant(ZoneOffset.UTC).toEpochMilli()
    this.end = countdown.end.toInstant(ZoneOffset.UTC).toEpochMilli()
    this.initial = countdown.initial
    this.finishing = countdown.finishing
    this.passageType = countdown.countdownType.key
}