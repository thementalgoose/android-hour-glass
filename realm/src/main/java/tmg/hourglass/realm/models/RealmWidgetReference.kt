package tmg.hourglass.realm.models

import androidx.annotation.Keep
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

@Keep
open class RealmWidgetReference(
    @PrimaryKey
    var appWidgetId: Int = -1,
    var countdownId: String = "",
    var openAppOnClick: Boolean = false
): RealmObject()