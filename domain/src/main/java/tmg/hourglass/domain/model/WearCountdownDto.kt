package tmg.hourglass.domain.model

import kotlinx.serialization.Serializable
import tmg.hourglass.domain.enums.CountdownType
import tmg.hourglass.domain.model.Countdown.Companion.YYYY_MM_DD_FORMAT
import java.time.Month

@Serializable
data class WearCountdownDto(
    val id: String,
    val name: String,
    val description: String,
    val colour: String,
    val start: String,
    val end: String,
    val startValue: String,
    val endValue: String,
    val countdownType: String,
    val isRecurring: Boolean,
    val day: Int = 1,
    val month: Int = 1,
    val emoji: String? = null
)

fun Countdown.toWearDto(): WearCountdownDto {
    return when (this) {
        is Countdown.Static -> WearCountdownDto(
            id = id,
            name = name,
            description = description,
            colour = colour,
            start = startDate.format(YYYY_MM_DD_FORMAT),
            end = endDate.format(YYYY_MM_DD_FORMAT),
            startValue = startValue,
            endValue = endValue,
            countdownType = countdownType.key,
            isRecurring = false,
            emoji = emoji
        )
        is Countdown.Recurring -> WearCountdownDto(
            id = id,
            name = name,
            description = description,
            colour = colour,
            start = startDate.format(YYYY_MM_DD_FORMAT),
            end = endDate.format(YYYY_MM_DD_FORMAT),
            startValue = startValue,
            endValue = endValue,
            countdownType = countdownType.key,
            isRecurring = true,
            day = endDate.dayOfMonth,
            month = endDate.monthValue,
            emoji = emoji
        )
    }
}

fun WearCountdownDto.toCountdown(): Countdown {
    return if (isRecurring) {
        Countdown.Recurring(
            id = id,
            name = name,
            description = description,
            colour = colour,
            emoji = emoji,
            day = day,
            month = try { Month.of(month) } catch (e: Exception) { Month.JANUARY },
            tag = null
        )
    } else {
        val mappedType = CountdownType.entries.firstOrNull { it.key == countdownType } ?: CountdownType.NUMBER
        Countdown.Static(
            id = id,
            name = name,
            description = description,
            colour = colour,
            emoji = emoji,
            start = start,
            end = end,
            startValue = startValue,
            endValue = endValue,
            countdownType = mappedType,
            tag = null
        )
    }
}
