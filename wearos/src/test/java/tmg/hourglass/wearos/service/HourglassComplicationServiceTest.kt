package tmg.hourglass.wearos.service

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class HourglassComplicationServiceTest {

    @Test
    fun `formatComplicationTitle returns full title when length is 7 or less`() {
        assertEquals("Short", HourglassComplicationService.formatComplicationTitle("Short"))
        assertEquals("7Chars!", HourglassComplicationService.formatComplicationTitle("7Chars!"))
    }

    @Test
    fun `formatComplicationTitle breaks at space within first 7 characters`() {
        assertEquals("New", HourglassComplicationService.formatComplicationTitle("New Year"))
        assertEquals("My App", HourglassComplicationService.formatComplicationTitle("My App Title"))
        assertEquals("A B C", HourglassComplicationService.formatComplicationTitle("A B C D E F"))
    }

    @Test
    fun `formatComplicationTitle truncates at 7 characters when no space exists in first 7 chars`() {
        assertEquals("Christm", HourglassComplicationService.formatComplicationTitle("Christmas"))
        assertEquals("Superca", HourglassComplicationService.formatComplicationTitle("Supercalifragilistic"))
    }
}
