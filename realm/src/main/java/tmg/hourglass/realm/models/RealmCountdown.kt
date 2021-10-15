package tmg.hourglass.realm.models

import io.realm.Realm
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class RealmCountdown(
    @PrimaryKey
    var id: String = "",
    var name: String = "",
    var description: String = "",
    var colour: String = "",

    var start: Long = 0L, // Date
    var end: Long = 0L, // Date
    var initial: String = "",
    var finishing: String = "",

    var passageType: String = "",
    var interpolator: String = ""
): RealmObject()