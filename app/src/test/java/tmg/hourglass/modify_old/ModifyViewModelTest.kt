package tmg.hourglass.modify_old

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import tmg.hourglass.crash.CrashReporter
import tmg.hourglass.domain.enums.CountdownInterpolator
import tmg.hourglass.domain.enums.CountdownInterpolator.ACCELERATE_DECELERATE
import tmg.hourglass.domain.enums.CountdownInterpolator.LINEAR
import tmg.hourglass.domain.enums.CountdownType
import tmg.hourglass.domain.enums.CountdownType.DAYS
import tmg.hourglass.domain.enums.CountdownType.GRAMS
import tmg.hourglass.domain.connectors.CountdownConnector
import tmg.hourglass.utils.Selected
import tmg.testutils.BaseTest
import tmg.testutils.livedata.assertDataEventValue
import tmg.testutils.livedata.assertEventFired
import tmg.testutils.livedata.test

internal class ModifyViewModelTest: BaseTest() {

    lateinit var sut: ModifyViewModel

    private val mockCountdownConnector: CountdownConnector = mockk(relaxed = true)
    private val mockCrashReporter: CrashReporter = mockk(relaxed = true)

    private fun initSUT() {
        sut = ModifyViewModel(mockCountdownConnector, mockCrashReporter)
        sut.inputs.initialise(null)
    }

    @Test
    fun `initialising vm sets is addition to true`() {

        initSUT()

        sut.outputs.isAddition.test {
            assertValue(true)
        }
    }

    @Test
    fun `input name registers name update`() {

        val expectedInput= "test"

        initSUT()

        sut.inputs.inputName(expectedInput)

        sut.outputs.name.test {
            assertValue(expectedInput)
        }
    }

    @Test
    fun `input description registers description update`() {

        val expectedInput= "desc"

        initSUT()

        sut.inputs.inputDescription(expectedInput)
        sut.outputs.description.test {
            assertValue(expectedInput)
        }
    }

    @Test
    fun `input colours registers colours update`() {

        val expectedInput= "#123123"

        initSUT()

        sut.inputs.inputColour(expectedInput)
        sut.outputs.colour.test {
            assertValue(expectedInput)
        }
    }

    @Test
    fun `input type registers type update`() {

        val expectedInput = GRAMS
        val expectedTypeList = CountdownType
            .values()
            .map {
                Selected(it, it == GRAMS)
            }

        initSUT()

        sut.inputs.inputType(expectedInput)

        sut.outputs.type.test {
            assertValue(expectedInput)
        }
        sut.outputs.showRange.test {
            assertValue(Pair(true, second = true))
        }
        sut.outputs.typeList.test {
            assertValue(expectedTypeList)
        }
    }

    @Test
    fun `input type as days registers type update and shows range event`() {

        val expectedInput = DAYS
        val expectedTypeList = CountdownType
            .values()
            .map {
                Selected(it, it == DAYS)
            }

        initSUT()

        sut.inputs.inputType(expectedInput)

        sut.outputs.type.test {
            assertValue(expectedInput)
        }
        sut.outputs.showRange.test {
            assertValue(Pair(false, second = false))
        }
        sut.outputs.typeList.test {
            assertValue(expectedTypeList)
        }
    }

    @Test
    fun `open date picker launches date picker event with blank value on first run`() {

        initSUT()

        sut.inputs.clickDatePicker()

        sut.outputs.showDatePicker.test {
            assertDataEventValue(null)
        }
    }

    @Test
    fun `input dates registers dates event`() {

        val expectedStart = LocalDateTime.now()
        val expectedEnd = LocalDateTime.now()

        initSUT()

        sut.inputs.inputDates(expectedStart, expectedEnd)

        sut.outputs.dates.test {
            assertValue(Pair(expectedStart, expectedEnd))
        }
    }

    @Test
    fun `input initial registers initial event`() {

        val expectedInput = "0"

        initSUT()

        sut.inputs.inputInitial(expectedInput)

        sut.outputs.initial.test {
            assertValue(expectedInput)
        }
    }

    @Test
    fun `input final registers final event`() {

        val expectedInput = "0"

        initSUT()

        sut.inputs.inputFinal(expectedInput)

        sut.outputs.final.test {
            assertValue(expectedInput)
        }
    }

    @Test
    fun `input interpolator registers interpolator event`() {

        val expectedInput = ACCELERATE_DECELERATE
        val expectedInterpolatorList = CountdownInterpolator
            .values()
            .map {
                Selected(it, it == ACCELERATE_DECELERATE)
            }

        initSUT()

        sut.inputs.inputInterpolator(expectedInput)

        sut.outputs.interpolator.test {
            assertValue(expectedInput)
        }
        sut.outputs.interpolatorList.test {
            assertValue(expectedInterpolatorList)
        }
    }

    @ParameterizedTest(name = "Valid inputs {9} - name={0}, desc={1}, colour={2}, type={3}, dates={4}-{5}, values={6}-{7}, interpolator={8}")
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
    fun `testing isValid is represented properly`(name: String?, desc: String?, color: String?, type: String, start: String?, end: String?, initial: String?, final: String?, interpolator: String, expectedIsValidValue: Boolean) {

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

        sut.outputs.isValid.test {
            assertValue(expectedIsValidValue)
        }
    }

    @Test
    fun `clicking save when no date and type set to days it sets the values to be difference between dates`() {

        val diffDays = 10

        initSUT()
        setupValidInputs(type = DAYS, addDates = true, addInputs = false, diffDays = diffDays)

        sut.inputs.clickSave()

        sut.outputs.initial.test {
            assertValue(diffDays.toString())
        }
        sut.outputs.final.test {
            assertValue("0")
        }
    }

    @Test
    fun `clicking saves countdown item in countdown connector`() {

        initSUT()
        setupValidInputs(type = DAYS, addDates = true, addInputs = false)

        sut.inputs.clickSave()

        verify {
            mockCountdownConnector.saveSync(any())
        }

        sut.outputs.closeEvent.test {
            assertEventFired()
        }
    }

    @Test
    fun `trying to save when dates are not added submits a crash report to the logger`() {

        initSUT()
        setupValidInputs(type = DAYS, addDates = false, addInputs = false)

        sut.inputs.clickSave()

        verify {
            mockCrashReporter.log(any())
        }
    }

    @Test
    fun `connector save sync method throws a null pointer exception then isValid is reset and exception is silently logged`() {

        every { mockCountdownConnector.saveSync(any()) } throws NullPointerException()

        initSUT()
        setupValidInputs(type = DAYS, addDates = true, addInputs = true)

        sut.inputs.clickSave()

        sut.outputs.isValid.test {
            assertValue(false)
        }
        verify {
            mockCrashReporter.logException(any())
        }
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