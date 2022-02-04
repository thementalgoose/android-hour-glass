package tmg.hourglass.modify

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import tmg.hourglass.crash.CrashReporter
import tmg.hourglass.domain.connectors.CountdownConnector
import tmg.hourglass.domain.enums.CountdownInterpolator
import tmg.hourglass.domain.enums.CountdownType
import tmg.hourglass.modify.ModifyViewModel
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
    fun `initialising vm sets isEdit to false`() {

        initSUT()

        sut.outputs.isEdit.test {
            assertValue(false)
        }
    }

    @Test
    fun `initialising vm with isEdit to true`() {

        initSUT()

        sut.inputs.initialise("my-id")
        sut.outputs.isEdit.test {
            assertValue(true)
        }
    }

    @Test
    fun `input name registers name update`() {

        val expectedInput= "test"

        initSUT()

        sut.inputs.name(expectedInput)

        sut.outputs.name.test {
            assertValue(expectedInput)
        }
    }

    @Test
    fun `input description registers description update`() {

        val expectedInput= "desc"

        initSUT()

        sut.inputs.description(expectedInput)
        sut.outputs.description.test {
            assertValue(expectedInput)
        }
    }

    @Test
    fun `input colours registers colours update`() {

        val expectedInput= "#123123"

        initSUT()

        sut.inputs.color(expectedInput)
        sut.outputs.color.test {
            assertValue(expectedInput)
        }
    }

    @Test
    fun `input type registers type update`() {

        val expectedInput = CountdownType.GRAMS

        initSUT()

        sut.inputs.type(expectedInput)

        sut.outputs.type.test {
            assertValue(expectedInput)
        }
    }

    @Test
    fun `input type as days registers type update and shows range event`() {

        val expectedInput = CountdownType.DAYS
        val expectedTypeList = CountdownType
            .values()
            .map {
                Selected(it, it == CountdownType.DAYS)
            }

        initSUT()

        sut.inputs.type(expectedInput)

        sut.outputs.type.test {
            assertValue(expectedInput)
        }
    }

    @Test
    fun `input start dates registers dates event`() {

        val expectedStart = LocalDateTime.now()

        initSUT()

        sut.inputs.startDate(expectedStart)

        sut.outputs.startDate.test {
            assertValue(expectedStart)
        }
    }

    @Test
    fun `input end dates registers dates event`() {

        val expectedEnd = LocalDateTime.now()

        initSUT()

        sut.inputs.endDate(expectedEnd)

        sut.outputs.endDate.test {
            assertValue(expectedEnd)
        }
    }

    @Test
    fun `input initial registers initial event`() {

        val expectedInput = "0"

        initSUT()

        sut.inputs.initial(expectedInput)

        sut.outputs.initial.test {
            assertValue(expectedInput)
        }
    }

    @Test
    fun `input final registers final event`() {

        val expectedInput = "0"

        initSUT()

        sut.inputs.finish(expectedInput)

        sut.outputs.finished.test {
            assertValue(expectedInput)
        }
    }

    @Test
    fun `input interpolator registers interpolator event`() {

        val expectedInput = CountdownInterpolator.ACCELERATE_DECELERATE
        val expectedInterpolatorList = CountdownInterpolator
            .values()
            .map {
                Selected(it, it == CountdownInterpolator.ACCELERATE_DECELERATE)
            }

        initSUT()

        sut.inputs.interpolator(expectedInput)

        sut.outputs.interpolator.test {
            assertValue(expectedInput)
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

        sut.inputs.name(name ?: "")
        sut.inputs.description(desc ?: "")
        sut.inputs.color(color ?: "")
        start?.localDateTime?.let { sut.inputs.startDate(it) }
        end?.localDateTime?.let { sut.inputs.endDate(it) }
        sut.inputs.type(getType(type))
        sut.inputs.initial(initial ?: "")
        sut.inputs.finish(final ?: "")
        sut.inputs.interpolator(getInterpolator(interpolator))

        sut.outputs.saveEnabled.test {
            assertValue(expectedIsValidValue)
        }
    }

    @Test
    fun `clicking save when no date and type set to days it sets the values to be difference between dates`() {

        val diffDays = 10

        initSUT()
        setupValidInputs(type = CountdownType.DAYS, addDates = true, addInputs = false, diffDays = diffDays)

        sut.inputs.saveClicked()

        sut.outputs.initial.test {
            assertValue(diffDays.toString())
        }
        sut.outputs.finished.test {
            assertValue("0")
        }
    }

    @Test
    fun `clicking saves countdown item in countdown connector`() {

        initSUT()
        setupValidInputs(type = CountdownType.DAYS, addDates = true, addInputs = false)

        sut.inputs.saveClicked()

        verify {
            mockCountdownConnector.saveSync(any())
        }

        sut.outputs.close.test {
            assertEventFired()
        }
    }

    @Test
    fun `trying to save when dates are not added submits a crash report to the logger`() {

        initSUT()
        setupValidInputs(type = CountdownType.DAYS, addDates = false, addInputs = false)

        sut.inputs.saveClicked()

        verify {
            mockCrashReporter.log(any())
        }
    }

    @Test
    fun `connector save sync method throws a null pointer exception then isValid is reset and exception is silently logged`() {

        every { mockCountdownConnector.saveSync(any()) } throws NullPointerException()

        initSUT()
        setupValidInputs(type = CountdownType.DAYS, addDates = true, addInputs = true)

        sut.inputs.saveClicked()

        sut.outputs.saveEnabled.test {
            assertValue(false)
        }
        verify {
            mockCrashReporter.logException(any())
        }
    }


    private fun setupValidInputs(addDates: Boolean = true, type: CountdownType = CountdownType.NUMBER, diffDays: Int = 10, addInputs: Boolean = true) {
        sut.inputs.name("name")
        sut.inputs.description("desc")
        sut.inputs.color("#123123")
        if (addDates) {
            sut.inputs.startDate(LocalDateTime.of(2020, 1, 2, 12, 0))
            sut.inputs.endDate(LocalDateTime.of(2020, 1, 2, 12, 0).plusDays(diffDays.toLong()))
        }
        sut.inputs.type(type)
        if (addInputs) {
            sut.inputs.initial("0")
            sut.inputs.finish("1")
        }
        sut.inputs.interpolator(CountdownInterpolator.LINEAR)
    }

    private val String.localDateTime: LocalDateTime
        get() = LocalDate.parse(this, DateTimeFormatter.ofPattern("dd/MM/yyyy")).atStartOfDay()
    private fun getType(type: String): CountdownType = CountdownType.values().first { it.key == type }
    private fun getInterpolator(interpolator: String): CountdownInterpolator = CountdownInterpolator.values().first { it.key == interpolator }

}