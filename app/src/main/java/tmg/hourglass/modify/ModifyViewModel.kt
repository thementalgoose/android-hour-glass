package tmg.hourglass.modify

import androidx.lifecycle.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import org.threeten.bp.LocalDateTime
import tmg.hourglass.crash.CrashReporter
import tmg.hourglass.domain.connectors.CountdownConnector
import tmg.hourglass.domain.enums.CountdownColors
import tmg.hourglass.domain.enums.CountdownInterpolator
import tmg.hourglass.domain.enums.CountdownType
import tmg.hourglass.domain.model.Countdown
import tmg.utilities.extensions.format
import tmg.utilities.lifecycle.Event
import java.util.*

//region Inputs

interface ModifyViewModelInputs {

    fun backClicked()

    fun initialise(id: String? = null)

    fun name(text: String)
    fun description(text: String)
    fun color(text: String)
    fun type(value: CountdownType)
    fun initial(value: String)
    fun finish(value: String)
    fun interpolator(countdownInterpolator: CountdownInterpolator)
    fun startDate(date: LocalDateTime)
    fun endDate(date: LocalDateTime)

    fun saveClicked()
    fun deleteClicked()
}

//endregion

//region Outputs

interface ModifyViewModelOutputs {

    val isEdit: LiveData<Boolean>

    val name: LiveData<String>
    val description: LiveData<String>
    val color: LiveData<String>

    val type: LiveData<CountdownType>

    val initial: LiveData<String>
    val finished: LiveData<String>

    val interpolator: LiveData<CountdownInterpolator>

    val startDate: LiveData<LocalDateTime?>
    val endDate: LiveData<LocalDateTime?>

    val saveEnabled: LiveData<Boolean>

    val close: LiveData<Event>
}

//endregion

class ModifyViewModel(
    private val countdownConnector: CountdownConnector,
    private val crashReporter: CrashReporter
): ViewModel(), ModifyViewModelInputs, ModifyViewModelOutputs {

    val inputs: ModifyViewModelInputs = this
    val outputs: ModifyViewModelOutputs = this

    private val id: MutableLiveData<String?> = MutableLiveData()

    override val isEdit: LiveData<Boolean> = id
        .map { it != null}

    override val name: MutableLiveData<String> = MutableLiveData("")
    override val description: MutableLiveData<String> = MutableLiveData("")
    override val color: MutableLiveData<String> = MutableLiveData(CountdownColors.COLOUR_1.hex)
    override val type: MutableLiveData<CountdownType> = MutableLiveData(CountdownType.NUMBER)
    override val initial: MutableLiveData<String> = MutableLiveData("")
    override val finished: MutableLiveData<String> = MutableLiveData("")
    override val interpolator: MutableLiveData<CountdownInterpolator> = MutableLiveData(CountdownInterpolator.LINEAR)
    override val startDate: MutableLiveData<LocalDateTime?> = MutableLiveData()
    override val endDate: MutableLiveData<LocalDateTime?> = MutableLiveData()

    override val close: MutableLiveData<Event> = MutableLiveData()

    private val model: Flow<Countdown?> = combine(
        name.asFlow(),
        description.asFlow(),
        color.asFlow(),
        type.asFlow(),
        initial.asFlow(),
        finished.asFlow(),
        interpolator.asFlow(),
        startDate.asFlow(),
        endDate.asFlow()
    ) {
        val _name: String = (it[0] as? String) ?: ""
        val _description: String = (it[1] as? String) ?: ""
        val _color: String = (it[2] as? String) ?: ""
        val _type: CountdownType = (it[3] as? CountdownType) ?: CountdownType.NUMBER
        val _initial: String = (it[4] as? String) ?: ""
        val _finished: String = (it[5] as? String) ?: ""
        val _interpolator: CountdownInterpolator = (it[6] as? CountdownInterpolator) ?: CountdownInterpolator.LINEAR
        val _startDate: LocalDateTime? = it[7] as? LocalDateTime
        val _endDate: LocalDateTime? = it[8] as? LocalDateTime

        if (_name.isBlank()) {
            return@combine null
        }
        if (_description.isBlank()) {
            return@combine null
        }
        if (_initial.isBlank()) {
            return@combine null
        }
        if (_initial.toIntOrNull() == null || _initial.toFloatOrNull() == null) {
            return@combine null
        }
        if (_finished.isBlank()) {
            return@combine null
        }
        if (_finished.toIntOrNull() == null || _finished.toFloatOrNull() == null) {
            return@combine null
        }
        if (_startDate == null) {
            return@combine null
        }
        if (_endDate == null) {
            return@combine null
        }
        if (_endDate < LocalDateTime.now()) {
            return@combine null
        }

        return@combine Countdown(
            id = "",
            name = _name,
            description = _description,
            colour = _color,
            countdownType = _type,
            initial = _initial,
            finishing = _finished,
            start = _startDate,
            end = _endDate,
            interpolator = _interpolator
        )
    }

    override val saveEnabled: LiveData<Boolean> = model
        .map { it != null  }
        .asLiveData(viewModelScope.coroutineContext)

    override fun initialise(id: String?) {
        this.id.value = id
        if (id != null) {
            val model = countdownConnector.getSync(id)
            name.value = model?.name ?: ""
            description.value = model?.description ?: ""
            color.value = model?.colour ?: "#283793"

            type.value = model?.countdownType

            initial.value = model?.initial
            finished.value = model?.finishing

            startDate.value = model?.start
            endDate.value = model?.end
        }
    }

    override fun backClicked() {
        close.value = Event()
    }

    override fun name(text: String) {
        name.value = text
    }

    override fun description(text: String) {
        description.value = text
    }

    override fun color(text: String) {
        color.value = text
    }

    override fun type(value: CountdownType) {
        type.value = value
    }

    override fun initial(value: String) {
        initial.value = value
    }

    override fun finish(value: String) {
        finished.value = value
    }

    override fun interpolator(countdownInterpolator: CountdownInterpolator) {
        interpolator.value = countdownInterpolator
    }

    override fun startDate(date: LocalDateTime) {
        startDate.value = date
        endDate.value = null
    }

    override fun endDate(date: LocalDateTime) {
        endDate.value = date
    }

    override fun saveClicked() {
        try {
            val countdown = Countdown(
                id = id.value ?: UUID.randomUUID().toString(),
                name = name.value!!,
                description = description.value ?: "",
                colour = color.value!!,
                start = startDate.value!!,
                end = endDate.value!!,
                initial = initial.value!!,
                finishing = finished.value!!,
                countdownType = type.value!!,
                interpolator = interpolator.value!!
            )
            countdownConnector.saveSync(countdown)
            close.value = Event()
        } catch (e: NullPointerException) {
            crashReporter.logException(e)
        }
    }

    override fun deleteClicked() {
        id.value?.let {
            countdownConnector.delete(it)
        }
        close.value = Event()
    }
}