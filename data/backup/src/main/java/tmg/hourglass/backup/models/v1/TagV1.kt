package tmg.hourglass.backup.models.v1

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TagV1(
    @SerialName("id")
    val id: String,
    @SerialName("name")
    val name: String,
    @SerialName("colour")
    val colour: String,
    @SerialName("sort")
    val sort: String,
    @SerialName("expanded")
    val expanded: Boolean
)
