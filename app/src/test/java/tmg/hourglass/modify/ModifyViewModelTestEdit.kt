package tmg.hourglass.modify

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.threeten.bp.LocalDateTime
import tmg.hourglass.crash.CrashReporter
import tmg.hourglass.domain.connectors.CountdownConnector
import tmg.hourglass.domain.enums.CountdownInterpolator
import tmg.hourglass.domain.enums.CountdownType
import tmg.hourglass.domain.model.Countdown
import tmg.testutils.BaseTest
import tmg.testutils.livedata.assertEventFired
import tmg.testutils.livedata.test

internal class ModifyViewModelTestEdit: BaseTest() {

    lateinit var sut: ModifyViewModel

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
        interpolator = mockInterpolator
    )

    @BeforeEach
    internal fun setUp() {

        every { mockCountdownConnector.getSync(mockEditId) } returns mockEditableItem
    }

    private fun initSUT() {
        sut = ModifyViewModel(mockCountdownConnector, mockCrashReporter)
        sut.inputs.initialise(mockEditId)
    }

    @Test
    fun `initialise edit item preloads all the mock values into outputs`() = coroutineTest {

        runBlocking {
            initSUT()
        }
        advanceUntilIdle()

        sut.outputs.isEdit.test { assertValue(true) }

        sut.outputs.name.test {
            assertValue(mockName)
        }
        sut.outputs.description.test {
            assertValue(mockDescription)
        }
        sut.outputs.color.test {
            assertValue(mockColour)
        }
        sut.outputs.startDate.test {
            assertValue(mockDateStart)
        }
        sut.outputs.endDate.test {
            assertValue(mockDateEnd)
        }
        sut.outputs.initial.test {
            assertValue(mockInitial)
        }
        sut.outputs.finished.test {
            assertValue(mockFinal)
        }
        sut.outputs.type.test {
            assertValue(mockType)
        }
        sut.outputs.interpolator.test {
            assertValue(mockInterpolator)
        }

        sut.outputs.saveEnabled.test {
            assertValueWasEmitted(false)
        }
    }

    @Test
    fun `clicking delete deletes the item in the connector and fires close event`() {

        initSUT()

        sut.inputs.deleteClicked()

        verify {
            mockCountdownConnector.delete(any())
        }

        sut.outputs.close.test {
            assertEventFired()
        }
    }
}