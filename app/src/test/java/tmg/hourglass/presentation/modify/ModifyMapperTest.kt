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
    fun `uiState DAYS toCountdown converts notification value into AtTime notification`() {
        val endDate = ModifyData.tomorrow
        val uiState = ModifyData.uiStateDays.copy(
            notifications = listOf(UiNotification(id = "n1", value = "3"))
        )
        val countdown = uiState.toCountdown("1")
        assertEquals(1, countdown.notifications.size)
        val notification = countdown.notifications[0] as tmg.hourglass.domain.model.CountdownNotifications.AtTime
        assertEquals("n1", notification.id)
        assertEquals(endDate.minusDays(3), notification.time)
    }

    @Test
    fun `countdown DAYS toUiState converts AtTime notification into daysBefore string`() {
        val endDate = ModifyData.tomorrow
        val notification = tmg.hourglass.domain.model.CountdownNotifications.AtTime(
            id = "n1",
            time = endDate.minusDays(3)
        )
        val countdown = ModifyData.countdownDays.copy(notifications = listOf(notification))
        val uiState = countdown.toUiState()

        val matching = uiState.notifications.firstOrNull { it.id == "n1" }
        assertEquals("3", matching?.value)
        assertEquals(NotificationType.TIME, matching?.type)
    }

    @Test
    fun `emoji maps between Countdown and UiState`() {
        val countdown = countdownDays.copy(emoji = "🚀")
        val uiState = countdown.toUiState()
        assertEquals("🚀", uiState.emoji)
        val restored = uiState.toCountdown("1")
        assertEquals("🚀", restored.emoji)
    }
}