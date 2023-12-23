package tmg.hourglass.realm.migrations

import android.util.Log
import io.realm.DynamicRealm
import io.realm.FieldAttribute
import io.realm.RealmList
import io.realm.RealmMigration
import io.realm.RealmObjectSchema
import tmg.hourglass.realm.models.RealmCountdownNotifications

class RealmDBMigration: RealmMigration {

    override fun migrate(realm: DynamicRealm, oldVersion: Long, newVersion: Long) {
        Log.i("Realm", "Performing migration $oldVersion -> $newVersion")

        val schema = realm.schema
        for (version in (oldVersion + 1)..newVersion) {
            Log.i("Realm", " Performing migration for $version")
            when (version) {
                1L -> {
                    schema.create("RealmWidgetReference")
                        .addField("appWidgetId", Int::class.java, FieldAttribute.PRIMARY_KEY)
                        .addField("countdownId", String::class.java, FieldAttribute.REQUIRED)
                }
                2L -> {
                    schema.get("RealmCountdown")
                        ?.addField("interpolator", String::class.java, FieldAttribute.REQUIRED)
                }
                4L -> {
                    schema.create("RealmCountdownNotifications")
                        .addField("id", String::class.java, FieldAttribute.REQUIRED, FieldAttribute.PRIMARY_KEY)
                        .addField("atTime", String::class.java, FieldAttribute.REQUIRED)
                        .addField("atValue", String::class.java, FieldAttribute.REQUIRED)
                    schema.get("RealmCountdown")
                        ?.addRealmListField("notifications", schema.get("RealmCountdownNotifications")!!)
                }
                else -> { }
            }
        }

        Log.i("Realm", "Migration complete")
    }
}