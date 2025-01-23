package tmg.hourglass.presentation.modify

import org.threeten.bp.LocalDate
import tmg.hourglass.domain.enums.CountdownColors
import tmg.hourglass.domain.enums.CountdownInterpolator
import tmg.hourglass.domain.enums.CountdownType
import tmg.hourglass.domain.model.Countdown

object ModifyData {

    val today = LocalDate.now().atStartOfDay()
    val tomorrow = LocalDate.now().atStartOfDay().plusDays(1L)

    val uiStateDaysEmpty = UiState(
        title = "",
        description = "",
        colorHex = CountdownColors.COLOUR_1.hex,
        type = CountdownType.DAYS,
        inputTypes = UiState.Types.EndDate(
            finishDate = null
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
            finishDate = null,
            initial = "",
            finishing = ""
        )
    )

    val countdownDays = Countdown(
        id = "1",
        name = "name",
        description = "desc",
        colour = "#123456",
        start = today,
        end = tomorrow,
        initial = "1",
        finishing = "0",
        countdownType = CountdownType.DAYS,
        interpolator = CountdownInterpolator.LINEAR,
        notifications = emptyList()
    )
    val uiStateDays = UiState(
        title = "name",
        description = "desc",
        colorHex = "#123456",
        type = CountdownType.DAYS,
        inputTypes = UiState.Types.EndDate(
            finishDate = tomorrow
        )
    )

    val countdownNumber = Countdown(
        id = "2",
        name = "name",
        description = "desc",
        colour = "#123456",
        start = today,
        end = tomorrow,
        initial = "0",
        finishing = "100",
        countdownType = CountdownType.NUMBER,
        interpolator = CountdownInterpolator.LINEAR,
        notifications = emptyList()
    )
    val uiStateNumber = UiState(
        title = "name",
        description = "desc",
        colorHex = "#123456",
        type = CountdownType.NUMBER,
        inputTypes = UiState.Types.Values(
            valueDirection = UiState.Direction.CountUp,
            startDate = today,
            finishDate = tomorrow,
            initial = "0",
            finishing = "100"
        )
    )
}