package tmg.hourglass.backup.models.v1

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BackupSchemaV1(
    @SerialName("schemaVersion")
    val schemaVersion: String = "v1",
    @SerialName("tags")
    val tags: List<TagV1> = emptyList(),
    @SerialName("countdowns")
    val countdowns: List<CountdownV1> = emptyList()
)
