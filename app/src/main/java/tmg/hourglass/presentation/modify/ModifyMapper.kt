package tmg.hourglass.presentation.modify

import android.util.Log
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Year
import java.time.temporal.ChronoUnit
import tmg.hourglass.domain.enums.CountdownInterpolator.LINEAR
import tmg.hourglass.domain.enums.CountdownType
import tmg.hourglass.domain.model.Countdown
import tmg.hourglass.domain.model.Countdown.Companion.MM_DD_FORMAT
import tmg.hourglass.domain.model.Countdown.Companion.YYYY_MM_DD_FORMAT
import tmg.hourglass.domain.model.CountdownNotifications
import tmg.hourglass.domain.model.Tag
import tmg.hourglass.utils.DateUtils
import tmg.utilities.extensions.extend

object ModifyMapper {

    fun Countdown.toUiState(): UiState {
        val inputTypes = when (countdownType) {
            CountdownType.DAYS -> UiState.Types.EndDate(
                startDate = startDate,
                day = endDate.dayOfMonth.toString(),
                month = endDate.month,
                year = endDate.year.toString().takeIf { this is Countdown.Static } ?: "",
            )
            else -> UiState.Types.Values(
                valueDirection = when {
                    startValue == "0" -> UiState.Direction.CountUp
                    endValue == "0" -> UiState.Direction.CountDown
                    else -> UiState.Direction.Custom
                },
                startDate = startDate,
                endDate = endDate,
                startValue = startValue,
                endValue = endValue
            )
        }
        val uiNotifications = notifications.mapNotNull { notification ->
            when (notification) {
                is CountdownNotifications.AtValue -> UiNotification(
                    id = notification.id,
                    type = NotificationType.VALUE,
                    value = notification.value
                )
                is CountdownNotifications.AtTime -> {
                    val daysBefore = ChronoUnit.DAYS.between(
                        notification.time.toLocalDate(),
                        endDate.toLocalDate()
                    ).coerceAtLeast(0)
                    UiNotification(
                        id = notification.id,
                        type = NotificationType.TIME,
                        value = daysBefore.toString()
                    )
                }
                else -> null
            }
        }.toMutableList()

        if (uiNotifications.isEmpty() || uiNotifications.all { it.value.isNotBlank() }) {
            uiNotifications.add(UiNotification())
        }

        return UiState(
            title = this.name,
            description = this.description,
            colorHex = this.colour,
            type = this.countdownType,
            inputTypes = inputTypes,
            allTags = emptyList(),
            tag = this.tag,
            notifications = uiNotifications
        )
    }

    @Throws(IllegalStateException::class)
    fun UiState.toCountdown(id: String): Countdown {
        when (inputTypes) {
            is UiState.Types.EndDate -> {
                Log.d("Modify", "Saving EndDate UI state (year=${inputTypes.year}, month=${inputTypes.month}, day=${inputTypes.day})")
                val dayInt = inputTypes.day?.trim()?.toIntOrNull() ?: 1
                val monthVal = inputTypes.month ?: java.time.Month.JANUARY

                val endDate: LocalDateTime = if (inputTypes.year.isNullOrBlank()) {
                    val string = "${Year.now().value}-${monthVal.value.extend(2, '0')}-${dayInt.extend(2, '0')}"
                    val date = LocalDate.parse(string, YYYY_MM_DD_FORMAT)
                    if (date < LocalDate.now()) {
                        date.plusYears(1L).atStartOfDay()
                    } else {
                        date.atStartOfDay()
                    }
                } else {
                    val yearInt = inputTypes.year.trim().toIntOrNull() ?: Year.now().value
                    LocalDate.of(yearInt, monthVal, dayInt).atStartOfDay()
                }

                val endDateNotifications: List<CountdownNotifications> = notifications
                    .filter { it.value.isNotBlank() }
                    .map { notification ->
                        val daysBefore = notification.value.trim().toLongOrNull() ?: 0L
                        val notificationTime = endDate.minusDays(daysBefore)
                        CountdownNotifications.AtTime(
                            id = notification.id,
                            time = notificationTime
                        )
                    }

                if (inputTypes.year.isNullOrBlank()) {
                    return Countdown.Recurring(
                        id = id,
                        name = title,
                        description = description,
                        colour = colorHex,
                        day = dayInt,
                        month = monthVal,
                        tag = this.tag,
                        notifications = endDateNotifications
                    )
                } else {
                    val startDate = when (inputTypes.startDate < endDate) {
                        true -> inputTypes.startDate
                        false -> LocalDate.now().atStartOfDay()
                    }
                    val start = DateUtils.daysBetween(startDate, endDate).toString()
                    val end = "0"
                    return Countdown.Static(
                        id = id,
                        name = title,
                        description = description,
                        colour = colorHex,
                        start = startDate.format(YYYY_MM_DD_FORMAT),
                        end = endDate.format(YYYY_MM_DD_FORMAT),
                        startValue = start,
                        endValue = end,
                        countdownType = type,
                        tag = this.tag,
                        notifications = endDateNotifications
                    )
                }
            }
            is UiState.Types.Values -> {
                Log.d("Modify", "Saving Values UI state (endDate=${inputTypes.endDate})")
                val valuesNotifications: List<CountdownNotifications> = notifications
                    .filter { it.value.isNotBlank() }
                    .map { notification ->
                        CountdownNotifications.AtValue(
                            id = notification.id,
                            value = notification.value.trim()
                        )
                    }

                val startDate = inputTypes.startDate ?: LocalDateTime.now()
                val endDate = inputTypes.endDate ?: LocalDateTime.now()
                val start = inputTypes.startValue.trim().takeIf { it.toIntOrNull() != null }?.ifBlank { "0" } ?: "0"
                val end = inputTypes.endValue.trim().takeIf { it.toIntOrNull() != null }?.ifBlank { "0" } ?: "0"
                return Countdown.Static(
                    id = id,
                    name = title,
                    description = description,
                    colour = colorHex,
                    start = startDate.format(YYYY_MM_DD_FORMAT),
                    end = endDate.format(YYYY_MM_DD_FORMAT),
                    startValue = start,
                    endValue = end,
                    countdownType = type,
                    tag = this.tag,
                    notifications = valuesNotifications
                )
            }
        }
    }
}