package tmg.hourglass.modify

import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import tmg.hourglass.crash.CrashReporter
import tmg.hourglass.data.CountdownInterpolator
import tmg.hourglass.data.CountdownInterpolator.ACCELERATE_DECELERATE
import tmg.hourglass.data.CountdownInterpolator.LINEAR
import tmg.hourglass.data.CountdownType
import tmg.hourglass.data.CountdownType.DAYS
import tmg.hourglass.data.CountdownType.GRAMS
import tmg.hourglass.data.connectors.CountdownConnector
import tmg.hourglass.data.models.Countdown
import tmg.hourglass.testutils.BaseTest
import tmg.hourglass.testutils.assertEventFired
import tmg.hourglass.testutils.assertValue
import tmg.hourglass.utils.Selected
import kotlin.NullPointerException

@FlowPreview
@ExperimentalCoroutinesApi
class ModifyViewModelTest: BaseTest() {

    lateinit var sut: ModifyViewModel

    private val mockCountdownConnector: CountdownConnector = mock()
    private val mockCrashReporter: CrashReporter = mock()

    @BeforeEach
    internal fun setUp() {

    }

    private fun initSUT() {
        sut = ModifyViewModel(mockCountdownConnector, mockCrashReporter, testScopeProvider)
        sut.inputs.initialise(null)
    }

    @Test
    fun `ModifyViewModel initialising vm sets is addition to true`() {

        initSUT()

        assertValue(true, sut.outputs.isAddition)
    }

    @Test
    fun `ModifyViewModel input name registers name update`() {

        val expectedInput= "test"

        initSUT()

        sut.inputs.inputName(expectedInput)
        assertValue(expectedInput, sut.outputs.name)
    }

    @Test
    fun `ModifyViewModel input description registers description update`() {

        val expectedInput= "desc"

        initSUT()

        sut.inputs.inputDescription(expectedInput)
        assertValue(expectedInput, sut.outputs.description)
    }

    @Test
    fun `ModifyViewModel input colours registers colours update`() {

        val expectedInput= "#123123"

        initSUT()

        sut.inputs.inputColour(expectedInput)
        assertValue(expectedInput, sut.outputs.colour)
    }

    @Test
    fun `ModifyViewModel input type registers type update`() {

        val expectedInput = GRAMS
        val expectedTypeList = CountdownType
            .values()
            .map {
                Selected(it, it == GRAMS)
            }

        initSUT()

        sut.inputs.inputType(expectedInput)
        assertValue(expectedInput, sut.outputs.type)
        assertValue(Pair(true, second = true), sut.outputs.showRange)
        assertValue(expectedTypeList, sut.outputs.typeList)

    }

    @Test
    fun `ModifyViewModel input type as days registers type update and shows range event`() {

        val expectedInput = DAYS
        val expectedTypeList = CountdownType
            .values()
            .map {
                Selected(it, it == DAYS)
            }

        initSUT()

        sut.inputs.inputType(expectedInput)
        assertValue(expectedInput, sut.outputs.type)
        assertValue(Pair(false, second = false), sut.outputs.showRange)
        assertValue(expectedTypeList, sut.outputs.typeList)
    }

    @Test
    fun `ModifyViewModel input dates registers dates event`() {

        val expectedStart = LocalDateTime.now()
        val expectedEnd = LocalDateTime.now()

        initSUT()

        sut.inputs.inputDates(expectedStart, expectedEnd)

        assertValue(Pair(expectedStart, expectedEnd), sut.outputs.dates)
    }

    @Test
    fun `ModifyViewModel input initial registers initial event`() {

        val expectedInput = "0"

        initSUT()

        sut.inputs.inputInitial(expectedInput)

        assertValue(expectedInput, sut.outputs.initial)
    }

    @Test
    fun `ModifyViewModel input final registers final event`() {

        val expectedInput = "0"

        initSUT()

        sut.inputs.inputFinal(expectedInput)

        assertValue(expectedInput, sut.outputs.final)
    }

    @Test
    fun `ModifyViewModel input interpolator registers interpolator event`() {

        val expectedInput = ACCELERATE_DECELERATE
        val expectedInterpolatorList = CountdownInterpolator
            .values()
            .map {
                Selected(it, it == ACCELERATE_DECELERATE)
            }

        initSUT()

        sut.inputs.inputInterpolator(expectedInput)

        assertValue(expectedInput, sut.outputs.interpolator)
        assertValue(expectedInterpolatorList, sut.outputs.interpolatorList)
    }

    @ParameterizedTest
    @CsvSource(
        // Valid
        "name,desc,#123123,DAYS,01/01/2020,02/02/2020,0,1,linear,true",
        // Invalid (missing name)
        ",desc,#123123,DAYS,03/01/2020,02/02/2020,0,1,linear,false",
        // Valid (missing desc)
        "name,,#123123,DAYS,03/01/2020,02/02/2020,0,1,linear,true",
        // Invalid (missing color)
        "name,desc,,DAYS,03/01/2020,02/02/2020,0,1,linear,false",
        // Invalid (dates equal)
        "name,desc,#123123,DAYS,02/02/2020,02/02/2020,0,1,linear,false",
        // Valid (start date one day before end date)
        "name,desc,#123123,DAYS,01/02/2020,02/02/2020,0,1,linear,true",
        // Invalid (start date after end date)
        "name,desc,#123123,DAYS,03/02/2020,02/02/2020,0,1,linear,false",
        // Invalid (missing start value)
        "name,desc,#123123,DAYS,03/02/2020,02/02/2020,,1,linear,false",
        // Invalid (missing end value)
        "name,desc,#123123,DAYS,03/02/2020,02/02/2020,0,,linear,false",
        // Invalid (values are the same, so nothing to map between)
        "name,desc,#123123,DAYS,03/02/2020,02/02/2020,0,0,linear,false",
        // Valid
        "name,desc,#123123,DAYS,01/01/2020,02/02/2020,0,1,linear,true"
    )
    fun `ModifyViewModel testing isValid is represented properly`(name: String?, desc: String?, color: String?, type: String, start: String?, end: String?, initial: String?, final: String?, interpolator: String, expectedIsValidValue: Boolean) {

        initSUT()

        sut.inputs.inputName(name ?: "")
        sut.inputs.inputDescription(desc ?: "")
        sut.inputs.inputColour(color ?: "")
        if (start != null && end != null) {
            sut.inputs.inputDates(start.localDateTime, end.localDateTime)
        }
        sut.inputs.inputType(getType(type))
        sut.inputs.inputInitial(initial ?: "")
        sut.inputs.inputFinal(final ?: "")
        sut.inputs.inputInterpolator(getInterpolator(interpolator))

        assertValue(expectedIsValidValue, sut.outputs.isValid)
    }

    @Test
    fun `ModifyViewModel clicking save when no date and type set to days it sets the values to be difference between dates`() {

        val diffDays = 10

        initSUT()
        setupValidInputs(type = DAYS, addDates = true, addInputs = false, diffDays = diffDays)

        sut.inputs.clickSave()

        assertValue(diffDays.toString(), sut.outputs.initial)
        assertValue("0", sut.outputs.final)
    }

    @Test
    fun `ModifyViewModel clicking saves countdown item in countdown connector`() {

        initSUT()
        setupValidInputs(type = DAYS, addDates = true, addInputs = false)

        sut.inputs.clickSave()

        verify(mockCountdownConnector).saveSync(any())
        assertEventFired(sut.outputs.closeEvent)
    }

    @Test
    fun `ModifyViewModel trying to save when dates are not added submits a crash report to the logger`() {

        initSUT()
        setupValidInputs(type = DAYS, addDates = false, addInputs = false)

        sut.inputs.clickSave()

        verify(mockCrashReporter).log(any())
    }

    @Test
    fun `ModifyViewModel connector save sync method throws a null pointer exception then isValid is reset and exception is silently logged`() {

        whenever(mockCountdownConnector.saveSync(any())).thenThrow(NullPointerException::class.java)

        initSUT()
        setupValidInputs(type = DAYS, addDates = true, addInputs = true)

        sut.inputs.clickSave()

        assertValue(false, sut.outputs.isValid)
        verify(mockCrashReporter).logException(any())
    }

    @AfterEach
    internal fun tearDown() {

        reset(mockCountdownConnector, mockCrashReporter)
    }



    private fun setupValidInputs(addDates: Boolean = true, type: CountdownType = CountdownType.NUMBER, diffDays: Int = 10, addInputs: Boolean = true) {
        sut.inputs.inputName("name")
        sut.inputs.inputDescription("desc")
        sut.inputs.inputColour("#123123")
        if (addDates) {
            sut.inputs.inputDates(
                LocalDateTime.of(2020, 1, 2, 12, 0),
                LocalDateTime.of(2020, 1, 2, 12, 0).plusDays(diffDays.toLong())
            )
        }
        sut.inputs.inputType(type)
        if (addInputs) {
            sut.inputs.inputInitial("0")
            sut.inputs.inputFinal("1")
        }
        sut.inputs.inputInterpolator(LINEAR)
    }

    private val String.localDateTime: LocalDateTime
        get() = LocalDate.parse(this, DateTimeFormatter.ofPattern("dd/MM/yyyy")).atStartOfDay()
    private fun getType(type: String): CountdownType = CountdownType.values().first { it.key == type }
    private fun getInterpolator(interpolator: String): CountdownInterpolator = CountdownInterpolator.values().first { it.key == interpolator }

}