package tmg.hourglass.realm.models

import io.realm.Realm
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.kotlin.where
import tmg.hourglass.data.models.Countdown
import tmg.hourglass.data.models.WidgetReference

open class RealmWidgetReference(
    @PrimaryKey var appWidgetId: Int = -1,
    var countdownId: String = ""
): RealmObject()

fun RealmWidgetReference.convert(): WidgetReference {
    return WidgetReference(
        this.appWidgetId,
        this.countdownId
    )
}

fun WidgetReference.saveSync(realm: Realm) {
    realm.executeTransaction { transRealm ->
        val realmCountdown: RealmWidgetReference = transRealm
            .where<RealmWidgetReference>()
            .equalTo("appWidgetId", this.appWidgetId)
            .findFirst()
            ?: transRealm.createObject(RealmWidgetReference::class.java, this.appWidgetId)
        realmCountdown.applyData(this)
    }
}

fun WidgetReference.save(realm: Realm) {
    realm.executeTransactionAsync { transRealm ->
        val realmCountdown: RealmWidgetReference = transRealm
            .where<RealmWidgetReference>()
            .equalTo("id", this.appWidgetId)
            .findFirst()
            ?: transRealm.createObject(RealmWidgetReference::class.java, this.appWidgetId)
        realmCountdown.applyData(this)
    }
}

private fun RealmWidgetReference.applyData(widgetReference: WidgetReference) {
    this.countdownId = widgetReference.countdownId
}