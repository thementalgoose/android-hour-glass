package tmg.hourglass.realm.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.internal.Keep

@Keep
open class RealmCountdownNotifications(
    @PrimaryKey
    var id: String = "",
    var atTime: String = "",
    var atValue: String = ""
): RealmObject()