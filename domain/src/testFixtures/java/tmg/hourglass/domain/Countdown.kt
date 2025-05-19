package tmg.hourglass.domain

import tmg.hourglass.domain.enums.CountdownInterpolator
import tmg.hourglass.domain.enums.CountdownType
import tmg.hourglass.domain.model.Countdown
import tmg.hourglass.domain.model.CountdownNotifications
import java.time.LocalDateTime
import kotlin.String

fun Countdown.Companion.model(
    id: String = "countdownId",
    name: String = "name",
    description: String = "description",
    colour: String = "colour",

    start: LocalDateTime = LocalDateTime.of(2025, 1, 1, 1, 1),
    end: LocalDateTime = LocalDateTime.of(2025, 1, 10, 1, 1),
    startValue: String = "start",
    endValue: String = "end",

    countdownType: CountdownType = CountdownType.DAYS,
    interpolator: CountdownInterpolator = CountdownInterpolator.LINEAR,
    notifications: List<CountdownNotifications> = emptyList<CountdownNotifications>()
): Countdown = Countdown(
    id = id,
    name = name,
    description = description,
    colour = colour,
    start = start,
    end = end,
    startValue = startValue,
    endValue = endValue,
    countdownType = countdownType,
    interpolator = interpolator,
    notifications = notifications,
)