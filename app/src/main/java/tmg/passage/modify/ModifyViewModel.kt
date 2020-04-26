package tmg.passage.modify

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import tmg.passage.base.BaseViewModel
import tmg.passage.data.PassageType
import tmg.passage.data.connectors.PassageConnector
import tmg.passage.data.models.Passage
import tmg.passage.di.passageModule
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

    val name: MutableLiveData<String>
    val description: MutableLiveData<String>
    val colour: MutableLiveData<String>
    val dates: MutableLiveData<Pair<LocalDateTime, LocalDateTime>>
    val initial: MutableLiveData<String>
    val final: MutableLiveData<String>
}

//endregion

class ModifyViewModel(
    private val connector: PassageConnector
): BaseViewModel(), ModifyViewModelInputs, ModifyViewModelOutputs {

    var inputs: ModifyViewModelInputs = this
    var outputs: ModifyViewModelOutputs = this

    private var id: String? = null
    override val isAddition: MutableLiveData<Boolean> = MutableLiveData()

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
                final.value = it.final
            }
        }
    }

    override fun clickClose() {
        closeEvent.value = Event()
    }

    override fun inputName(name: String) {
        this.name.value = name
    }

    override fun inputDescription(name: String) {
        this.description.value = name
    }

    override fun inputColour(colour: String) {
        this.colour.value = colour
    }

    override fun inputDates(start: LocalDateTime, end: LocalDateTime) {
        this.dates.value = Pair(start, end)
    }

    override fun inputInitial(value: String) {
        this.initial.value = value
    }

    override fun inputFinal(value: String) {
        this.final.value = value
    }

    override fun clickSave() {
        Log.i("Passage", "Clicking save")
        try {
            val passage: Passage = Passage(
                id = id ?: UUID.randomUUID().toString(),
                name = name.value!!,
                description = description.value ?: "",
                colour = colour.value!!,
                start = dates.value?.first!!,
                end = dates.value?.second!!,
                initial = initial.value!!,
                final = final.value!!,
                passageType = PassageType.NUMBER
            )
            connector.saveSync(passage)
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
}