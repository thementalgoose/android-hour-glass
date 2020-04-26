package tmg.passage.realm.models

import io.realm.Realm
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import org.threeten.bp.ZoneOffset
import org.threeten.bp.format.DateTimeFormatter
import tmg.passage.data.PassageType
import tmg.passage.data.models.Passage
import tmg.passage.extensions.toLocalDateTime
import tmg.utilities.extensions.toEnum

private const val dateFormat: String = "yyyy/MM/dd HH:mm"

open class RealmPassage(
    @PrimaryKey var id: String = "",
    var name: String = "",
    var description: String = "",
    var colour: String = "",

    var start: Long = 0L, // Date
    var end: Long = 0L, // Date
    var initial: String = "",
    var final: String = "",

    var passageType: String = ""
): RealmObject()

fun RealmPassage.convert(): Passage {
    return Passage(
        id = this.id,
        name = this.name,
        description = this.description,
        colour = this.colour,
        start = this.start.toLocalDateTime(),
        end = this.end.toLocalDateTime(),
        initial = this.initial,
        final = this.final,
        passageType = this.passageType.toEnum<PassageType> { it.key } ?: PassageType.NUMBER
    )
}

fun Passage.saveSync(realm: Realm) {
    realm.executeTransaction { transRealm ->
        val realmPassage = transRealm.createObject(RealmPassage::class.java, this.id)
        realmPassage.applyData(this)
    }
}

fun Passage.save(realm: Realm) {
    realm.executeTransactionAsync { transRealm ->
        val realmPassage = transRealm.createObject(RealmPassage::class.java, this.id)
        realmPassage.applyData(this)
    }
}

private fun RealmPassage.applyData(passage: Passage) {
    this.name = passage.name
    this.description = passage.description
    this.colour = passage.colour
    this.start = passage.start.toInstant(ZoneOffset.UTC).toEpochMilli()
    this.end = passage.start.toInstant(ZoneOffset.UTC).toEpochMilli()
    this.initial = passage.initial
    this.final = passage.final
    this.passageType = passage.passageType.key
}