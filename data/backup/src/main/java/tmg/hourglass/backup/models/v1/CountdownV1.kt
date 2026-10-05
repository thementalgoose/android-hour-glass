package tmg.hourglass.backup.models.v1

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CountdownV1(
    @SerialName("id")
    val id: String,
    @SerialName("name")
    val name: String,
    @SerialName("description")
    val description: String,
    @SerialName("colour")
    val colour: String,
    @SerialName("start")
    val start: String,
    @SerialName("end")
    val end: String,
    @SerialName("initial")
    val initial: String,
    @SerialName("finishing")
    val finishing: String,
    @SerialName("passageType")
    val passageType: String,
    @SerialName("isRecurring")
    val isRecurring: Boolean = false,
    @SerialName("interpolator")
    val interpolator: String = "LINEAR",
    @SerialName("tagId")
    val tagId: String? = null,
    @SerialName("emoji")
    val emoji: String? = null,
    @SerialName("notifications")
    val notifications: List<NotificationV1> = emptyList()
)
