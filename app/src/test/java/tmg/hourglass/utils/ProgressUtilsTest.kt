package tmg.hourglass.utils

import android.view.animation.Interpolator
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import tmg.hourglass.data.CountdownInterpolator.LINEAR
import tmg.hourglass.extensions.millis
import tmg.hourglass.utils.ProgressUtils.Companion.getProgress

class ProgressUtilsTest {

    private var mockInterpolator: Interpolator = mockk(relaxed = true)

    @ParameterizedTest(name = "From {0} to {1}, current date is {2} meaning progress is {3}")
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

        val calculated = (((current.millis - start.millis).toFloat()) / (end.millis - start.millis).toFloat()).coerceIn(0.0f, 1.0f)

        every { mockInterpolator.getInterpolation(calculated) } returns expected

        assertEquals(expected, getProgress(start, end, current = current, interpolator = LINEAR, mockInterpolator = mockInterpolator))
    }
}