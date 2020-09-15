package tmg.hourglass.modify

import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.threeten.bp.LocalDateTime
import tmg.hourglass.crash.CrashReporter
import tmg.hourglass.data.CountdownInterpolator
import tmg.hourglass.data.CountdownInterpolator.LINEAR
import tmg.hourglass.data.CountdownType
import tmg.hourglass.data.CountdownType.DAYS
import tmg.hourglass.data.connectors.CountdownConnector
import tmg.hourglass.data.models.Countdown
import tmg.hourglass.testutils.BaseTest
import tmg.hourglass.testutils.assertEventFired
import tmg.hourglass.testutils.assertValue
import tmg.hourglass.utils.Selected

@FlowPreview
@ExperimentalCoroutinesApi
class ModifyViewModelTestEdit: BaseTest() {

    lateinit var sut: ModifyViewModel

    private val mockCountdownConnector: CountdownConnector = mock()
    private val mockCrashReporter: CrashReporter = mock()

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

        whenever(mockCountdownConnector.getSync(mockEditId)).thenReturn(mockEditableItem)
    }

    private fun initSUT() {
        sut = ModifyViewModel(mockCountdownConnector, mockCrashReporter, testScopeProvider)
        sut.inputs.initialise(mockEditId)
    }

    @Test
    fun `ModifyViewModel initialise edit item preloads all the mock values into outputs`() = coroutineTest {

        initSUT()

        assertValue(false, sut.outputs.isAddition)

        assertValue(mockName, sut.outputs.name)
        assertValue(mockDescription, sut.outputs.description)
        assertValue(mockColour, sut.outputs.colour)
        assertValue(mockDates, sut.outputs.dates)
        assertValue(mockInitial, sut.outputs.initial)
        assertValue(mockFinal, sut.outputs.final)
        assertValue(mockType, sut.outputs.type)
        assertValue(mockInterpolator, sut.outputs.interpolator)

        val expectedTypeList = CountdownType
            .values()
            .map {
                Selected(it, it == mockType)
            }
        assertValue(expectedTypeList, sut.outputs.typeList)

        val expectedInterpolatorList = CountdownInterpolator
            .values()
            .map {
                Selected(it, it == mockInterpolator)
            }
        assertValue(expectedInterpolatorList, sut.outputs.interpolatorList)

        assertValue(false, sut.outputs.isValid)
    }

    @Test
    fun `ModifyViewModel clicking delete deletes the item in the connector and fires close event`() {

        initSUT()

        sut.inputs.clickDelete()

        verify(mockCountdownConnector).delete(any())
        assertEventFired(sut.outputs.closeEvent)
    }

    @AfterEach
    internal fun tearDown() {

        reset(mockCountdownConnector, mockCrashReporter)
    }
}