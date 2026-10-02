package tmg.hourglass.room.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "Notification",
    foreignKeys = [
        ForeignKey(
            entity = Countdown::class,
            parentColumns = ["id"],
            childColumns = ["countdown_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("countdown_id")
    ]
)
internal data class Notification(
    @PrimaryKey
    @ColumnInfo("id")
    val id: String,
    @ColumnInfo("countdown_id")
    val countdownId: String,
    @ColumnInfo("type")
    val type: String,
    @ColumnInfo("time")
    val time: String?,
    @ColumnInfo("value")
    val value: String?
) {
    companion object {
        const val TYPE_TIME = "TIME"
        const val TYPE_VALUE = "VALUE"
    }
}
