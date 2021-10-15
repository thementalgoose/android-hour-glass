package tmg.hourglass.realm.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class RealmWidgetReference(
    @PrimaryKey
    var appWidgetId: Int = -1,
    var countdownId: String = ""
): RealmObject()