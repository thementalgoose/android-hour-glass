package tmg.hourglass.presentation.modify

import java.time.LocalDate
import java.time.LocalDateTime
import tmg.hourglass.domain.enums.CountdownInterpolator.LINEAR
import tmg.hourglass.domain.enums.CountdownType
import tmg.hourglass.domain.model.Countdown
import tmg.hourglass.utils.DateUtils

object ModifyMapper {

    fun Countdown.toUiState(): UiState {
        val inputTypes = when (countdownType) {
            CountdownType.DAYS -> UiState.Types.EndDate(
                startDate = start,
                finishDate = end,
            )
            else -> UiState.Types.Values(
                valueDirection = when {
                    startValue == "0" -> UiState.Direction.CountUp
                    endValue == "0" -> UiState.Direction.CountDown
                    else -> UiState.Direction.Custom
                },
                startDate = start,
                endDate = end,
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
                val endDate = inputTypes.finishDate!!.toLocalDate().atStartOfDay()
                val startDate = when (inputTypes.startDate < inputTypes.finishDate) {
                    true -> inputTypes.startDate
                    false -> LocalDate.now().atStartOfDay()
                }
                val start = DateUtils.daysBetween(startDate, endDate).toString()
                val end = "0"
                return Countdown(
                    id = id,
                    name = title,
                    description = description,
                    colour = colorHex,
                    start = startDate,
                    end = endDate,
                    startValue = start,
                    endValue = end,
                    countdownType = type,
                    interpolator = LINEAR,
                    notifications = emptyList()
                )
            }
            is UiState.Types.Values -> {
                val startDate = inputTypes.startDate ?: LocalDateTime.now()
                val endDate = inputTypes.endDate ?: LocalDateTime.now()
                val start = inputTypes.startValue.trim().takeIf { it.toIntOrNull() != null }?.ifBlank { "0" } ?: "0"
                val end = inputTypes.endValue.trim().takeIf { it.toIntOrNull() != null }?.ifBlank { "0" } ?: "0"
                return Countdown(
                    id = id,
                    name = title,
                    description = description,
                    colour = colorHex,
                    start = startDate,
                    end = endDate,
                    startValue = start,
                    endValue = end,
                    countdownType = type,
                    interpolator = LINEAR,
                    notifications = emptyList()
                )
            }
        }
    }
}