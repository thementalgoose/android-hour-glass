package tmg.hourglass.presentation.modify

import java.time.LocalDate
import java.time.LocalDateTime
import tmg.hourglass.domain.enums.CountdownInterpolator.LINEAR
import tmg.hourglass.domain.enums.CountdownType
import tmg.hourglass.domain.model.Countdown
import tmg.hourglass.domain.model.Countdown.Companion.MM_DD_FORMAT
import tmg.hourglass.domain.model.Countdown.Companion.YYYY_MM_DD_FORMAT
import tmg.hourglass.utils.DateUtils
import tmg.utilities.extensions.extend
import java.time.Year

object ModifyMapper {

    fun Countdown.toUiState(): UiState {
        val inputTypes = when (countdownType) {
            CountdownType.DAYS -> UiState.Types.EndDate(
                startDate = startDate,
                day = endDate.dayOfMonth,
                month = endDate.month,
                year = endDate.year.takeIf { this is Countdown.Static },
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
        return UiState(
            title = this.name,
            description = this.description,
            colorHex = this.colour,
            type = this.countdownType,
            inputTypes = inputTypes
        )
    }

    @Throws(IllegalStateException::class)
    fun UiState.toCountdown(id: String): Countdown {
        when (inputTypes) {
            is UiState.Types.EndDate -> {
                if (inputTypes.year == null) {
                    return Countdown.Recurring(
                        id = id,
                        name = title,
                        description = description,
                        colour = colorHex,
                        day = inputTypes.day!!,
                        month = inputTypes.month!!
                    )
                } else {
                    val endDate = LocalDate.of(inputTypes.year, inputTypes.month, inputTypes.day!!).atStartOfDay()
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
                        countdownType = type
                    )
                }
            }
            is UiState.Types.Values -> {
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
                    countdownType = type
                )
            }
        }
    }
}