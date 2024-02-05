package tmg.hourglass.presentation.modify

import app.cash.turbine.test
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.threeten.bp.LocalDateTime
import tmg.hourglass.core.googleanalytics.CrashReporter
import tmg.hourglass.domain.connectors.CountdownConnector
import tmg.hourglass.domain.enums.CountdownInterpolator
import tmg.hourglass.domain.enums.CountdownType
import tmg.hourglass.domain.model.Countdown
import tmg.testutils.BaseTest
import tmg.testutils.livedata.assertEventFired
import tmg.testutils.livedata.test

internal class ModifyViewModelTestEdit: BaseTest() {

    lateinit var underTest: ModifyViewModel

    private val mockCountdownConnector: CountdownConnector = mockk(relaxed = true)
    private val mockCrashReporter: CrashReporter = mockk(relaxed = true)

    private val mockEditId: String = "123"
    private val mockName = "editName"
    private val mockDescription = "editDescription"
    private val mockColour = "#123312"
    private val mockDateStart = LocalDateTime.now()
    private val mockDateEnd = LocalDateTime.now()
    private val mockInitial = "0"
    private val mockFinal = "100"
    private val mockType = CountdownType.DAYS
    private val mockInterpolator = CountdownInterpolator.LINEAR
    private val mockEditableItem: Countdown = Countdown(
        id = mockEditId,
        name = mockName,
        description = mockDescription,
        colour = mockColour,
        start = mockDateStart,
        end = mockDateEnd,
        initial = mockInitial,
        finishing = mockFinal,
        countdownType = mockType,
        interpolator = mockInterpolator,
        notifications = emptyList()
    )

    @BeforeEach
    internal fun setUp() {

        every { mockCountdownConnector.getSync(mockEditId) } returns mockEditableItem
    }

    private fun initUnderTest() {
        underTest = ModifyViewModel(mockCountdownConnector, mockCrashReporter)
        underTest.inputs.initialise(mockEditId)
    }

    @Test
    fun `initialise edit item preloads all the mock values into outputs`() = runTest {

        initUnderTest()
        advanceTimeBy(100)
        advanceUntilIdle()

        underTest.outputs.name.test {
            assertEquals(mockName, awaitItem())
        }
        advanceTimeBy(100)
        underTest.outputs.description.test {
            assertEquals(mockDescription, awaitItem())
        }
        underTest.outputs.color.test {
            assertEquals(mockColour, awaitItem())
        }
        underTest.outputs.startDate.test {
            assertEquals(mockDateStart, awaitItem())
        }
        underTest.outputs.endDate.test {
            assertEquals(mockDateEnd, awaitItem())
        }
        underTest.outputs.initial.test {
            assertEquals(mockInitial, awaitItem())
        }
        underTest.outputs.finished.test {
            assertEquals(mockFinal, awaitItem())
        }
        underTest.outputs.type.test {
            assertEquals(mockType, awaitItem())
        }
        underTest.outputs.interpolator.test {
            assertEquals(mockInterpolator, awaitItem())
        }

        underTest.outputs.isEdit.test {
            assertEquals(true, awaitItem())
        }

        underTest.outputs.saveEnabled.test {
            assertEquals(false, awaitItem())
        }
    }

    @Test
    fun `clicking delete deletes the item in the connector and fires close event`() {

        initUnderTest()

        underTest.inputs.deleteClicked()

        verify {
            mockCountdownConnector.delete(any())
        }
    }
}