package tmg.hourglass.utils

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import java.time.LocalDateTime

internal class DateUtilsTest {

    @ParameterizedTest(name = "daysBetween from {0} to {1} returns {2} days")
    @CsvSource(
        "2025-01-01T23:59, 2025-01-02T00:01, 1",
        "2025-01-01T21:59, 2025-01-07T23:59, 6"
    )
    fun `calculates correct amount of days between start and end times`(
        startIso: String,
        endIso: String,
        expectedDays: Int
    ) {
        val start = LocalDateTime.parse(startIso)
        val end = LocalDateTime.parse(endIso)
        assertEquals(expectedDays, DateUtils.daysBetween(start, end))
    }
}