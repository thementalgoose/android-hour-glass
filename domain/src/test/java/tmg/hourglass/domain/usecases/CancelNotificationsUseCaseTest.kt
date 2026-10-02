package tmg.hourglass.domain.usecases

import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import tmg.hourglass.domain.model
import tmg.hourglass.domain.model.Countdown
import tmg.hourglass.domain.model.CountdownNotifications
import tmg.hourglass.domain.repositories.CountdownRepository
import tmg.hourglass.domain.schedulers.NotificationScheduler
import java.time.LocalDateTime

class CancelNotificationsUseCaseTest {

    private val countdownRepository: CountdownRepository = mockk(relaxed = true)
    private val notificationScheduler: NotificationScheduler = mockk(relaxed = true)

    private val useCase = CancelNotificationsUseCase(
        countdownRepository = countdownRepository,
        notificationScheduler = notificationScheduler
    )

    @Test
    fun `invoke cancels all notifications for existing countdown`() = runTest {
        val notif1 = CountdownNotifications.AtTime(id = "n-1", time = LocalDateTime.of(2026, 11, 1, 12, 0))
        val notif2 = CountdownNotifications.AtValue(id = "n-2", value = "350")
        val countdown = Countdown.Static.model(
            id = "cd-123",
            notifications = listOf(notif1, notif2)
        )

        coEvery { countdownRepository.getSync("cd-123") } returns countdown

        useCase("cd-123")

        verify(exactly = 1) {
            notificationScheduler.cancelNotification("cd-123", "n-1")
        }
        verify(exactly = 1) {
            notificationScheduler.cancelNotification("cd-123", "n-2")
        }
    }

    @Test
    fun `invoke does nothing when countdown is not found`() = runTest {
        coEvery { countdownRepository.getSync("non-existent") } returns null

        useCase("non-existent")

        verify(exactly = 0) {
            notificationScheduler.cancelNotification(any(), any())
        }
    }
}
