package tmg.hourglass.domain

import tmg.hourglass.domain.model.CountdownNotifications
import java.time.LocalDateTime

fun CountdownNotifications.Companion.model(
    id: String = "id",
    time: LocalDateTime = LocalDateTime.of(2025, 1, 1, 1, 1)
): CountdownNotifications.AtTime = CountdownNotifications.AtTime(
    _id = id,
    time = time
)

fun CountdownNotifications.Companion.model(
    id: String = "id",
    value: String = "value",
): CountdownNotifications.AtValue = CountdownNotifications.AtValue(
    _id = id,
    value = value
)