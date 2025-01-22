package tmg.hourglass.presentation.modify

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import tmg.hourglass.core.googleanalytics.CrashReporter
import tmg.hourglass.domain.connectors.CountdownConnector
import tmg.hourglass.domain.enums.CountdownColors
import tmg.hourglass.domain.enums.CountdownType
import tmg.hourglass.presentation.modify.ModifyMapper.toCountdown
import tmg.hourglass.presentation.modify.ModifyMapper.toUiState
import tmg.hourglass.presentation.modify.UiState.Direction.CountDown
import tmg.hourglass.presentation.modify.UiState.Direction.CountUp
import tmg.hourglass.presentation.modify.UiState.Direction.Custom
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ModifyViewModel @Inject constructor(
    private val countdownConnector: CountdownConnector,
    private val crashReporter: CrashReporter
): ViewModel() {
    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(getUiState())
    val uiState: StateFlow<UiState> = _uiState

    private var id: String? = null

    private fun getUiState(): UiState = UiState(
        title = "",
        description = "",
        colorHex = CountdownColors.COLOUR_1.hex,
        type = CountdownType.DAYS,
        inputTypes = UiState.Types.EndDate(
            finishDate = null
        )
    )

    fun initialise(id: String?) {
        if (id == null) {
            this.id = null
            _uiState.value = getUiState()
        } else {
            countdownConnector.getSync(id)?.let {
                this.id = id
                _uiState.value = it.toUiState()
            }
        }
    }

    fun setTitle(title: String) {
        _uiState.value = _uiState.value.copy(
            title = title
        )
    }
    fun setDescription(description: String) {
        _uiState.value = _uiState.value.copy(
            description = description
        )
    }
    fun setColor(colorHex: String) {
        _uiState.value = _uiState.value.copy(
            colorHex = colorHex
        )
    }
    fun setType(type: CountdownType) {
        val existingType = uiState.value.inputTypes
        val newType = when (type) {
            CountdownType.DAYS,
            CountdownType.SECONDS -> {
                UiState.Types.EndDate(finishDate = null)
            }
            else -> {
                UiState.Types.Values(
                    valueDirection = CountDown,
                    startDate = null,
                    initial = "",
                    finishDate = null,
                    finishing = "",
                )
            }
        }
        _uiState.value = _uiState.value.copy(
            type = type,
            inputTypes = when (newType == existingType) {
                true -> existingType
                false -> newType
            }
        )
    }
    fun setStartDate(date: LocalDateTime) {
        val existingType = _uiState.value.inputTypes
        if (existingType is UiState.Types.Values) {
            _uiState.value = _uiState.value.copy(
                inputTypes = existingType.copy(
                    startDate = date,
                    finishDate = null
                )
            )
        }
    }
    fun setEndDate(date: LocalDateTime) {
        when (val existingType = _uiState.value.inputTypes) {
            is UiState.Types.Values -> {
                _uiState.value = _uiState.value.copy(
                    inputTypes = existingType.copy(
                        finishDate = date
                    )
                )
            }
            is UiState.Types.EndDate -> {
                _uiState.value = _uiState.value.copy(
                    inputTypes = existingType.copy(
                        finishDate = date
                    )
                )
            }
        }
    }
    fun setValueDirection(direction: UiState.Direction) {
        val existingType = _uiState.value.inputTypes
        if (existingType is UiState.Types.Values) {
            _uiState.value = _uiState.value.copy(
                inputTypes = existingType.copy(
                    valueDirection = direction,
                    initial = existingType.initial.takeIf { direction == CountUp || direction == Custom } ?: "",
                    finishing = existingType.finishing.takeIf { direction == CountUp || direction == Custom } ?: ""
                )
            )
        }
    }
    fun setStartValue(value: String) {
        val existingType = _uiState.value.inputTypes
        if (existingType is UiState.Types.Values) {
            _uiState.value = _uiState.value.copy(
                inputTypes = existingType.copy(
                    initial = value
                )
            )
        }
    }
    fun setEndValue(value: String) {
        val existingType = _uiState.value.inputTypes
        if (existingType is UiState.Types.Values) {
            _uiState.value = _uiState.value.copy(
                inputTypes = existingType.copy(
                    finishing = value
                )
            )
        }
    }

    fun save() {
        try {
            val uiState = _uiState.value

            if (!uiState.saveEnabled) {
                crashReporter.logException(IllegalStateException("Save clicked while data is considered invalid. Model = $uiState"))
                return
            }

            val countdown = uiState.toCountdown(id ?: UUID.randomUUID().toString())
            countdownConnector.saveSync(countdown)
        } catch (e: NullPointerException) {
            crashReporter.logException(e)
        }
    }

    fun delete() {
        id?.let {
            countdownConnector.delete(it)
        }
    }
}

data class UiState(
    val title: String,
    val description: String,
    val colorHex: String,
    val type: CountdownType,
    val inputTypes: Types
) {
    sealed class Types {
        data class EndDate(
            val finishDate: LocalDateTime?
        ): Types()
        data class Values(
            val valueDirection: Direction,
            val startDate: LocalDateTime?,
            val finishDate: LocalDateTime?,
            val initial: String,
            val finishing: String,
        ): Types()
    }

    enum class Direction {
        CountUp,
        CountDown,
        Custom
    }

    private fun isDataValid(): Boolean {
        if (title.isBlank()) {
            return false
        }
        if (colorHex.isBlank()) {
            return false
        }
        when (inputTypes) {
            is Types.EndDate -> {
                if (inputTypes.finishDate == null) {
                    return false
                }
                if (inputTypes.finishDate.toLocalDate() < LocalDate.now()) {
                    return false
                }
                return true
            }
            is Types.Values -> {
                val hasInitialData = inputTypes.initial.isNotBlank() && inputTypes.initial != "0"
                val hasFinishingData = inputTypes.finishing.isNotBlank() && inputTypes.finishing != "0"

                if (!hasInitialData && !hasFinishingData) {
                    return false
                }
                if (inputTypes.initial.toIntOrNull() == null ||
                    inputTypes.finishing.toIntOrNull() == null ||
                    inputTypes.initial == inputTypes.finishing) {
                    return false
                }

                val hasStartDate = inputTypes.startDate != null
                val hasEndDate = inputTypes.finishDate != null
                if (!hasStartDate && !hasEndDate) {
                    return false
                }
                if (hasStartDate && inputTypes.startDate!!.toLocalDate() > LocalDate.now()) {
                    return false
                }
                if (hasEndDate && inputTypes.finishDate!!.toLocalDate() < LocalDate.now()) {
                    return false
                }
                return true
            }
        }
    }

    val saveEnabled: Boolean by lazy { isDataValid() }
}