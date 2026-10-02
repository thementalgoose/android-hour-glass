package tmg.hourglass.domain.usecases

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import tmg.hourglass.domain.model
import tmg.hourglass.domain.model.Countdown
import tmg.hourglass.domain.repositories.CountdownRepository

class ScheduleAllNotificationsUseCaseTest {

    private val countdownRepository: CountdownRepository = mockk(relaxed = true)
    private val scheduleNotificationsUseCase: ScheduleNotificationsUseCase = mockk(relaxed = true)

    private val useCase = ScheduleAllNotificationsUseCase(
        countdownRepository = countdownRepository,
        scheduleNotificationsUseCase = scheduleNotificationsUseCase
    )

    @Test
    fun `invoke schedules notifications for all current countdowns`() = runTest {
        val cd1 = Countdown.Static.model(id = "cd-1")
        val cd2 = Countdown.Static.model(id = "cd-2")

        coEvery { countdownRepository.allCurrent() } returns flowOf(listOf(cd1, cd2))

        useCase()

        coVerify(exactly = 1) { scheduleNotificationsUseCase("cd-1") }
        coVerify(exactly = 1) { scheduleNotificationsUseCase("cd-2") }
    }

    @Test
    fun `invoke does nothing when no current countdowns exist`() = runTest {
        coEvery { countdownRepository.allCurrent() } returns flowOf(emptyList())

        useCase()

        coVerify(exactly = 0) { scheduleNotificationsUseCase(any()) }
    }
}
