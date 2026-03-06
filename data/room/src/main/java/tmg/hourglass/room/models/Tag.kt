package tmg.hourglass.room.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import tmg.hourglass.domain.model.TagOrdering

@Entity(tableName = "Tag")
data class Tag(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "colour")
    val colour: String,
    @ColumnInfo(name = "sort")
    val sort: String,
)

val TagOrdering.sortKey: String
    get() = when (this) {
        TagOrdering.ALPHABETICAL -> "alphabetical"
        TagOrdering.FINISHING_SOONEST -> "soonest"
        TagOrdering.FINISHING_LATEST -> "latest"
        TagOrdering.PROGRESS -> "progress"
    }