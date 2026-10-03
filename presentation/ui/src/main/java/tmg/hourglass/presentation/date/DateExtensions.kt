package tmg.hourglass.presentation.date

import tmg.utilities.extensions.format
import tmg.utilities.extensions.ordinalAbbreviation
import java.time.LocalDate

fun LocalDate.displayDate(
    includeYear: Boolean = true
): String {
    val ordinal = this.dayOfMonth.ordinalAbbreviation
    return when (includeYear) {
        true -> this.format("'${ordinal}' MMM yyyy") ?: "${this.dayOfWeek} ${this.monthValue} ${this.year}"
        false -> this.format("'${ordinal}' MMM") ?: "${this.dayOfWeek} ${this.monthValue}"
    }
}