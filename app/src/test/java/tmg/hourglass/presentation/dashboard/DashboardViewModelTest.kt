package tmg.hourglass.presentation.dashboard

import app.cash.turbine.test
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.hourglass.domain.connectors.CountdownConnector
import tmg.hourglass.domain.model.Countdown
import tmg.testutils.BaseTest

internal class DashboardViewModelTest: BaseTest() {

    private lateinit var underTest: DashboardViewModel

    private val mockCountdownConnector: CountdownConnector = mockk(relaxed = true)

    private val fakeCountdownExpired: Countdown = mockk(relaxed = true) {
        every { id } returns "expired"
    }
    private val fakeCountdownUpcoming: Countdown = mockk(relaxed = true) {
        every { id } returns "upcoming"
    }

    private fun initUnderTest() {
        underTest = DashboardViewModel(
            countdownConnector = mockCountdownConnector,
            ioDispatcher = Dispatchers.Unconfined
        )
    }

    @BeforeEach
    fun setUp() {
        every { mockCountdownConnector.allDone() } returns flow { emit(listOf(fakeCountdownExpired)) }
        every { mockCountdownConnector.allCurrent() } returns flow { emit(listOf(fakeCountdownUpcoming)) }
    }

    @Test
    fun `init sets state to new upcoming`() = runTest {
        initUnderTest()
        underTest.uiState.test {
            val item = awaitItem()
            assertEquals(listOf(fakeCountdownUpcoming), item.upcoming)
            assertEquals(listOf(fakeCountdownExpired), item.expired)
            assertEquals(null, item.action)
        }
    }

    @Test
    fun `refresh updates state`() = runTest {
        initUnderTest()
        underTest.uiState.test {
            val item1 = awaitItem()
            assertEquals(listOf(fakeCountdownUpcoming), item1.upcoming)
            assertEquals(listOf(fakeCountdownExpired), item1.expired)

            every { mockCountdownConnector.allDone() } returns flow { emit(emptyList()) }
            every { mockCountdownConnector.allCurrent() } returns flow { emit(emptyList()) }

            underTest.refresh()

            val item2 = awaitItem()
            assertEquals(emptyList<Countdown>(), item2.upcoming)
            assertEquals(emptyList<Countdown>(), item2.expired)
        }
    }

    @Test
    fun `edit updates state to modify`() = runTest {
        initUnderTest()
        underTest.uiState.test {
            val item1 = awaitItem()
            assertEquals(null, item1.action)

            underTest.edit(fakeCountdownUpcoming)

            val item2 = awaitItem()
            assertTrue(item2.action is DashboardAction.Modify)
        }
    }


    @Test
    fun `deletes the item`() = runTest {
        initUnderTest()
        underTest.delete(fakeCountdownUpcoming)
        verify {
            mockCountdownConnector.delete("upcoming")
        }
    }

    @Test
    fun `create new updates action`() = runTest {
        initUnderTest()
        underTest.uiState.test {
            val item1 = awaitItem()
            assertEquals(null, item1.action)

            underTest.createNew()
            val item2 = awaitItem()
            assertEquals(DashboardAction.Add, item2.action)
        }
    }

    @Test
    fun `close action sets action to null`() = runTest {
        initUnderTest()
        underTest.uiState.test {
            val item1 = awaitItem()
            assertEquals(null, item1.action)

            underTest.createNew()
            val item2 = awaitItem()
            assertEquals(DashboardAction.Add, item2.action)

            underTest.closeAction()
            val item3 = awaitItem()
            assertEquals(null, item3.action)
        }
    }
}