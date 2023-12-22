package tmg.hourglass.presentation.modify

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.threeten.bp.LocalDateTime
import tmg.hourglass.core.googleanalytics.CrashReporter
import tmg.hourglass.domain.connectors.CountdownConnector
import tmg.hourglass.domain.enums.CountdownColors
import tmg.hourglass.domain.enums.CountdownInterpolator
import tmg.hourglass.domain.enums.CountdownType
import tmg.hourglass.domain.model.Countdown
import java.util.UUID
import javax.inject.Inject

//region Inputs

interface ModifyViewModelInputs {

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

    val isEdit: StateFlow<Boolean>

    val name: StateFlow<String>
    val description: StateFlow<String>
    val color: StateFlow<String>

    val type: StateFlow<CountdownType>

    val initial: StateFlow<String>
    val finished: StateFlow<String>

    val interpolator: StateFlow<CountdownInterpolator>

    val startDate: StateFlow<LocalDateTime?>
    val endDate: StateFlow<LocalDateTime?>

    val saveEnabled: StateFlow<Boolean>
}

//endregion

@HiltViewModel
class ModifyViewModel @Inject constructor(
    private val countdownConnector: CountdownConnector,
    private val crashReporter: CrashReporter
): ViewModel(), ModifyViewModelInputs, ModifyViewModelOutputs {

    val inputs: ModifyViewModelInputs = this
    val outputs: ModifyViewModelOutputs = this

    private val id: MutableStateFlow<String?> = MutableStateFlow(null)

    override val isEdit: StateFlow<Boolean> = id
        .map { it != null}
        .stateIn(viewModelScope, SharingStarted.Lazily, false)

    override val name: MutableStateFlow<String> = MutableStateFlow("")
    override val description: MutableStateFlow<String> = MutableStateFlow("")
    override val color: MutableStateFlow<String> = MutableStateFlow(CountdownColors.COLOUR_1.hex)
    override val type: MutableStateFlow<CountdownType> = MutableStateFlow(CountdownType.NUMBER)
    override val initial: MutableStateFlow<String> = MutableStateFlow("")
    override val finished: MutableStateFlow<String> = MutableStateFlow("")
    override val interpolator: MutableStateFlow<CountdownInterpolator> = MutableStateFlow(CountdownInterpolator.LINEAR)
    override val startDate: MutableStateFlow<LocalDateTime?> = MutableStateFlow(null)
    override val endDate: MutableStateFlow<LocalDateTime?> = MutableStateFlow(null)

    private val model: Flow<Countdown?> = combine(
        name,
        description,
        color,
        type,
        initial,
        finished,
        interpolator,
        startDate,
        endDate
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
        if (_color.isBlank()) {
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
        if (_startDate == _endDate) {
            return@combine null
        }
        if (_endDate < _startDate) {
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

    override val saveEnabled: StateFlow<Boolean> = model
        .map { it != null }
        .stateIn(viewModelScope, SharingStarted.Lazily, false)

    override fun initialise(id: String?) {
        this.id.value = id
        if (id != null) {
            val model = countdownConnector.getSync(id)
            name.value = model?.name ?: ""
            description.value = model?.description ?: ""
            color.value = model?.colour ?: CountdownColors.COLOUR_1.hex

            type.value = model?.countdownType ?: CountdownType.NUMBER

            initial.value = model?.initial ?: ""
            finished.value = model?.finishing ?: ""

            startDate.value = model?.start
            endDate.value = model?.end
        } else {
            name.value = ""
            description.value = ""
            color.value = CountdownColors.COLOUR_1.hex

            type.value = CountdownType.NUMBER

            initial.value = ""
            finished.value = ""

            startDate.value = null
            endDate.value = null
        }
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
                name = name.value,
                description = description.value,
                colour = color.value,
                start = startDate.value!!,
                end = endDate.value!!,
                initial = initial.value,
                finishing = finished.value,
                countdownType = type.value,
                interpolator = interpolator.value
            )
            countdownConnector.saveSync(countdown)
        } catch (e: NullPointerException) {
            crashReporter.logException(e)
        }
    }

    override fun deleteClicked() {
        id.value?.let {
            countdownConnector.delete(it)
        }
    }
}