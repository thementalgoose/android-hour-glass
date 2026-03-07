package tmg.hourglass.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import tmg.hourglass.room.dao.CountdownDao
import tmg.hourglass.room.dao.TagDao
import tmg.hourglass.room.dao.WidgetDao
import tmg.hourglass.room.models.Countdown
import tmg.hourglass.room.models.Tag
import tmg.hourglass.room.models.WidgetReference

internal const val DATABASE_NAME = "HourGlass"

@Database(
    version = 3,
    entities = [
        WidgetReference::class,
        Countdown::class,
        Tag::class
    ],
    exportSchema = true
)
internal abstract class HourGlassDatabase: RoomDatabase() {
    abstract fun widgetDao(): WidgetDao
    abstract fun countdownDao(): CountdownDao
    abstract fun tagDao(): TagDao
}