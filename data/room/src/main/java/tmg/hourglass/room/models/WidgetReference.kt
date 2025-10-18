package tmg.hourglass.room.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "WidgetReference")
internal data class WidgetReference(
    @PrimaryKey
    @ColumnInfo(name = "app_widget_id")
    val appWidgetId: Int,
    @ColumnInfo(name = "countdown_id")
    val countdownId: String,
    @ColumnInfo(name = "open_app_on_click")
    val openAppOnClick: Boolean = false
)