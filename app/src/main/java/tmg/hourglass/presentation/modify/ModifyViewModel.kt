package tmg.hourglass.presentation.modify

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import tmg.hourglass.BuildConfig
import tmg.hourglass.core.crashlytics.AnalyticsManager
import java.time.LocalDate
import java.time.LocalDateTime
import tmg.hourglass.core.googleanalytics.CrashReporter
import tmg.hourglass.domain.repositories.CountdownRepository
import tmg.hourglass.domain.enums.CountdownColors
import tmg.hourglass.domain.enums.CountdownType
import tmg.hourglass.domain.model.Countdown
import tmg.hourglass.domain.model.Tag
import tmg.hourglass.domain.repositories.PreferencesManager
import tmg.hourglass.domain.repositories.TagRepository
import tmg.hourglass.navigation.Tags
import tmg.hourglass.presentation.modify.ModifyMapper.toCountdown
import tmg.hourglass.presentation.modify.ModifyMapper.toUiState
import tmg.hourglass.presentation.modify.UiState.Direction.CountDown
import tmg.hourglass.presentation.modify.UiState.Direction.CountUp
import android.content.Context
import androidx.core.app.NotificationManagerCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import tmg.hourglass.domain.usecases.CancelNotificationsUseCase
import tmg.hourglass.domain.usecases.ScheduleNotificationsUseCase
import tmg.hourglass.presentation.modify.UiState.Direction.CountDown
import tmg.hourglass.presentation.modify.UiState.Direction.CountUp
import tmg.hourglass.presentation.modify.UiState.Direction.Custom
import java.time.Month
import java.time.Year
import java.util.UUID
import javax.inject.Inject

import tmg.hourglass.utils.DateUtils

enum class NotificationType {
    VALUE,
    TIME
}

sealed class NotificationError {
    data class DaysOutOfRange(val maxDays: Long): NotificationError()
    data class ValueOutOfRange(val minVal: Int, val maxVal: Int): NotificationError()
}

data class UiNotification(
    val id: String = UUID.randomUUID().toString(),
    val type: NotificationType = NotificationType.VALUE,
    val value: String = ""
)

@HiltViewModel
class ModifyViewModel @Inject constructor(
    private val countdownRepository: CountdownRepository,
    tagRepository: TagRepository,
    private val scheduleNotificationsUseCase: ScheduleNotificationsUseCase,
    private val cancelNotificationsUseCase: CancelNotificationsUseCase,
    private val crashReporter: CrashReporter,
    private val analyticsManager: AnalyticsManager,
    @param:ApplicationContext private val context: Context
): ViewModel() {
    private val allTags: Flow<List<Tag>> = tagRepository.getAll()
    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(getUiState())
    val uiState: StateFlow<UiState> =
        combine(
            flow = _uiState,
            flow2 = allTags
        ) { uiState, tags ->
            uiState.copy(allTags = tags)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = _uiState.value
        )

    private var id: String? = null

    private fun getUiState(): UiState {
        val enabled = NotificationManagerCompat.from(context).areNotificationsEnabled()
        return UiState(
            title = "",
            description = "",
            colorHex = CountdownColors.COLOUR_1.hex,
            type = CountdownType.DAYS,
            inputTypes = UiState.Types.EndDate(
                day = null,
                month = null,
                year = "${Year.now().value}"
            ),
            allTags = emptyList(),
            tag = null,
            notifications = listOf(UiNotification()),
            notificationsEnabled = enabled
        )
    }

    fun initialise(id: String?) {
        val enabled = NotificationManagerCompat.from(context).areNotificationsEnabled()
        if (id == null) {
            this.id = null
            _uiState.value = getUiState().copy(notificationsEnabled = enabled)
        } else {
            countdownRepository.getSync(id)?.let {
                this.id = id
                _uiState.value = it.toUiState().copy(notificationsEnabled = enabled)
            }
        }
    }

    fun refreshNotificationsEnabled() {
        val enabled = NotificationManagerCompat.from(context).areNotificationsEnabled()
        _uiState.value = _uiState.value.copy(notificationsEnabled = enabled)
    }

    fun updateNotificationValue(id: String, value: String) {
        val currentNotifications = _uiState.value.notifications.toMutableList()
        val index = currentNotifications.indexOfFirst { it.id == id }
        if (index != -1) {
            currentNotifications[index] = currentNotifications[index].copy(value = value)
            val nonBlank = currentNotifications.filter { it.value.isNotBlank() }.toMutableList()
            nonBlank.add(UiNotification())
            _uiState.value = _uiState.value.copy(notifications = nonBlank)
        }
    }

    fun updateNotificationType(id: String, type: NotificationType) {
        val currentNotifications = _uiState.value.notifications.toMutableList()
        val index = currentNotifications.indexOfFirst { it.id == id }
        if (index != -1) {
            currentNotifications[index] = currentNotifications[index].copy(type = type)
            _uiState.value = _uiState.value.copy(notifications = currentNotifications)
        }
    }

    fun deleteNotification(id: String) {
        val currentNotifications = _uiState.value.notifications.filterNot { it.id == id }
        val nonBlank = currentNotifications.filter { it.value.isNotBlank() }.toMutableList()
        nonBlank.add(UiNotification())
        _uiState.value = _uiState.value.copy(notifications = nonBlank)
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
                UiState.Types.EndDate(
                    day = null,
                    month = null,
                    year = null
                )
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
            },
            notifications = listOf(UiNotification())
        )
    }
    fun setStartDate(date: LocalDateTime) {
        val existingType = _uiState.value.inputTypes
        when (existingType) {
            is UiState.Types.EndDate -> {
                _uiState.value = _uiState.value.copy(
                    inputTypes = existingType.copy(
                        startDate = date
                    )
                )
            }
            is UiState.Types.Values -> {
                _uiState.value = _uiState.value.copy(
                    inputTypes = existingType.copy(
                        startDate = date,
                        endDate = null
                    )
                )
            }
        }
    }
    fun setEndDateDay(day: String) {
        val existingType = _uiState.value.inputTypes
        if (existingType is UiState.Types.EndDate) {
            _uiState.value = _uiState.value.copy(
                inputTypes = existingType.copy(
                    day = day
                )
            )
        }
    }
    fun setEndDateMonth(month: Month) {
        val existingType = _uiState.value.inputTypes
        if (existingType is UiState.Types.EndDate) {
            _uiState.value = _uiState.value.copy(
                inputTypes = existingType.copy(
                    month = month
                )
            )
        }
    }
    fun setEndDateYear(year: String) {
        val existingType = _uiState.value.inputTypes
        if (existingType is UiState.Types.EndDate) {
            _uiState.value = _uiState.value.copy(
                inputTypes = existingType.copy(
                    year = year
                )
            )
        }
    }
    fun setEndDate(date: LocalDateTime) {
        val existingType = _uiState.value.inputTypes
        if (existingType is UiState.Types.Values) {
            _uiState.value = _uiState.value.copy(
                inputTypes = existingType.copy(
                    endDate = date
                )
            )
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

    fun setTag(tag: Tag?) {
        val existing = _uiState.value.tag
        Log.d("Modify", "Setting tag to $tag")
        if (existing == tag && existing != null) {
            _uiState.value = _uiState.value.copy(tag = null)
        } else {
            _uiState.value = _uiState.value.copy(tag = tag)
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

            val saveId = id ?: UUID.randomUUID().toString()
            val countdown = uiState.toCountdown(saveId)
            Log.d("Modify", "Saving countdown $countdown")
            viewModelScope.launch {
                if (id != null) {
                    cancelNotificationsUseCase(saveId)
                }
                countdownRepository.saveSync(countdown)
                scheduleNotificationsUseCase(countdown.id)
            }

            val key = when (id == null) {
                true -> "countdown_add"
                false -> "countdown_modify"
            }
            val label = countdown.countdownType.key
            analyticsManager.event(key, mapOf(
                "type" to label
            ))

            id = null
        } catch (e: NullPointerException) {
            crashReporter.logException(e)
        }
    }

    fun delete() {
        id?.let {
            analyticsManager.event("countdown_remove")
            viewModelScope.launch {
                cancelNotificationsUseCase(it)
            }
            countdownRepository.delete(it)
        }
    }
}

data class UiState(
    val title: String,
    val description: String,
    val colorHex: String,
    val type: CountdownType,
    val inputTypes: Types,
    val allTags: List<Tag>,
    val tag: Tag?,
    val notifications: List<UiNotification> = listOf(UiNotification()),
    val notificationsEnabled: Boolean = false
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
            val day: String?,
            val month: Month?,
            val year: String?
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
        FINISH_DATE_INVALID,
        FINISH_DATE_IN_PAST,
        VALUES_EMPTY,
        VALUES_MATCH,
        VALUES_MUST_BE_NUMBER,
        START_DATE_NULL,
        START_DATE_IN_FUTURE,
        FINISH_DATE_BEFORE_START_DATE,
        NOTIFICATION_OUT_OF_RANGE
    }

    fun getNotificationError(notification: UiNotification): NotificationError? {
        if (notification.value.isBlank()) return null
        return when (inputTypes) {
            is Types.EndDate -> {
                val dayInt = inputTypes.day?.trim()?.toIntOrNull() ?: return null
                val monthVal = inputTypes.month ?: return null
                val endDate = try {
                    if (inputTypes.year.isNullOrBlank()) {
                        val date = LocalDate.of(Year.now().value, monthVal, dayInt)
                        if (date < LocalDate.now()) date.plusYears(1L).atStartOfDay() else date.atStartOfDay()
                    } else {
                        val yearInt = inputTypes.year.trim().toIntOrNull() ?: return null
                        LocalDate.of(yearInt, monthVal, dayInt).atStartOfDay()
                    }
                } catch (_: Exception) {
                    return null
                }
                val startDate = inputTypes.startDate
                val totalDays = DateUtils.daysBetween(startDate, endDate).toLong()
                val daysBefore = notification.value.trim().toLongOrNull()
                if (daysBefore == null || daysBefore < 0 || daysBefore > totalDays) {
                    NotificationError.DaysOutOfRange(maxDays = totalDays)
                } else null
            }
            is Types.Values -> {
                val startVal = inputTypes.startValue.trim().toIntOrNull() ?: return null
                val endVal = inputTypes.endValue.trim().toIntOrNull() ?: return null
                val minVal = minOf(startVal, endVal)
                val maxVal = maxOf(startVal, endVal)
                val numVal = notification.value.trim().toIntOrNull()
                if (numVal == null || numVal < minVal || numVal > maxVal) {
                    NotificationError.ValueOutOfRange(minVal = minVal, maxVal = maxVal)
                } else null
            }
        }
    }

    private fun isDataValid(): List<ErrorTypes> {
        return buildList {
            if (title.isBlank()) {
                add(ErrorTypes.TITLE_BLANK)
            }
            if (colorHex.isBlank()) {
                add(ErrorTypes.COLOUR_BLANK)
            }
            val hasNotificationError = notifications
                .filter { it.value.isNotBlank() }
                .any { getNotificationError(it) != null }
            if (hasNotificationError) {
                add(ErrorTypes.NOTIFICATION_OUT_OF_RANGE)
            }
            when (inputTypes) {
                is Types.EndDate -> {

                    if (inputTypes.startDate.toLocalDate() > LocalDate.now()) {
                        add(ErrorTypes.START_DATE_IN_FUTURE)
                        return@buildList
                    }

                    if (inputTypes.day == null || inputTypes.month == null) {
                        add(ErrorTypes.FINISH_DATE_NULL)
                        return@buildList
                    }

                    val day = inputTypes.day.trim().toIntOrNull() ?: return listOf(
                        ErrorTypes.FINISH_DATE_INVALID
                    )

                    if (inputTypes.year.isNullOrBlank()) {
                        try {
                            val localDate = LocalDate.of(Year.now().value, inputTypes.month, day)
                            if (localDate < inputTypes.startDate.toLocalDate()) {
                                add(ErrorTypes.FINISH_DATE_BEFORE_START_DATE)
                                return@buildList
                            }
                        } catch (_: Exception) {
                            add(ErrorTypes.FINISH_DATE_INVALID)
                            return@buildList
                        }
                    } else {
                        val year = inputTypes.year.trim().toIntOrNull() ?: return listOf(
                            ErrorTypes.FINISH_DATE_INVALID
                        )
                        try {
                            val localDate = LocalDate.of(year, inputTypes.month, day)
                            if (localDate < inputTypes.startDate.toLocalDate()) {
                                add(ErrorTypes.FINISH_DATE_BEFORE_START_DATE)
                                return@buildList
                            }
                            if (localDate < LocalDate.now()) {
                                add(ErrorTypes.FINISH_DATE_IN_PAST)
                                return@buildList
                            }
                        } catch (_: Exception) {
                            add(ErrorTypes.FINISH_DATE_INVALID)
                            return@buildList
                        }
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
                    if (inputTypes.startValue.toIntOrNull() == null || inputTypes.endValue.toIntOrNull() == null) {
                        add(ErrorTypes.VALUES_MUST_BE_NUMBER)
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