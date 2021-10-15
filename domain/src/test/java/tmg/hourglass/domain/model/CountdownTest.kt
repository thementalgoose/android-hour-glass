package tmg.hourglass.domain.model

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.threeten.bp.LocalDateTime
import tmg.hourglass.domain.enums.CountdownInterpolator.LINEAR
import tmg.hourglass.domain.enums.CountdownType.DAYS
import tmg.hourglass.domain.enums.CountdownType.NUMBER


internal class CountdownTest {

    @Test
    fun `Countdown startByType and endByType returns localdate at end of value when type is days`() {

        val start = LocalDateTime.of(2020, 1, 1, 1, 1)
        val end = LocalDateTime.of(2020, 1, 2, 1, 1)
        val countdown = Countdown(id = "", name = "", description = "", colour = "", start = start, end = end, initial = "0", finishing = "1", countdownType = DAYS, interpolator = LINEAR)

        val expectedStart = LocalDateTime.of(2020, 1, 1, 23, 59, 59)
        val expectedEnd = LocalDateTime.of(2020, 1, 2, 23, 59, 59)
        assertEquals(expectedStart, countdown.startByType)
        assertEquals(expectedEnd, countdown.endByType)
    }

    @Test
    fun `Countdown startByType and endByType returns regular value when type is not days`() {

        val start = LocalDateTime.of(2020, 1, 1, 1, 1)
        val end = LocalDateTime.of(2020, 1, 2, 1, 1)
        val countdown = Countdown(id = "", name = "", description = "", colour = "", start = start, end = end, initial = "0", finishing = "1", countdownType = NUMBER, interpolator = LINEAR)

        assertEquals(start, countdown.startByType)
        assertEquals(end, countdown.endByType)
    }
}