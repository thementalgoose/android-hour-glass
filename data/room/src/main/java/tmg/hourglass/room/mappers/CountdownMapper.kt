package tmg.hourglass.room.mappers

import tmg.hourglass.domain.enums.CountdownInterpolator
import tmg.hourglass.domain.enums.CountdownType
import tmg.hourglass.domain.model.Countdown
import tmg.utilities.extensions.toEnum
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.inject.Inject

internal class CountdownMapper @Inject constructor() {
    fun serialize(model: Countdown) = tmg.hourglass.room.models.Countdown(
        id = model.id,
        name = model.name,
        description = model.description,
        colour = model.colour,
        start = model.start.toInstant(ZoneOffset.UTC).toEpochMilli(),
        end = model.end.toInstant(ZoneOffset.UTC).toEpochMilli(),
        initial = model.startValue,
        finishing = model.endValue,
        passageType = model.countdownType.key,
        interpolator = model.interpolator.key
    )

    fun deserialize(model: tmg.hourglass.room.models.Countdown) = Countdown(
        id = model.id,
        name = model.name,
        description = model.description,
        colour = model.colour,
        start = model.start.toLocalDateTime(),
        end = model.end.toLocalDateTime(),
        startValue = model.initial,
        endValue = model.finishing,
        countdownType = model.passageType.toEnum<CountdownType> { it.key } ?: CountdownType.NUMBER,
        interpolator = model.interpolator.toEnum<CountdownInterpolator> { it.key } ?: CountdownInterpolator.LINEAR,
        notifications = emptyList()
    )

    private fun Long.toLocalDateTime(): LocalDateTime {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(this), ZoneOffset.UTC)
    }
}