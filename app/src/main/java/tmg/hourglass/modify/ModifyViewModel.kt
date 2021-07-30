package tmg.hourglass.modify

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.threeten.bp.LocalDateTime
import org.threeten.bp.temporal.ChronoUnit
import tmg.hourglass.ui.base.BaseViewModel
import tmg.hourglass.domain.crash.CrashReporter
import tmg.hourglass.domain.data.CountdownInterpolator
import tmg.hourglass.domain.data.CountdownType
import tmg.hourglass.domain.data.CountdownType.*
import tmg.hourglass.domain.data.connectors.CountdownConnector
import tmg.hourglass.domain.data.models.Countdown
import tmg.hourglass.extensions.format
import tmg.hourglass.utils.Selected
import tmg.utilities.lifecycle.DataEvent
import tmg.utilities.lifecycle.Event
import java.util.*

private val requiresStart: List<CountdownType> = CountdownType.values()
    .filter { it != DAYS }
private val requiresEnd: List<CountdownType> = CountdownType.values()
    .filter { it != DAYS }

//region Inputs

interface ModifyViewModelInputs {
    fun initialise(id: String?)

    fun clickClose()

    fun inputName(name: String)
    fun inputDescription(name: String)
    fun inputColour(colour: String)

    fun inputType(type: CountdownType)

    fun clickDatePicker()
    fun inputDates(start: LocalDateTime, end: LocalDateTime)

    fun inputInitial(value: String)
    fun inputFinal(value: String)
    fun inputInterpolator(interpolator: CountdownInterpolator)

    fun clickSave()
    fun clickDelete()
}

//endregion

//region Outputs

interface ModifyViewModelOutputs {
    val closeEvent: LiveData<Event>
    val isAddition: LiveData<Boolean>

    val isValid: LiveData<Boolean>

    val showRange: LiveData<Pair<Boolean, Boolean>>

    val type: LiveData<CountdownType>
    val typeList: LiveData<List<Selected<CountdownType>>>

    val name: LiveData<String>
    val description: LiveData<String>
    val colour: LiveData<String>

    val showDatePicker: LiveData<DataEvent<Pair<LocalDateTime, LocalDateTime>?>>
    val dates: LiveData<Pair<LocalDateTime, LocalDateTime>>
    val initial: LiveData<String>
    val final: LiveData<String>

    val interpolator: LiveData<CountdownInterpolator>
    val interpolatorList: LiveData<List<Selected<CountdownInterpolator>>>
}

//endregion

class ModifyViewModel(
    private val connector: CountdownConnector,
    private val crashReporter: CrashReporter
) : BaseViewModel(), ModifyViewModelInputs, ModifyViewModelOutputs {

    var inputs: ModifyViewModelInputs = this
    var outputs: ModifyViewModelOutputs = this

    private var id: String? = null
    override val isAddition: MutableLiveData<Boolean> = MutableLiveData()

    override val isValid: MutableLiveData<Boolean> = MutableLiveData()
    override val showRange: MutableLiveData<Pair<Boolean, Boolean>> = MutableLiveData()

    override val showDatePicker: MutableLiveData<DataEvent<Pair<LocalDateTime, LocalDateTime>?>> = MutableLiveData()

    override val name: MutableLiveData<String> = MutableLiveData()
    override val description: MutableLiveData<String> = MutableLiveData()
    override val colour: MutableLiveData<String> = MutableLiveData()
    override val dates: MutableLiveData<Pair<LocalDateTime, LocalDateTime>> = MutableLiveData()
    override val initial: MutableLiveData<String> = MutableLiveData()
    override val final: MutableLiveData<String> = MutableLiveData()

    override val type: MutableLiveData<CountdownType> = MutableLiveData()
    override val typeList: MutableLiveData<List<Selected<CountdownType>>> = MutableLiveData()

    override val interpolator: MutableLiveData<CountdownInterpolator> = MutableLiveData()
    override val interpolatorList: MutableLiveData<List<Selected<CountdownInterpolator>>> = MutableLiveData()

    override val closeEvent: MutableLiveData<Event> = MutableLiveData()

    init {
        interpolator.value = CountdownInterpolator.LINEAR
    }

    //region Inputs

    override fun initialise(id: String?) {
        this.id = id
        this.isAddition.value = id == null
        this.type.value = NUMBER
        if (id != null) {
            connector.getSync(id)?.let {
                name.value = it.name
                description.value = it.description
                colour.value = it.colour
                dates.value = Pair(it.start, it.end)
                initial.value = it.initial
                final.value = it.finishing
                type.value = it.countdownType
                interpolator.value = it.interpolator
            }
        }
        updateTypeList(this.type.value!!)
        updateInterpolatorList(this.interpolator.value!!)
        validate()
    }

    override fun clickClose() {
        closeEvent.value = Event()
    }

    override fun inputName(name: String) {
        this.name.value = name
        validate()
    }

    override fun inputDescription(name: String) {
        this.description.value = name
    }

    override fun inputColour(colour: String) {
        this.colour.value = colour
        validate()
    }

    override fun inputType(type: CountdownType) {
        this.type.value = type
        this.updateTypeList(type)
    }

    override fun clickDatePicker() {
        this.showDatePicker.value = DataEvent(this.dates.value)
    }

    override fun inputDates(start: LocalDateTime, end: LocalDateTime) {
        this.dates.value = Pair(start, end)
        validate()
    }

    override fun inputInitial(value: String) {
        this.initial.value = value
        validate()
    }

    override fun inputFinal(value: String) {
        this.final.value = value
        validate()
    }

    override fun inputInterpolator(interpolator: CountdownInterpolator) {
        this.interpolator.value = interpolator
        this.updateInterpolatorList(interpolator)
    }

    override fun clickSave() {
        try {
            updatePartialValues(type.value!!)
            val countdown = Countdown(
                id = id ?: UUID.randomUUID().toString(),
                name = name.value!!,
                description = description.value ?: "",
                colour = colour.value!!,
                start = dates.value?.first!!,
                end = dates.value?.second!!,
                initial = initial.value!!,
                finishing = final.value!!,
                countdownType = type.value!!,
                interpolator = interpolator.value!!
            )
            connector.saveSync(countdown)
            closeEvent.value = Event()
        } catch (e: NullPointerException) {
            validate()
            isValid.value = false
            crashReporter.logException(e)
        }
    }

    override fun clickDelete() {
        id?.let {
            connector.delete(it)
        }
        closeEvent.value = Event()
    }

    //endregion

    private fun updatePartialValues(type: CountdownType) {
        when (type) {
            DAYS -> {
                val startDate: LocalDateTime? = dates.value?.first
                val endDate: LocalDateTime? = dates.value?.second
                if (startDate != null && endDate != null) {
                    val days = ChronoUnit.DAYS.between(startDate, endDate)
                    initial.value = days.toString()
                    final.value = "0"
                }
                else {
                    crashReporter.log("Validation error occurred - Days selected and save attempted before date range is configured")
                }
            }
            else -> {}
        }
    }

    private fun updateTypeList(type: CountdownType) {
        when (type) {
            DAYS -> showRange.value = Pair(false, second = false)
            else -> showRange.value = Pair(true, second = true)
        }
        typeList.value = CountdownType
            .values()
            .map {
                Selected(it, it == type)
            }
    }

    private fun updateInterpolatorList(interpolator: CountdownInterpolator) {
        interpolatorList.value = CountdownInterpolator
            .values()
            .map {
                Selected(it, it == interpolator)
            }
    }

    private fun validate() {
        val startDate: String? = dates.value?.first?.format("yyyy/MM/dd")
        val endDate: String? = dates.value?.second?.format("yyyy/MM/dd")
        when {
            name.value.isNullOrEmpty() -> isValid.value = false
            colour.value.isNullOrEmpty() -> isValid.value = false
            initial.value.isNullOrEmpty() && type.value.hasStartValue() -> isValid.value = false
            initial.value?.toIntOrNull() == null && type.value.hasStartValue() -> isValid.value = false
            final.value.isNullOrEmpty() && type.value.hasEndValue() -> isValid.value = false
            final.value?.toIntOrNull() == null && type.value.hasEndValue() -> isValid.value = false
            type.value.hasStartValue() && type.value.hasEndValue() && initial.value == final.value -> isValid.value = false
            dates.value == null -> isValid.value = false
            startDate == endDate -> isValid.value = false
            ((startDate?.compareTo(endDate ?: "1970/01/01") ?: -1) >= 0) -> isValid.value = false
            else -> isValid.value = true
        }
    }

    private fun CountdownType?.hasStartValue(): Boolean {
        return requiresStart.contains(this ?: NUMBER)
    }

    private fun CountdownType?.hasEndValue(): Boolean {
        return requiresEnd.contains(this ?: NUMBER)
    }
}