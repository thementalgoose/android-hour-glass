package tmg.hourglass.presentation.modify

import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import tmg.hourglass.domain.enums.CountdownInterpolator.LINEAR
import tmg.hourglass.domain.enums.CountdownType
import tmg.hourglass.domain.model.Countdown
import tmg.hourglass.utils.DateUtils

object ModifyMapper {

    fun Countdown.toUiState(): UiState {
        val inputTypes = when (countdownType) {
            CountdownType.DAYS,
            CountdownType.SECONDS -> UiState.Types.EndDate(
                finishDate = end,
            )
            else -> UiState.Types.Values(
                valueDirection = when {
                    initial == "0" -> UiState.Direction.CountUp
                    finishing == "0" -> UiState.Direction.CountDown
                    else -> UiState.Direction.Custom
                },
                startDate = start,
                finishDate = end,
                initial = initial,
                finishing = finishing
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
                val startDate = LocalDate.now().atStartOfDay()
                val endDate = inputTypes.finishDate!!.toLocalDate().atStartOfDay()
                val start = DateUtils.daysBetween(startDate, endDate).toString()
                val end = "0"
                return Countdown(
                    id = id,
                    name = title,
                    description = description,
                    colour = colorHex,
                    start = startDate,
                    end = endDate,
                    initial = start,
                    finishing = end,
                    countdownType = type,
                    interpolator = LINEAR,
                    notifications = emptyList()
                )
            }
            is UiState.Types.Values -> {
                val startDate = inputTypes.startDate ?: LocalDateTime.now()
                val endDate = inputTypes.finishDate ?: LocalDateTime.now()
                val start = inputTypes.initial.ifBlank { "0" }
                val end = inputTypes.finishing.ifBlank { "0" }
                return Countdown(
                    id = id,
                    name = title,
                    description = description,
                    colour = colorHex,
                    start = startDate,
                    end = endDate,
                    initial = start,
                    finishing = end,
                    countdownType = type,
                    interpolator = LINEAR,
                    notifications = emptyList()
                )
            }
        }
    }
}