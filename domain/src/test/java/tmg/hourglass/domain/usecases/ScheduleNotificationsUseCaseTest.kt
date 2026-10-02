package tmg.hourglass.domain.usecases

import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import tmg.hourglass.domain.model
import tmg.hourglass.domain.model.Countdown
import tmg.hourglass.domain.model.CountdownNotifications
import tmg.hourglass.domain.repositories.CountdownRepository
import tmg.hourglass.domain.schedulers.NotificationScheduler
import java.time.LocalDateTime

class ScheduleNotificationsUseCaseTest {

    private val countdownRepository: CountdownRepository = mockk(relaxed = true)
    private val notificationScheduler: NotificationScheduler = mockk(relaxed = true)

    private val useCase = ScheduleNotificationsUseCase(
        countdownRepository = countdownRepository,
        notificationScheduler = notificationScheduler
    )

    @Test
    fun `invoke schedules all notifications for existing countdown`() = runTest {
        val notif1 = CountdownNotifications.AtTime(id = "n-1", time = LocalDateTime.of(2026, 11, 1, 12, 0))
        val notif2 = CountdownNotifications.AtValue(id = "n-2", value = "350")
        val countdown = Countdown.Static.model(
            id = "cd-123",
            start = "2026-01-01",
            end = "2026-01-11",
            startValue = "200",
            endValue = "500",
            notifications = listOf(notif1, notif2)
        )

        coEvery { countdownRepository.getSync("cd-123") } returns countdown

        useCase("cd-123")

        verify(exactly = 1) {
            notificationScheduler.scheduleNotification(countdown, notif1, LocalDateTime.of(2026, 11, 1, 12, 0))
        }
        verify(exactly = 1) {
            notificationScheduler.scheduleNotification(countdown, notif2, LocalDateTime.of(2026, 1, 6, 0, 0))
        }
    }

    @Test
    fun `invoke does nothing when countdown is not found`() = runTest {
        coEvery { countdownRepository.getSync("non-existent") } returns null

        useCase("non-existent")

        verify(exactly = 0) {
            notificationScheduler.scheduleNotification(any(), any(), any())
        }
    }
}
