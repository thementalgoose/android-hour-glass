package tmg.hourglass.modify

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.threeten.bp.LocalDateTime
import tmg.hourglass.crash.CrashReporter
import tmg.hourglass.domain.enums.CountdownInterpolator
import tmg.hourglass.domain.enums.CountdownInterpolator.LINEAR
import tmg.hourglass.domain.enums.CountdownType
import tmg.hourglass.domain.enums.CountdownType.DAYS
import tmg.hourglass.domain.connectors.CountdownConnector
import tmg.hourglass.domain.model.Countdown
import tmg.hourglass.testutils.BaseTest
import tmg.hourglass.testutils.assertDataEventValue
import tmg.hourglass.testutils.assertEventFired
import tmg.hourglass.testutils.test
import tmg.hourglass.utils.Selected

internal class ModifyViewModelTestEdit: BaseTest() {

    lateinit var sut: ModifyViewModel

    private val mockCountdownConnector: CountdownConnector = mockk(relaxed = true)
    private val mockCrashReporter: CrashReporter = mockk(relaxed = true)

    private val mockEditId: String = "123"
    private val mockName = "editName"
    private val mockDescription = "editDescription"
    private val mockColour = "#123312"
    private val mockDates = Pair(LocalDateTime.now(), LocalDateTime.now())
    private val mockInitial = "0"
    private val mockFinal = "100"
    private val mockType = DAYS
    private val mockInterpolator = LINEAR
    private val mockEditableItem: Countdown = Countdown(
        id = mockEditId,
        name = mockName,
        description = mockDescription,
        colour = mockColour,
        start = mockDates.first,
        end = mockDates.second,
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

        initSUT()

        sut.outputs.isAddition.test { assertValue(false) }

        sut.outputs.name.test {
            assertValue(mockName)
        }
        sut.outputs.description.test {
            assertValue(mockDescription)
        }
        sut.outputs.colour.test {
            assertValue(mockColour)
        }
        sut.outputs.dates.test {
            assertValue(mockDates)
        }
        sut.outputs.initial.test {
            assertValue(mockInitial)
        }
        sut.outputs.final.test {
            assertValue(mockFinal)
        }
        sut.outputs.type.test {
            assertValue(mockType)
        }
        sut.outputs.interpolator.test {
            assertValue(mockInterpolator)
        }

        sut.outputs.typeList.test {
            val expectedTypeList = CountdownType
                .values()
                .map {
                    Selected(it, it == mockType)
                }
            assertValue(expectedTypeList)
        }

        sut.outputs.interpolatorList.test {
            val expectedInterpolatorList = CountdownInterpolator
                .values()
                .map {
                    Selected(it, it == mockInterpolator)
                }
            assertValue(expectedInterpolatorList)
        }

        sut.outputs.isValid.test {
            assertValue(false)
        }
    }

    @Test
    fun `open date picker launches date picker event with stored value`() {

        initSUT()

        sut.inputs.clickDatePicker()

        sut.outputs.showDatePicker.test {
            assertDataEventValue(mockDates)
        }
    }

    @Test
    fun `clicking delete deletes the item in the connector and fires close event`() {

        initSUT()

        sut.inputs.clickDelete()

        verify {
            mockCountdownConnector.delete(any())
        }

        sut.outputs.closeEvent.test {
            assertEventFired()
        }
    }
}