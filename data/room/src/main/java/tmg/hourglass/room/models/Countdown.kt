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
    @ColumnInfo("start")
    val start: Long,
    @ColumnInfo("end")
    val end: Long,
    @ColumnInfo("initial")
    val initial: String,
    @ColumnInfo("finishing")
    val finishing: String,
    @ColumnInfo("passage_type")
    val passageType: String,
    @ColumnInfo("interpolator")
    val interpolator: String
)