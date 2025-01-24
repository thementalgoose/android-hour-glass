package tmg.hourglass.domain.model

import org.threeten.bp.LocalDateTime
import tmg.hourglass.domain.enums.CountdownInterpolator
import tmg.hourglass.domain.enums.CountdownType
import kotlin.math.ceil

data class Countdown(
    val id: String,
    val name: String,
    val description: String,
    val colour: String,

    val start: LocalDateTime,
    val end: LocalDateTime,
    val startValue: String,
    val endValue: String,

    val countdownType: CountdownType,
    val interpolator: CountdownInterpolator,
    val notifications: List<CountdownNotifications>
) {

    val isFinished: Boolean
        get() = end <= LocalDateTime.now()

    val startAtStartOfDay: LocalDateTime
        get() = start.toLocalDate().atTime(0, 0, 0, 0)

    val endAtStartOfDay: LocalDateTime
        get() = end.toLocalDate().atTime(0, 0, 0)

    fun getProgress(progress: Float): String {
        val start: Int = startValue.toIntOrNull() ?: 0
        val end: Int = endValue.toIntOrNull() ?: 100

        return countdownType.converter(ceil((start + (progress * (end - start)))).toInt().toString())
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
        start = LocalDateTime.now().minusDays(1L),
        end = LocalDateTime.now().plusDays(2L),
        startValue = "0",
        endValue = "1000",
        countdownType = CountdownType.DAYS,
        interpolator = CountdownInterpolator.LINEAR,
        notifications = emptyList()
    )
}