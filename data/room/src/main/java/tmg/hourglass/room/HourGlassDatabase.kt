package tmg.hourglass.room

import androidx.room.Database
import androidx.room.RoomDatabase
import tmg.hourglass.room.dao.CountdownDao
import tmg.hourglass.room.dao.WidgetDao
import tmg.hourglass.room.models.Countdown
import tmg.hourglass.room.models.WidgetReference

internal const val DATABASE_NAME = "HourGlass"

@Database(
    version = 1,
    entities = [
        WidgetReference::class,
        Countdown::class
    ],
    exportSchema = true
)
internal abstract class HourGlassDatabase: RoomDatabase() {
    abstract fun widgetDao(): WidgetDao
    abstract fun countdownDao(): CountdownDao
}