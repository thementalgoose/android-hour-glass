package tmg.hourglass.utils

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.threeten.bp.LocalDateTime

internal class DateUtilsTest {

    @Test
    fun `calculates correct amount of days when end time is before start time`() {
        val start = LocalDateTime.of(2025, 1, 1, 23, 59)
        val end = LocalDateTime.of(2025, 1, 2, 0, 1)
        assertEquals(1, DateUtils.daysBetween(start, end))
    }

    @Test
    fun `calculates correct amount of days when end time is after start time`() {
        val start = LocalDateTime.of(2025, 1, 1, 21, 59)
        val end = LocalDateTime.of(2025, 1, 7, 23, 59)
        assertEquals(6, DateUtils.daysBetween(start, end))
    }
}