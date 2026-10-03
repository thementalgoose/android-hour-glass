package tmg.hourglass.notifications

import android.content.res.Resources
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.EnumSource
import tmg.hourglass.strings.R

class AtTimeSubtitleFormatterTest {

    private val resources: Resources = mockk(relaxed = true)
    private lateinit var formatter: AtTimeSubtitleFormatter

    @BeforeEach
    fun setUp() {
        formatter = AtTimeSubtitleFormatter()
    }

    enum class SpecialCaseTestParam(
        val days: Int,
        val resId: Int,
        val expectedText: String
    ) {
        TODAY(0, R.string.notification_subtitle_today, "Today"),
        TOMORROW(1, R.string.notification_subtitle_tomorrow, "Tomorrow")
    }

    @ParameterizedTest(name = "format with {0} days remaining returns special string resource")
    @EnumSource(SpecialCaseTestParam::class)
    fun `format with special cases returns expected string resource`(param: SpecialCaseTestParam) {
        every { resources.getString(param.resId) } returns param.expectedText

        val result = formatter.format(resources, param.days)

        assertEquals(param.expectedText, result)
        verify { resources.getString(param.resId) }
    }

    enum class PluralCaseTestParam(
        val days: Int,
        val pluralId: Int,
        val count: Int,
        val expectedText: String
    ) {
        ONE_WEEK(7, R.plurals.notification_subtitle_weeks_to_go, 1, "1 week to go"),
        TWO_WEEKS(14, R.plurals.notification_subtitle_weeks_to_go, 2, "2 weeks to go"),
        THREE_DAYS(3, R.plurals.notification_subtitle_days_to_go, 3, "3 days to go")
    }

    @ParameterizedTest(name = "format with {0} days remaining returns plural string")
    @EnumSource(PluralCaseTestParam::class)
    fun `format with plural days remaining returns expected plural string`(param: PluralCaseTestParam) {
        every { resources.getQuantityString(param.pluralId, param.count, param.count) } returns param.expectedText

        val result = formatter.format(resources, param.days)

        assertEquals(param.expectedText, result)
        verify { resources.getQuantityString(param.pluralId, param.count, param.count) }
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
