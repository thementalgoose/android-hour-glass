package tmg.hourglass.domain.utils

import tmg.hourglass.domain.model.Countdown
import tmg.hourglass.domain.model.CountdownNotifications
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

object NotificationTriggerUtils {

    fun sortChronologically(
        countdown: Countdown,
        notifications: List<CountdownNotifications>
    ): List<CountdownNotifications> {
        return notifications.sortedBy { calculateTriggerTime(countdown, it) }
    }

    fun calculateTriggerTime(
        countdown: Countdown,
        notification: CountdownNotifications
    ): LocalDateTime {
        return when (notification) {
            is CountdownNotifications.AtTime -> notification.time
            is CountdownNotifications.AtValue -> calculateValueTriggerTime(countdown, notification.value)
        }
    }

    fun calculateValueTriggerTime(
        countdown: Countdown,
        targetValueStr: String
    ): LocalDateTime {
        val startVal = countdown.startValue.toDoubleOrNull() ?: 0.0
        val endVal = countdown.endValue.toDoubleOrNull() ?: 100.0
        val targetVal = targetValueStr.toDoubleOrNull() ?: startVal

        val diff = endVal - startVal
        val progress = if (diff == 0.0) 0.0 else ((targetVal - startVal) / diff).coerceIn(0.0, 1.0)

        val startMillis = countdown.startDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val endMillis = countdown.endDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val totalDuration = endMillis - startMillis

        val targetMillis = startMillis + (progress * totalDuration).toLong()
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(targetMillis), ZoneId.systemDefault())
    }
}
