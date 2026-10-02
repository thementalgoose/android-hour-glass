package tmg.hourglass.room.mappers

import tmg.hourglass.domain.model.Countdown
import tmg.hourglass.domain.model.CountdownNotifications
import tmg.hourglass.room.models.Notification
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

internal class NotificationMapper @Inject constructor() {

    fun serialize(countdownId: String, model: CountdownNotifications): Notification {
        return when (model) {
            is CountdownNotifications.AtTime -> Notification(
                id = model.id,
                countdownId = countdownId,
                type = Notification.TYPE_TIME,
                time = model.time.toLocalDate().format(Countdown.YYYY_MM_DD_FORMAT),
                value = null
            )
            is CountdownNotifications.AtValue -> Notification(
                id = model.id,
                countdownId = countdownId,
                type = Notification.TYPE_VALUE,
                time = null,
                value = model.value
            )
        }
    }

    fun deserialize(entity: Notification): CountdownNotifications {
        return when (entity.type) {
            Notification.TYPE_TIME -> CountdownNotifications.AtTime(
                id = entity.id,
                time = entity.time?.toLocalDateTime() ?: LocalDateTime.now()
            )
            Notification.TYPE_VALUE -> CountdownNotifications.AtValue(
                id = entity.id,
                value = entity.value.orEmpty()
            )
            else -> CountdownNotifications.AtValue(
                id = entity.id,
                value = entity.value.orEmpty()
            )
        }
    }

    private fun String.toLocalDateTime(): LocalDateTime {
        return try {
            LocalDate.parse(this, Countdown.YYYY_MM_DD_FORMAT).atStartOfDay()
        } catch (e: Exception) {
            try {
                LocalDateTime.parse(this)
            } catch (e2: Exception) {
                LocalDateTime.now()
            }
        }
    }
}
