package tmg.hourglass.domain.usecases

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import tmg.hourglass.domain.model
import tmg.hourglass.domain.model.Countdown
import tmg.hourglass.domain.repositories.CountdownRepository
import tmg.hourglass.domain.schedulers.NotificationScheduler

class CancelAllNotificationsUseCaseTest {

    private val countdownRepository: CountdownRepository = mockk(relaxed = true)
    private val cancelNotificationsUseCase: CancelNotificationsUseCase = mockk(relaxed = true)
    private val notificationScheduler: NotificationScheduler = mockk(relaxed = true)

    private val useCase = CancelAllNotificationsUseCase(
        countdownRepository = countdownRepository,
        cancelNotificationsUseCase = cancelNotificationsUseCase,
        notificationScheduler = notificationScheduler
    )

    @Test
    fun `invoke cancels notifications for all countdowns when force is false`() = runTest {
        val cd1 = Countdown.Static.model(id = "cd-1")
        val cd2 = Countdown.Static.model(id = "cd-2")

        coEvery { countdownRepository.all() } returns flowOf(listOf(cd1, cd2))

        useCase(force = false)

        coVerify(exactly = 1) { cancelNotificationsUseCase("cd-1") }
        coVerify(exactly = 1) { cancelNotificationsUseCase("cd-2") }
        verify(exactly = 0) { notificationScheduler.cancelAll() }
    }

    @Test
    fun `invoke calls notificationScheduler cancelAll when force is true`() = runTest {
        useCase(force = true)

        verify(exactly = 1) { notificationScheduler.cancelAll() }
        coVerify(exactly = 0) { countdownRepository.all() }
        coVerify(exactly = 0) { cancelNotificationsUseCase(any()) }
    }

    @Test
    fun `invoke does nothing when no countdowns exist`() = runTest {
        coEvery { countdownRepository.all() } returns flowOf(emptyList())

        useCase()

        coVerify(exactly = 0) { cancelNotificationsUseCase(any()) }
        verify(exactly = 0) { notificationScheduler.cancelAll() }
    }
}
