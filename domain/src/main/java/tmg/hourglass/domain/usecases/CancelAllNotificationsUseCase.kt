package tmg.hourglass.domain.usecases

import kotlinx.coroutines.flow.firstOrNull
import tmg.hourglass.domain.repositories.CountdownRepository
import javax.inject.Inject

class CancelAllNotificationsUseCase @Inject constructor(
    private val countdownRepository: CountdownRepository,
    private val cancelNotificationsUseCase: CancelNotificationsUseCase
) {
    suspend operator fun invoke() {
        val countdowns = countdownRepository.all().firstOrNull() ?: return
        for (countdown in countdowns) {
            cancelNotificationsUseCase(countdown.id)
        }
    }
}

typealias CancellingAllNotificationsUseCase = CancelAllNotificationsUseCase
