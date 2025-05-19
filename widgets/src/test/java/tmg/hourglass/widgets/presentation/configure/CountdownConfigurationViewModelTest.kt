package tmg.hourglass.widgets.presentation.configure

import app.cash.turbine.test
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.hourglass.domain.model
import tmg.hourglass.domain.model.Countdown
import tmg.hourglass.domain.model.WidgetReference
import tmg.hourglass.domain.repositories.CountdownRepository
import tmg.hourglass.domain.repositories.WidgetRepository
import tmg.testutils.BaseTest

internal class CountdownConfigurationViewModelTest: BaseTest() {

    private val mockCountdownRepository: CountdownRepository = mockk(relaxed = true)
    private val mockWidgetRepository: WidgetRepository = mockk(relaxed = true)

    private lateinit var underTest: CountdownConfigurationViewModel

    private fun initUnderTest() {
        underTest = CountdownConfigurationViewModel(
            countdownRepository = mockCountdownRepository,
            widgetRepository = mockWidgetRepository
        )
    }

    @Test
    fun `view model initialise loads all widgets`() = runTest {
        val models = listOf<Countdown>(Countdown.model())
        every { mockCountdownRepository.all() } returns flow { emit(models) }
        initUnderTest()

        underTest.uiState.test {
            assertEquals(models, awaitItem().items)
        }
    }

    @Test
    fun `loading app widget updates state with selection`() = runTest {
        val countdown1 = Countdown.model(id = "countdownId1")
        val countdown2 = Countdown.model(id = "countdownId2")
        val models = listOf<Countdown>(countdown1, countdown2)
        every { mockCountdownRepository.all() } returns flow { emit(models) }
        every { mockCountdownRepository.getSync("countdownId1") } returns countdown1
        every { mockCountdownRepository.getSync("countdownId2") } returns countdown2

        val appWidgetId = 1
        every { mockWidgetRepository.getSync(appWidgetId) } returns WidgetReference.model(
            appWidgetId = appWidgetId,
            countdownId = "countdownId2"
        )

        initUnderTest()
        underTest.uiState.test {
            assertEquals(models, awaitItem().items)

            underTest.load(appWidgetId)

            val model = awaitItem()
            assertEquals(appWidgetId, model.appWidgetId)
            assertEquals(countdown2, model.selected)
        }
    }

    @Test
    fun `loading app widget updates state with not found selection`() = runTest {
        val countdown1 = Countdown.model(id = "countdownId1")
        val countdown2 = Countdown.model(id = "countdownId2")
        val models = listOf<Countdown>(countdown1, countdown2)
        every { mockCountdownRepository.all() } returns flow { emit(models) }
        every { mockCountdownRepository.getSync("countdownId3") } returns null

        val appWidgetId = 1
        every { mockWidgetRepository.getSync(appWidgetId) } returns WidgetReference.model(
            appWidgetId = appWidgetId,
            countdownId = "countdownId3"
        )

        initUnderTest()
        underTest.uiState.test {
            assertEquals(models, awaitItem().items)

            underTest.load(appWidgetId)

            val model = awaitItem()
            assertEquals(appWidgetId, model.appWidgetId)
            assertEquals(null, model.selected)
        }
    }

    @Test
    fun `selecting item updates selection`() = runTest {
        val countdown1 = Countdown.model(id = "countdownId1")
        val countdown2 = Countdown.model(id = "countdownId2")
        val models = listOf<Countdown>(countdown1, countdown2)
        every { mockCountdownRepository.all() } returns flow { emit(models) }

        initUnderTest()
        underTest.uiState.test {
            assertEquals(models, awaitItem().items)

            underTest.select(countdown2)

            val model = awaitItem()
            assertEquals(countdown2, model.selected)
        }
    }

    @Test
    fun `saving item saves widget ref`() = runTest {
        val countdown1 = Countdown.model(id = "countdownId1")
        val countdown2 = Countdown.model(id = "countdownId2")
        val models = listOf<Countdown>(countdown1, countdown2)
        every { mockCountdownRepository.all() } returns flow { emit(models) }
        every { mockCountdownRepository.getSync("countdownId1") } returns countdown1
        every { mockCountdownRepository.getSync("countdownId2") } returns countdown2

        val appWidgetId = 1
        every { mockWidgetRepository.getSync(appWidgetId) } returns WidgetReference.model(
            appWidgetId = appWidgetId,
            countdownId = "countdownId2",
            openAppOnClick = false
        )

        initUnderTest()
        underTest.uiState.test {
            assertEquals(models, awaitItem().items)

            underTest.load(appWidgetId)

            val loaded = awaitItem()
            assertEquals(countdown2, loaded.selected)

            underTest.openAppOnClick(true)

            val updated = awaitItem()
            assertEquals(true, updated.openAppOnClick)

            underTest.save()

            val expectedWidgetRef = WidgetReference.model(
                appWidgetId = appWidgetId,
                countdownId = "countdownId2",
                openAppOnClick = true
            )
            verify {
                mockWidgetRepository.saveSync(expectedWidgetRef)
            }
        }
    }
}