package tmg.hourglass.realm.models

import androidx.annotation.Keep
import io.realm.Realm
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

@Deprecated("Do not update this model as it has been migrated to Room")
@Keep
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
    var interpolator: String = "",
    var notifications: RealmList<RealmCountdownNotifications> = RealmList()
): RealmObject()