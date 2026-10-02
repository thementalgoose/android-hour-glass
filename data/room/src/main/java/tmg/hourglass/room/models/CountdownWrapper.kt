package tmg.hourglass.room.models

import androidx.room.Embedded
import androidx.room.Relation

internal class CountdownWrapper(
    @Embedded
    val countdown: Countdown,
    @Relation(
        parentColumn = "tag_id",
        entityColumn = "id"
    )
    val tag: Tag?,
    @Relation(
        parentColumn = "id",
        entityColumn = "countdown_id"
    )
    val notifications: List<Notification> = emptyList()
)