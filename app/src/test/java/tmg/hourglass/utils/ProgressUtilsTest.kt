package tmg.hourglass.utils

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import tmg.hourglass.utils.ProgressUtils.Companion.getProgress

class ProgressUtilsTest {

    @ParameterizedTest
    @CsvSource(
        "01/01/2000 00:00,02/01/2000 00:00,01/01/2000 12:00,0.5",
        "23/01/2000 00:00,25/01/2000 00:00,24/01/2000 00:00,0.5",
        "01/01/2000 00:00,02/01/2000 00:00,24/12/1999 12:00,0.0", // Under
        "01/01/2000 00:00,02/01/2000 00:00,01/01/2000 00:00,0.0", // At minimum
        "01/01/2000 00:00,02/01/2000 00:00,02/01/2000 00:00,1.0", // At max
        "01/01/2000 00:00,02/01/2000 00:00,02/01/2000 12:00,1.0"  // Over
    )
    fun `getProgress start time and end time checks returning the correct progress`(startText: String, endText: String, currentText: String, expected: Float) {
        val start = LocalDateTime.parse(startText, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
        val end = LocalDateTime.parse(endText, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
        val current = LocalDateTime.parse(currentText, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))

        assertEquals(expected, getProgress(start, end, current = current))
    }
}