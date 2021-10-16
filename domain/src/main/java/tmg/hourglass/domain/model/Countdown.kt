package tmg.hourglass.domain.model

import org.threeten.bp.LocalDateTime
import tmg.hourglass.domain.enums.CountdownInterpolator
import tmg.hourglass.domain.enums.CountdownType

data class Countdown(
    val id: String,
    val name: String,
    val description: String,
    val colour: String,

    val start: LocalDateTime,
    val end: LocalDateTime,
    val initial: String,
    val finishing: String,

    val countdownType: CountdownType,
    val interpolator: CountdownInterpolator
) {

    val startByType: LocalDateTime
        get() = when (countdownType) {
            CountdownType.DAYS -> start.toLocalDate().atTime(23, 59, 59)
            else -> start
        }

    val endByType: LocalDateTime
        get() = when (countdownType) {
            CountdownType.DAYS -> end.toLocalDate().atTime(23, 59, 59)
            else -> end
        }
}