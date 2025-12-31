package tmg.hourglass.presentation.modify

import io.mockk.InternalPlatformDsl.toStr
import java.time.LocalDate
import tmg.hourglass.domain.enums.CountdownColors
import tmg.hourglass.domain.enums.CountdownInterpolator
import tmg.hourglass.domain.enums.CountdownType
import tmg.hourglass.domain.model
import tmg.hourglass.domain.model.Countdown
import tmg.hourglass.domain.model.Countdown.Companion.YYYY_MM_DD_FORMAT
import java.time.ZoneId

object ModifyData {
    val yesterday = LocalDate.now().atStartOfDay().minusDays(1L)
    val yesterdayString = yesterday.format(YYYY_MM_DD_FORMAT)
    val today = LocalDate.now().atStartOfDay()
    val todayString = today.format(YYYY_MM_DD_FORMAT)
    val tomorrow = LocalDate.now().atStartOfDay().plusDays(1L)
    val tomorrowString = tomorrow.format(YYYY_MM_DD_FORMAT)

    val uiStateDaysEmpty = UiState(
        title = "",
        description = "",
        colorHex = CountdownColors.COLOUR_1.hex,
        type = CountdownType.DAYS,
        inputTypes = UiState.Types.EndDate(
            day = null,
            month = null,
            year = null
        )
    )
    val uiStateNumberEmpty = UiState(
        title = "",
        description = "",
        colorHex = CountdownColors.COLOUR_1.hex,
        type = CountdownType.NUMBER,
        inputTypes = UiState.Types.Values(
            valueDirection = UiState.Direction.CountDown,
            startDate = null,
            endDate = null,
            startValue = "",
            endValue = ""
        )
    )

    val countdownDays = Countdown.Static.model(
        id = "1",
        name = "name",
        description = "desc",
        colour = "#123456",
        start = todayString,
        end = tomorrowString,
        startValue = "1",
        endValue = "0",
        countdownType = CountdownType.DAYS,
    )
    val uiStateDays = UiState(
        title = "name",
        description = "desc",
        colorHex = "#123456",
        type = CountdownType.DAYS,
        inputTypes = UiState.Types.EndDate(
            day = tomorrow.dayOfMonth.toString(),
            month = tomorrow.month,
            year = tomorrow.year.toString()
        )
    )

    val countdownNumber = Countdown.Static.model(
        id = "2",
        name = "name",
        description = "desc",
        colour = "#123456",
        start = todayString,
        end = tomorrowString,
        startValue = "0",
        endValue = "100",
        countdownType = CountdownType.NUMBER,
    )
    val uiStateNumber = UiState(
        title = "name",
        description = "desc",
        colorHex = "#123456",
        type = CountdownType.NUMBER,
        inputTypes = UiState.Types.Values(
            valueDirection = UiState.Direction.CountUp,
            startDate = today,
            endDate = tomorrow,
            startValue = "0",
            endValue = "100"
        )
    )
}