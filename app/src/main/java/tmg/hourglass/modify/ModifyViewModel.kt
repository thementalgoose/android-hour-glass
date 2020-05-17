package tmg.hourglass.modify

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.crashlytics.internal.common.CrashlyticsCore
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.temporal.ChronoUnit
import tmg.hourglass.base.BaseViewModel
import tmg.hourglass.crash.CrashReporter
import tmg.hourglass.data.CountdownType
import tmg.hourglass.data.CountdownType.*
import tmg.hourglass.data.connectors.CountdownConnector
import tmg.hourglass.data.models.Countdown
import tmg.hourglass.extensions.format
import tmg.hourglass.utils.Selected
import tmg.utilities.lifecycle.Event
import java.util.*

private val requiresStart: List<CountdownType> = listOf(
    NUMBER,
    MILES,
    KILOMETRES,
    MONEY_GBP,
    MONEY_USD,
    MONEY_EUR
)
private val requiresEnd: List<CountdownType> = listOf(
    NUMBER,
    MILES,
    KILOMETRES,
    MONEY_GBP,
    MONEY_USD,
    MONEY_EUR
)

//region Inputs

interface ModifyViewModelInputs {
    fun initialise(id: String?)

    fun clickClose()

    fun inputName(name: String)
    fun inputDescription(name: String)
    fun inputColour(colour: String)

    fun inputType(type: CountdownType)
    fun inputDates(start: LocalDateTime, end: LocalDateTime)
    fun inputInitial(value: String)
    fun inputFinal(value: String)

    fun clickSave()
    fun clickDelete()
}

//endregion

//region Outputs

interface ModifyViewModelOutputs {
    val closeEvent: MutableLiveData<Event>
    val isAddition: MutableLiveData<Boolean>

    val isValid: MutableLiveData<Boolean>

    val showRange: MutableLiveData<Pair<Boolean, Boolean>>

    val type: MutableLiveData<CountdownType>
    val typeList: MutableLiveData<List<Selected<CountdownType>>>

    val name: MutableLiveData<String>
    val description: MutableLiveData<String>
    val colour: MutableLiveData<String>
    val dates: MutableLiveData<Pair<LocalDateTime, LocalDateTime>>
    val initial: MutableLiveData<String>
    val final: MutableLiveData<String>
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

    override val name: MutableLiveData<String> = MutableLiveData()
    override val description: MutableLiveData<String> = MutableLiveData()
    override val colour: MutableLiveData<String> = MutableLiveData()
    override val dates: MutableLiveData<Pair<LocalDateTime, LocalDateTime>> = MutableLiveData()
    override val initial: MutableLiveData<String> = MutableLiveData()
    override val final: MutableLiveData<String> = MutableLiveData()

    override val type: MutableLiveData<CountdownType> = MutableLiveData()
    override val typeList: MutableLiveData<List<Selected<CountdownType>>> = MutableLiveData()

    override val closeEvent: MutableLiveData<Event> = MutableLiveData()

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
            }
        }
        updateList(this.type.value!!)
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
        this.updateList(type)
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
                countdownType = type.value!!
            )
            connector.saveSync(countdown)
            closeEvent.value = Event()
        } catch (e: NullPointerException) {
            e.printStackTrace()
            // TODO: Notify of a failure
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

                    // TODO: Remove logging here
                    Log.i("HourGlass", "Days found: $days between $startDate and $endDate")
                }
                else {
                    crashReporter.log("Validation error occurred - Days selected and save attempted before date range is configured")
                }
            }

            else -> {}
        }
    }

    private fun updateList(type: CountdownType) {
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

    private fun validate() {
        when {
            name.value.isNullOrEmpty() -> isValid.value = false
            colour.value.isNullOrEmpty() -> isValid.value = false
            initial.value.isNullOrEmpty() && type.value.hasStartValue() -> isValid.value = false
            initial.value?.toIntOrNull() == null && type.value.hasStartValue() -> isValid.value = false
            final.value.isNullOrEmpty() && type.value.hasEndValue() -> isValid.value = false
            final.value?.toIntOrNull() == null && type.value.hasEndValue() -> isValid.value = false
            type.value.hasStartValue() && type.value.hasEndValue() && initial.value == final.value -> isValid.value = false
            dates.value == null -> isValid.value = false
            dates.value?.first?.format("dd/MM/yyyy") == dates.value?.second?.format("dd/MM/yyyy") -> isValid.value = false
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