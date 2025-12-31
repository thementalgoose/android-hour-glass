package tmg.hourglass.room.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Countdown")
internal data class Countdown(
    @PrimaryKey
    @ColumnInfo("id")
    val id: String,
    @ColumnInfo("name")
    val name: String,
    @ColumnInfo("description")
    val description: String,
    @ColumnInfo("colour")
    val colour: String,
    @ColumnInfo("start_date")
    val start: String,
    @ColumnInfo("end_date")
    val end: String,
    @ColumnInfo("initial")
    val initial: String,
    @ColumnInfo("finishing")
    val finishing: String,
    @ColumnInfo("passage_type")
    val passageType: String,
    @ColumnInfo("is_recurring")
    val isRecurring: Boolean = false,
    @ColumnInfo("interpolator")
    val interpolator: String
)