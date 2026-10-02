package tmg.hourglass.domain.usecases

import tmg.hourglass.domain.repositories.CountdownRepository
import tmg.hourglass.domain.schedulers.NotificationScheduler
import javax.inject.Inject

class CancelNotificationsUseCase @Inject constructor(
    private val countdownRepository: CountdownRepository,
    private val notificationScheduler: NotificationScheduler
) {
    suspend operator fun invoke(countdownId: String) {
        val countdown = countdownRepository.getSync(countdownId) ?: return
        for (notification in countdown.notifications) {
            notificationScheduler.cancelNotification(countdown.id, notification.id)
        }
    }
}

typealias CancellingNotificationsUseCase = CancelNotificationsUseCase
