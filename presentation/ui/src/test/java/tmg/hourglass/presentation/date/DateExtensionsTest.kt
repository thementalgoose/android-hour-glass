package tmg.hourglass.presentation.date

import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import java.time.LocalDate

class DateExtensionsTest {

    @Test
    fun `displayDate formats date string correctly`() {
        val date = LocalDate.of(2026, 10, 3)
        val formatted = date.displayDate(includeYear = true)
        assertNotNull(formatted)
    }
}
