package tmg.hourglass.presentation.modify

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import tmg.hourglass.BuildConfig
import java.time.LocalDate
import java.time.LocalDateTime
import tmg.hourglass.core.googleanalytics.CrashReporter
import tmg.hourglass.domain.repositories.CountdownRepository
import tmg.hourglass.domain.enums.CountdownColors
import tmg.hourglass.domain.enums.CountdownType
import tmg.hourglass.domain.repositories.RealmRepo
import tmg.hourglass.presentation.modify.ModifyMapper.toCountdown
import tmg.hourglass.presentation.modify.ModifyMapper.toUiState
import tmg.hourglass.presentation.modify.UiState.Direction.CountDown
import tmg.hourglass.presentation.modify.UiState.Direction.CountUp
import tmg.hourglass.presentation.modify.UiState.Direction.Custom
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ModifyViewModel @Inject constructor(
    @param:RealmRepo
    private val countdownRepository: CountdownRepository,
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
            countdownRepository.getSync(id)?.let {
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
            CountdownType.DAYS -> {
                UiState.Types.EndDate(finishDate = null)
            }
            else -> {
                UiState.Types.Values(
                    valueDirection = CountDown,
                    startDate = null,
                    startValue = "",
                    endDate = null,
                    endValue = "",
                )
            }
        }
        _uiState.value = _uiState.value.copy(
            type = type,
            inputTypes = when (newType::class == existingType::class) {
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
                    endDate = null
                )
            )
        }
    }
    fun setEndDate(date: LocalDateTime) {
        when (val existingType = _uiState.value.inputTypes) {
            is UiState.Types.Values -> {
                _uiState.value = _uiState.value.copy(
                    inputTypes = existingType.copy(
                        endDate = date
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
                    startValue = existingType.startValue.takeIf { direction == CountUp || direction == Custom } ?: "",
                    endValue = existingType.endValue.takeIf { direction == CountUp || direction == Custom } ?: ""
                )
            )
        }
    }
    fun setStartValue(value: String) {
        val existingType = _uiState.value.inputTypes
        if (existingType is UiState.Types.Values) {
            _uiState.value = _uiState.value.copy(
                inputTypes = existingType.copy(
                    startValue = value
                )
            )
        }
    }
    fun setEndValue(value: String) {
        val existingType = _uiState.value.inputTypes
        if (existingType is UiState.Types.Values) {
            _uiState.value = _uiState.value.copy(
                inputTypes = existingType.copy(
                    endValue = value
                )
            )
        }
    }

    fun save() {
        try {
            val uiState = _uiState.value

            if (!uiState.isSaveEnabled) {
                crashReporter.logException(IllegalStateException("Save clicked while data is considered invalid. Model = $uiState"))
                return
            }

            val countdown = uiState.toCountdown(id ?: UUID.randomUUID().toString())
            countdownRepository.saveSync(countdown)
        } catch (e: NullPointerException) {
            crashReporter.logException(e)
        }
    }

    fun delete() {
        id?.let {
            countdownRepository.delete(it)
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

    val errors: List<ErrorTypes> by lazy {
        val list = isDataValid()
        if (BuildConfig.DEBUG) {
            Log.d("Modify", "Error list:\n - ${list.joinToString(separator = "\n - ") { it.name }}")
        }
        return@lazy list
    }
    val isSaveEnabled: Boolean by lazy {
        errors.isEmpty()
    }

    sealed class Types {
        data class EndDate(
            val startDate: LocalDateTime = LocalDate.now().atStartOfDay(),
            val finishDate: LocalDateTime?
        ): Types()
        data class Values(
            val valueDirection: Direction,
            val startDate: LocalDateTime?,
            val endDate: LocalDateTime?,
            val startValue: String,
            val endValue: String,
        ): Types()
    }

    enum class Direction {
        CountUp,
        CountDown,
        Custom
    }

    enum class ErrorTypes {
        TITLE_BLANK,
        COLOUR_BLANK,
        FINISH_DATE_NULL,
        FINISH_DATE_IN_PAST,
        VALUES_EMPTY,
        VALUES_MATCH,
        START_DATE_NULL,
        FINISH_DATE_BEFORE_START_DATE
    }

    private fun isDataValid(): List<ErrorTypes> {
        return buildList {
            if (title.isBlank()) {
                add(ErrorTypes.TITLE_BLANK)
            }
            if (colorHex.isBlank()) {
                add(ErrorTypes.COLOUR_BLANK)
            }
            when (inputTypes) {
                is Types.EndDate -> {
                    if (inputTypes.finishDate == null) {
                        add(ErrorTypes.FINISH_DATE_NULL)
                        return@buildList
                    }
                    if (inputTypes.finishDate.toLocalDate() < LocalDate.now()) {
                        add(ErrorTypes.FINISH_DATE_IN_PAST)
                    }
                    return@buildList
                }
                is Types.Values -> {
                    val hasInitialData = inputTypes.startValue.isNotBlank() && inputTypes.startValue != "0"
                    val hasFinishingData = inputTypes.endValue.isNotBlank() && inputTypes.endValue != "0"

                    if (!hasInitialData && !hasFinishingData) {
                        add(ErrorTypes.VALUES_EMPTY)
                    }
                    if (inputTypes.startValue == inputTypes.endValue) {
                        add(ErrorTypes.VALUES_MATCH)
                    }

                    if (inputTypes.startDate == null) {
                        add(ErrorTypes.START_DATE_NULL)
                        return@buildList
                    }
                    if (inputTypes.endDate == null) {
                        add(ErrorTypes.FINISH_DATE_NULL)
                        return@buildList
                    }
                    if (inputTypes.endDate.toLocalDate() < LocalDate.now()) {
                        add(ErrorTypes.FINISH_DATE_IN_PAST)
                    }
                    if (inputTypes.endDate.toLocalDate() <= inputTypes.startDate.toLocalDate()) {
                        add(ErrorTypes.FINISH_DATE_BEFORE_START_DATE)
                    }
                    return@buildList
                }
            }
        }
    }
}