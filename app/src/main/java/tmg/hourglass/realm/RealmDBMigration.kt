package tmg.hourglass.realm

import io.realm.DynamicRealm
import io.realm.FieldAttribute
import io.realm.RealmMigration

class RealmDBMigration: RealmMigration {
    override fun migrate(realm: DynamicRealm, oldVersion: Long, newVersion: Long) {

        val schema = realm.schema

        if (oldVersion <= 0L) {
            schema.create("RealmWidgetReference")
                .addField("appWidgetId", Int::class.java, FieldAttribute.PRIMARY_KEY)
                .addField("countdownId", String::class.java, FieldAttribute.REQUIRED)
        }

        if (oldVersion <= 1L) {
            schema.get("RealmCountdown")
                ?.addField("interpolator", String::class.java, FieldAttribute.REQUIRED)
        }

        if (oldVersion <= 2L) {
            // Future migration
        }
    }
}