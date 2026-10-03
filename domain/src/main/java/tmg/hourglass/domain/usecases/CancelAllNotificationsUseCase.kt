package tmg.hourglass.domain.usecases

import kotlinx.coroutines.flow.firstOrNull
import tmg.hourglass.domain.repositories.CountdownRepository
import tmg.hourglass.domain.schedulers.NotificationScheduler
import javax.inject.Inject

class CancelAllNotificationsUseCase @Inject constructor(
    private val countdownRepository: CountdownRepository,
    private val cancelNotificationsUseCase: CancelNotificationsUseCase,
    private val notificationScheduler: NotificationScheduler
) {
    suspend operator fun invoke(force: Boolean = false) {
        if (force) {
            notificationScheduler.cancelAll()
            return
        }
        val countdowns = countdownRepository.all().firstOrNull() ?: return
        for (countdown in countdowns) {
            cancelNotificationsUseCase(countdown.id)
        }
    }
}

typealias CancellingAllNotificationsUseCase = CancelAllNotificationsUseCase
