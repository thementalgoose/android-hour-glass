package tmg.hourglass.backup.models.v1

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NotificationV1(
    @SerialName("id")
    val id: String,
    @SerialName("type")
    val type: String,
    @SerialName("time")
    val time: String? = null,
    @SerialName("value")
    val value: String? = null
)
