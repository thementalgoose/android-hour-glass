package tmg.hourglass.notifications

import android.content.res.Resources
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.hourglass.strings.R

class AtTimeSubtitleFormatterTest {

    private val resources: Resources = mockk(relaxed = true)
    private lateinit var formatter: AtTimeSubtitleFormatter

    @BeforeEach
    fun setUp() {
        formatter = AtTimeSubtitleFormatter()
    }

    @Test
    fun `format with 0 days remaining returns Today string resource`() {
        every { resources.getString(R.string.notification_subtitle_today) } returns "Today"

        val result = formatter.format(resources, 0)

        assertEquals("Today", result)
        verify { resources.getString(R.string.notification_subtitle_today) }
    }

    @Test
    fun `format with 1 day remaining returns Tomorrow string resource`() {
        every { resources.getString(R.string.notification_subtitle_tomorrow) } returns "Tomorrow"

        val result = formatter.format(resources, 1)

        assertEquals("Tomorrow", result)
        verify { resources.getString(R.string.notification_subtitle_tomorrow) }
    }

    @Test
    fun `format with 7 days remaining returns 1 week to go plural`() {
        every { resources.getQuantityString(R.plurals.notification_subtitle_weeks_to_go, 1, 1) } returns "1 week to go"

        val result = formatter.format(resources, 7)

        assertEquals("1 week to go", result)
        verify { resources.getQuantityString(R.plurals.notification_subtitle_weeks_to_go, 1, 1) }
    }

    @Test
    fun `format with 14 days remaining returns 2 weeks to go plural`() {
        every { resources.getQuantityString(R.plurals.notification_subtitle_weeks_to_go, 2, 2) } returns "2 weeks to go"

        val result = formatter.format(resources, 14)

        assertEquals("2 weeks to go", result)
        verify { resources.getQuantityString(R.plurals.notification_subtitle_weeks_to_go, 2, 2) }
    }

    @Test
    fun `format with 3 days remaining returns 3 days to go plural`() {
        every { resources.getQuantityString(R.plurals.notification_subtitle_days_to_go, 3, 3) } returns "3 days to go"

        val result = formatter.format(resources, 3)

        assertEquals("3 days to go", result)
        verify { resources.getQuantityString(R.plurals.notification_subtitle_days_to_go, 3, 3) }
    }

    @Test
    fun `format with custom config respects custom special cases and custom multiplier`() {
        val customConfig = FormatterConfig(
            weeksMultiple = 5,
            specialCases = mapOf(2 to R.string.notification_subtitle_tomorrow)
        )
        val customFormatter = AtTimeSubtitleFormatter(customConfig)

        every { resources.getString(R.string.notification_subtitle_tomorrow) } returns "Custom Special"
        every { resources.getQuantityString(R.plurals.notification_subtitle_weeks_to_go, 2, 2) } returns "2 custom weeks"

        val resultSpecial = customFormatter.format(resources, 2)
        val resultWeeks = customFormatter.format(resources, 10)

        assertEquals("Custom Special", resultSpecial)
        assertEquals("2 custom weeks", resultWeeks)
    }
}
