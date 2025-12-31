package tmg.hourglass.domain

import tmg.hourglass.domain.enums.CountdownInterpolator
import tmg.hourglass.domain.enums.CountdownType
import tmg.hourglass.domain.model.Countdown
import tmg.hourglass.domain.model.CountdownNotifications
import java.time.LocalDateTime
import java.time.Month
import kotlin.String

fun Countdown.Static.Companion.model(
    id: String = "countdownId",
    name: String = "name",
    description: String = "description",
    colour: String = "colour",

    start: String = "2025-01-01",
    end: String = "2025-01-10",
    startValue: String = "start",
    endValue: String = "end",

    countdownType: CountdownType = CountdownType.DAYS
): Countdown.Static = Countdown.Static(
    id = id,
    name = name,
    description = description,
    colour = colour,
    start = start,
    end = end,
    startValue = startValue,
    endValue = endValue,
    countdownType = countdownType
)

fun Countdown.Recurring.Companion.model(
    id: String = "countdownId",
    name: String = "name",
    description: String = "description",
    colour: String = "colour",
    day: Int = 1,
    month: Month = Month.JANUARY
): Countdown.Recurring = Countdown.Recurring(
    id = id,
    name = name,
    description = description,
    colour = colour,
    day = day,
    month = month
)