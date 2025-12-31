package tmg.hourglass.domain.model

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import tmg.hourglass.domain.enums.CountdownInterpolator.LINEAR
import tmg.hourglass.domain.enums.CountdownType.DAYS
import tmg.hourglass.domain.enums.CountdownType.NUMBER
import tmg.hourglass.domain.model


internal class CountdownTest {

    @Test
    fun `Countdown startAtStartOfDay and endAtStartOfDay returns start of day value`() {
        val countdown = Countdown.Static.model(
            id = "",
            name = "",
            description = "",
            colour = "",
            start = "2020-01-01",
            end = "2020-01-02",
            startValue = "0",
            endValue = "1",
            countdownType = DAYS
        )

        val expectedStart = LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0)
        val expectedEnd = LocalDateTime.of(2020, 1, 2, 0, 0, 0, 0)
        assertEquals(expectedStart, countdown.startAtStartOfDay)
        assertEquals(expectedEnd, countdown.endAtStartOfDay)
    }
}