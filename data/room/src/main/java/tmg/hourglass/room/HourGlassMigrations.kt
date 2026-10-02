package tmg.hourglass.room

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


enum class Migrations(
    val migration: Migration
) {
    MIGRATION_1_2(object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                """
                CREATE TABLE Countdown_New(
                  id TEXT PRIMARY KEY NOT NULL,
                  name TEXT NOT NULL,
                  description TEXT NOT NULL,
                  colour TEXT NOT NULL,
                  start_date TEXT NOT NULL,
                  end_date TEXT NOT NULL,
                  initial TEXT NOT NULL,
                  finishing TEXT NOT NULL,
                  is_recurring INTEGER NOT NULL DEFAULT 0,
                  passage_type TEXT NOT NULL,
                  interpolator TEXT NOT NULL
                )""".trimIndent()
            )
            db.execSQL(
                """
                INSERT INTO Countdown_New (id, name, description, colour, start_date, end_date, initial, finishing, passage_type, interpolator)
                SELECT id, name, description, colour, start, end, initial, finishing, passage_type, interpolator FROM Countdown
            """.trimIndent()
            )
            db.execSQL("DROP TABLE Countdown")
            db.execSQL("ALTER TABLE Countdown_New RENAME TO Countdown")
        }
    }),
    MIGRATION_2_3(object : Migration(2, 3) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE Countdown ADD COLUMN tag_id TEXT DEFAULT NULL")
            db.execSQL("""
                CREATE TABLE Tag(
                  id TEXT PRIMARY KEY NOT NULL,
                  name TEXT NOT NULL,
                  colour TEXT NOT NULL,
                  sort TEXT NOT NULL
                )
            """.trimIndent())
        }
    }),
    MIGRATION_3_4(object : Migration(3, 4) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE Tag ADD COLUMN expanded INTEGER NOT NULL DEFAULT 1")
        }
    }),
    MIGRATION_4_5(object : Migration(4, 5) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("""
                CREATE TABLE IF NOT EXISTS `Notification` (
                  `id` TEXT NOT NULL,
                  `countdown_id` TEXT NOT NULL,
                  `type` TEXT NOT NULL,
                  `time` TEXT,
                  `value` TEXT,
                  PRIMARY KEY(`id`),
                  FOREIGN KEY(`countdown_id`) REFERENCES `Countdown`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE
                )
            """.trimIndent())
            db.execSQL("CREATE INDEX IF NOT EXISTS `index_Notification_countdown_id` ON `Notification` (`countdown_id`)")
        }
    });
}