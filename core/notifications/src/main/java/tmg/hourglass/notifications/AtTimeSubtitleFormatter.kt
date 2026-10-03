package tmg.hourglass.notifications

import android.content.res.Resources
import tmg.hourglass.strings.R
import javax.inject.Inject
import javax.inject.Singleton

data class FormatterConfig(
    val weeksMultiple: Int = 7,
    val specialCases: Map<Int, Int> = mapOf(
        0 to R.string.notification_subtitle_today,
        1 to R.string.notification_subtitle_tomorrow
    )
)

@Singleton
class AtTimeSubtitleFormatter(
    val config: FormatterConfig = FormatterConfig()
) {
    @Inject
    constructor() : this(FormatterConfig())

    fun format(resources: Resources, daysRemaining: Int): String {
        config.specialCases[daysRemaining]?.let { stringRes ->
            return resources.getString(stringRes)
        }

        if (daysRemaining > 0 && daysRemaining % config.weeksMultiple == 0) {
            val weeks = daysRemaining / config.weeksMultiple
            return resources.getQuantityString(
                R.plurals.notification_subtitle_weeks_to_go,
                weeks,
                weeks
            )
        }

        return resources.getQuantityString(
            R.plurals.notification_subtitle_days_to_go,
            daysRemaining,
            daysRemaining
        )
    }
}
