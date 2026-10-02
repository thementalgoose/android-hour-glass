package tmg.hourglass.domain.usecases

import tmg.hourglass.domain.repositories.CountdownRepository
import tmg.hourglass.domain.schedulers.NotificationScheduler
import tmg.hourglass.domain.utils.NotificationTriggerUtils
import javax.inject.Inject

class ScheduleNotificationsUseCase @Inject constructor(
    private val countdownRepository: CountdownRepository,
    private val notificationScheduler: NotificationScheduler
) {
    suspend operator fun invoke(countdownId: String) {
        val countdown = countdownRepository.getSync(countdownId) ?: return
        for (notification in countdown.notifications) {
            val triggerAt = NotificationTriggerUtils.calculateTriggerTime(countdown, notification)
            notificationScheduler.scheduleNotification(countdown, notification, triggerAt)
        }
    }
}
