package tmg.hourglass.realm.mappers

import android.util.Log
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import tmg.hourglass.domain.enums.CountdownInterpolator
import tmg.hourglass.domain.enums.CountdownType
import tmg.hourglass.domain.model.Countdown
import tmg.hourglass.realm.models.RealmCountdown
import tmg.utilities.extensions.toEnum
import javax.inject.Inject

class RealmCountdownMapper @Inject constructor() {

    fun deserialize(input: RealmCountdown): Countdown {
        Log.i("Mapper", "Deserializing ${input.id} (${input.name})")
        return Countdown.Static(
            id = input.id,
            name = input.name,
            description = input.description,
            colour = input.colour,
            start = input.start.toString(),
            end = input.end.toString(),
            startValue = input.initial,
            endValue = input.finishing,
            countdownType = input.passageType.toEnum<CountdownType> { it.key } ?: CountdownType.NUMBER
        )
    }
}