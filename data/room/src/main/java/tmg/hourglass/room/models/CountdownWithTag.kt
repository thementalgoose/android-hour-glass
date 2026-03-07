package tmg.hourglass.room.models

import androidx.room.Embedded
import androidx.room.Relation

internal class CountdownWithTag(
    @Embedded
    val countdown: Countdown,
    @Relation(
        parentColumn = "tag_id",
        entityColumn = "id"
    )
    val tag: Tag?,
)