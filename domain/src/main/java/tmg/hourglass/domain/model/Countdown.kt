package tmg.hourglass.domain.model

import org.threeten.bp.LocalDateTime
import tmg.hourglass.domain.enums.CountdownInterpolator
import tmg.hourglass.domain.enums.CountdownType
import kotlin.math.floor

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

    fun getProgress(progress: Float): String {
        val start: Int = initial.toIntOrNull() ?: 0
        val end: Int = finishing.toIntOrNull() ?: 100

        return floor((start + (progress * (end - start)))).toInt().toString()
    }

    companion object
}

fun Countdown.Companion.preview(
    color: String = "#152793",
): Countdown {
    return Countdown(
        id = "countdown",
        name = "Countdown Item",
        description = "Generic Lorum Ipsum content here",
        colour = color,
        start = LocalDateTime
            .of(2022, 1, 23, 0, 0),
        end = LocalDateTime
            .of(2022, 2, 5, 0, 0),
        initial = "0",
        finishing = "1000",
        countdownType = CountdownType.DAYS,
        interpolator = CountdownInterpolator.LINEAR
    )
}