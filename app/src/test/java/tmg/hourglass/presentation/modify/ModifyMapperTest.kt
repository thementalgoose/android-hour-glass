package tmg.hourglass.presentation.modify

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tmg.hourglass.presentation.modify.ModifyData.countdownDays
import tmg.hourglass.presentation.modify.ModifyData.countdownNumber
import tmg.hourglass.presentation.modify.ModifyData.uiStateDays
import tmg.hourglass.presentation.modify.ModifyData.uiStateNumber
import tmg.hourglass.presentation.modify.ModifyMapper.toCountdown
import tmg.hourglass.presentation.modify.ModifyMapper.toUiState

internal class ModifyMapperTest {

    @Test
    fun `countdown DAYS toUiState maps as expected`() {
        assertEquals(countdownDays, uiStateDays.toCountdown("1"))
    }

    @Test
    fun `countdown NON DAYS toUiState maps as expected`() {
        assertEquals(countdownNumber, uiStateNumber.toCountdown("2"))
    }

    @Test
    fun `uiState DAYS toCountdown maps as expected`() {
        assertEquals(uiStateDays, countdownDays.toUiState())
    }

    @Test
    fun `uiState NON DAYS toCountdown maps as expected`() {
        assertEquals(uiStateNumber, countdownNumber.toUiState())
    }
}