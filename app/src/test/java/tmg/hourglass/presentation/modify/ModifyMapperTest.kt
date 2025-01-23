package tmg.hourglass.presentation.modify

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.threeten.bp.LocalDate
import tmg.hourglass.domain.enums.CountdownInterpolator
import tmg.hourglass.domain.enums.CountdownType
import tmg.hourglass.domain.model.Countdown
import tmg.hourglass.presentation.modify.ModifyMapper.toCountdown
import tmg.hourglass.presentation.modify.ModifyMapper.toUiState

internal class ModifyMapperTest {

    private val today = LocalDate.now().atStartOfDay()
    private val tomorrow = LocalDate.now().atStartOfDay().plusDays(1L)

    private val countdown1 = Countdown(
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
    private val uiState1 = UiState(
        title = "name",
        description = "desc",
        colorHex = "#123456",
        type = CountdownType.DAYS,
        inputTypes = UiState.Types.EndDate(
            finishDate = tomorrow
        )
    )

    private val countdown2 = Countdown(
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
    private val uiState2 = UiState(
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

    @Test
    fun `countdown DAYS toUiState maps as expected`() {
        assertEquals(countdown1, uiState1.toCountdown("1"))
    }

    @Test
    fun `countdown NON DAYS toUiState maps as expected`() {
        assertEquals(countdown2, uiState2.toCountdown("2"))
    }

    @Test
    fun `uiState DAYS toCountdown maps as expected`() {
        assertEquals(uiState1, countdown1.toUiState())
    }

    @Test
    fun `uiState NON DAYS toCountdown maps as expected`() {
        assertEquals(uiState2, countdown2.toUiState())
    }
}