package tmg.hourglass.domain.schedulers

import tmg.hourglass.domain.model.Countdown
import tmg.hourglass.domain.model.CountdownNotifications
import java.time.LocalDateTime

interface NotificationScheduler {
    fun scheduleNotification(
        countdown: Countdown,
        notification: CountdownNotifications,
        triggerAt: LocalDateTime
    )

    fun cancelNotification(
        countdownId: String,
        notificationId: String
    )

    fun cancelAll()
}
