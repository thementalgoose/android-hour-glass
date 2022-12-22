package tmg.hourglass.realm.mappers

import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneOffset
import tmg.hourglass.domain.enums.CountdownInterpolator
import tmg.hourglass.domain.enums.CountdownType
import tmg.hourglass.domain.model.Countdown
import tmg.hourglass.realm.models.RealmCountdown
import tmg.utilities.extensions.toEnum
import javax.inject.Inject

class RealmCountdownMapper @Inject constructor() {

    fun deserialize(input: RealmCountdown): Countdown {
        return Countdown(
            id = input.id,
            name = input.name,
            description = input.description,
            colour = input.colour,
            start = input.start.toLocalDateTime(),
            end = input.end.toLocalDateTime(),
            initial = input.initial,
            finishing = input.finishing,
            countdownType = input.passageType.toEnum<CountdownType> { it.key } ?: CountdownType.NUMBER,
            interpolator = input.interpolator.toEnum<CountdownInterpolator> { it.key } ?: CountdownInterpolator.LINEAR
        )
    }

    fun serialize(model: RealmCountdown, data: Countdown) {
        model.name = data.name
        model.description = data.description
        model.colour = data.colour
        model.start = data.start.toInstant(ZoneOffset.UTC).toEpochMilli()
        model.end = data.end.toInstant(ZoneOffset.UTC).toEpochMilli()
        model.initial = data.initial
        model.finishing = data.finishing
        model.passageType = data.countdownType.key
        model.interpolator = data.interpolator.key
    }

    private fun Long.toLocalDateTime(): LocalDateTime {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(this), ZoneOffset.UTC)
    }
}