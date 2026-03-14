package tmg.hourglass.presentation.date

import tmg.utilities.extensions.format
import tmg.utilities.extensions.ordinalAbbreviation
import java.time.LocalDate

fun LocalDate.displayDate(): String {
    val ordinal = this.dayOfMonth.ordinalAbbreviation
    return this.format("'${ordinal}' MMM yyyy") ?: "${this.dayOfWeek} ${this.monthValue} ${this.year}"
}