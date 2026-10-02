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
    fun `uiState DAYS toCountdown maps as expected`() {
        assertEquals(countdownDays, uiStateDays.toCountdown("1"))
    }

    @Test
    fun `uiState NON DAYS toCountdown maps as expected`() {
        assertEquals(countdownNumber, uiStateNumber.toCountdown("2"))
    }

    @Test
    fun `countdown DAYS toUiState maps as expected`() {
        val result = countdownDays.toUiState()
        assertEquals(uiStateDays.title, result.title)
        assertEquals(uiStateDays.description, result.description)
        assertEquals(uiStateDays.colorHex, result.colorHex)
        assertEquals(uiStateDays.type, result.type)
        assertEquals(uiStateDays.inputTypes, result.inputTypes)
        assertEquals(uiStateDays.notifications.map { it.value }, result.notifications.map { it.value })
    }

    @Test
    fun `countdown NON DAYS toUiState maps as expected`() {
        val result = countdownNumber.toUiState()
        assertEquals(uiStateNumber.title, result.title)
        assertEquals(uiStateNumber.description, result.description)
        assertEquals(uiStateNumber.colorHex, result.colorHex)
        assertEquals(uiStateNumber.type, result.type)
        assertEquals(uiStateNumber.inputTypes, result.inputTypes)
        assertEquals(uiStateNumber.notifications.map { it.value }, result.notifications.map { it.value })
    }
}