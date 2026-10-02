package tmg.hourglass.domain.usecases

import kotlinx.coroutines.flow.firstOrNull
import tmg.hourglass.domain.repositories.CountdownRepository
import javax.inject.Inject

class ScheduleAllNotificationsUseCase @Inject constructor(
    private val countdownRepository: CountdownRepository,
    private val scheduleNotificationsUseCase: ScheduleNotificationsUseCase
) {
    suspend operator fun invoke() {
        val countdowns = countdownRepository.allCurrent().firstOrNull() ?: return
        for (countdown in countdowns) {
            scheduleNotificationsUseCase(countdown.id)
        }
    }
}
