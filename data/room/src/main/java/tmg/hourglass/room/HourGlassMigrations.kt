package tmg.hourglass.room

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

internal val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("""
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
            )""".trimIndent())
        db.execSQL("""
            INSERT INTO Countdown_New (id, name, description, colour, start_date, end_date, initial, finishing, passage_type, interpolator)
            SELECT id, name, description, colour, start, end, initial, finishing, passage_type, interpolator FROM Countdown
        """.trimIndent())
        db.execSQL("DROP TABLE Countdown")
        db.execSQL("ALTER TABLE Countdown_New RENAME TO Countdown")
    }
}