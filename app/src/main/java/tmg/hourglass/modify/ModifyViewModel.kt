package tmg.hourglass.modify

import android.util.Log
import androidx.lifecycle.MutableLiveData
import org.threeten.bp.LocalDateTime
import tmg.hourglass.base.BaseViewModel
import tmg.hourglass.data.CountdownType
import tmg.hourglass.data.connectors.CountdownConnector
import tmg.hourglass.data.models.Countdown
import tmg.utilities.lifecycle.Event
import java.util.*

//region Inputs

interface ModifyViewModelInputs {
    fun initialise(id: String?)

    fun clickClose()

    fun inputName(name: String)
    fun inputDescription(name: String)
    fun inputColour(colour: String)

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

    val name: MutableLiveData<String>
    val description: MutableLiveData<String>
    val colour: MutableLiveData<String>
    val dates: MutableLiveData<Pair<LocalDateTime, LocalDateTime>>
    val initial: MutableLiveData<String>
    val final: MutableLiveData<String>
}

//endregion

class ModifyViewModel(
    private val connector: CountdownConnector
): BaseViewModel(), ModifyViewModelInputs, ModifyViewModelOutputs {

    var inputs: ModifyViewModelInputs = this
    var outputs: ModifyViewModelOutputs = this

    private var id: String? = null
    override val isAddition: MutableLiveData<Boolean> = MutableLiveData()

    override val isValid: MutableLiveData<Boolean> = MutableLiveData()

    override val name: MutableLiveData<String> = MutableLiveData()
    override val description: MutableLiveData<String> = MutableLiveData()
    override val colour: MutableLiveData<String> = MutableLiveData()
    override val dates: MutableLiveData<Pair<LocalDateTime,LocalDateTime>> = MutableLiveData()
    override val initial: MutableLiveData<String> = MutableLiveData()
    override val final: MutableLiveData<String> = MutableLiveData()

    override val closeEvent: MutableLiveData<Event> = MutableLiveData()

    init {

    }

    //region Inputs

    override fun initialise(id: String?) {
        this.id = id
        this.isAddition.value = id == null
        if (id != null) {
            connector.getSync(id)?.let {
                name.value = it.name
                description.value = it.description
                colour.value = it.colour
                dates.value = Pair(it.start, it.end)
                initial.value = it.initial
                final.value = it.finishing
            }
        }
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
        Log.i("Passage", "Clicking save")
        try {
            val countdown: Countdown = Countdown(
                id = id ?: UUID.randomUUID().toString(),
                name = name.value!!,
                description = description.value ?: "",
                colour = colour.value!!,
                start = dates.value?.first!!,
                end = dates.value?.second!!,
                initial = initial.value!!,
                finishing = final.value!!,
                countdownType = CountdownType.NUMBER
            )
            connector.saveSync(countdown)
            closeEvent.value = Event()
        }
        catch (e: NullPointerException) {
            e.printStackTrace()
        }
    }

    override fun clickDelete() {
        id?.let {
            connector.delete(it)
        }
        closeEvent.value = Event()
    }

    //endregion

    private fun validate() {
        when {
            name.value.isNullOrEmpty() -> isValid.value = false
            colour.value.isNullOrEmpty() -> isValid.value = false
            initial.value.isNullOrEmpty() -> isValid.value = false
            initial.value?.toIntOrNull() == null -> isValid.value = false
            final.value.isNullOrEmpty() -> isValid.value = false
            final.value?.toIntOrNull() == null -> isValid.value = false
            dates.value == null -> isValid.value = false
            else -> isValid.value = true
        }
    }
}