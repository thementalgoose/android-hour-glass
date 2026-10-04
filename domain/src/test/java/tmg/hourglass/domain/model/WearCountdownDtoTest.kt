package tmg.hourglass.domain.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import tmg.hourglass.domain.enums.CountdownType
import java.time.LocalDateTime
import java.time.Month

class WearCountdownDtoTest {

    @Test
    fun `toWearDto and toCountdown convert Static countdown accurately`() {
        val countdown = Countdown.Static(
            id = "static_1",
            name = "Vacation",
            description = "Trip to Hawaii",
            colour = "#FF0000",
            emoji = "🏖️",
            start = "2026-01-01",
            end = "2026-06-01",
            startValue = "0",
            endValue = "100",
            countdownType = CountdownType.MONEY_GBP,
            tag = null
        )

        val dto = countdown.toWearDto()

        assertEquals("static_1", dto.id)
        assertEquals("Vacation", dto.name)
        assertEquals("🏖️", dto.emoji)
        assertFalse(dto.isRecurring)

        val restored = dto.toCountdown() as Countdown.Static

        assertEquals("static_1", restored.id)
        assertEquals("Vacation", restored.name)
        assertEquals("Trip to Hawaii", restored.description)
        assertEquals("#FF0000", restored.colour)
        assertEquals("🏖️", restored.emoji)
    }

    @Test
    fun `toWearDto and toCountdown convert Recurring countdown accurately`() {
        val countdown = Countdown.Recurring(
            id = "recurring_1",
            name = "Birthday",
            description = "Annual event",
            colour = "#00FF00",
            emoji = "🎂",
            day = 15,
            month = Month.OCTOBER,
            tag = null
        )

        val dto = countdown.toWearDto()

        assertEquals("recurring_1", dto.id)
        assertEquals("Birthday", dto.name)
        assertEquals("🎂", dto.emoji)
        assertTrue(dto.isRecurring)
        assertEquals(15, dto.day)
        assertEquals(10, dto.month)

        val restored = dto.toCountdown() as Countdown.Recurring

        assertEquals("recurring_1", restored.id)
        assertEquals("Birthday", restored.name)
        assertEquals("🎂", restored.emoji)
    }
}
