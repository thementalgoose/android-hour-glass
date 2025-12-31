package tmg.hourglass.room.mappers

import android.util.Log
import tmg.hourglass.domain.enums.CountdownType
import tmg.hourglass.domain.model.Countdown
import tmg.hourglass.domain.model.Countdown.Companion.MM_DD_FORMAT
import tmg.hourglass.domain.model.Countdown.Companion.YYYY_MM_DD_FORMAT
import tmg.utilities.extensions.toEnum
import java.time.Month
import javax.inject.Inject

internal class CountdownMapper @Inject constructor() {

    private val Countdown.startLabel: String
        get() = when (this) {
            is Countdown.Recurring -> this.startDate.toLocalDate().format(YYYY_MM_DD_FORMAT)
            is Countdown.Static -> this.startDate.toLocalDate().format(YYYY_MM_DD_FORMAT)
        }

    private val Countdown.endLabel: String
        get() = when (this) {
            is Countdown.Recurring -> this.endDate.toLocalDate().format(MM_DD_FORMAT)
            is Countdown.Static -> this.endDate.toLocalDate().format(YYYY_MM_DD_FORMAT)
        }

    fun serialize(model: Countdown): tmg.hourglass.room.models.Countdown {
        Log.d("CountdownMapper", "Serializing countdown $model (startLabel=${model.startLabel}, endLabel=${model.endLabel})")
        return tmg.hourglass.room.models.Countdown(
            id = model.id,
            name = model.name,
            description = model.description,
            colour = model.colour,
            start = model.startLabel,
            end = model.endLabel,
            initial = model.startValue,
            finishing = model.endValue,
            isRecurring = model is Countdown.Recurring,
            passageType = model.countdownType.key,
            interpolator = model.interpolator.key
        )
    }

    fun deserialize(model: tmg.hourglass.room.models.Countdown): Countdown {
        Log.d("CountdownMapper", "Deserializing countdown $model")
        return if (model.isRecurring) {
            Countdown.Recurring(
                id = model.id,
                name = model.name,
                description = model.description,
                colour = model.colour,
                day = model.end.split("-")[1].toIntOrNull() ?: 31,
                month = Month.of(model.end.split("-")[0].toIntOrNull() ?: 12)
            )
        } else {
            Countdown.Static(
                id = model.id,
                name = model.name,
                description = model.description,
                colour = model.colour,
                start = model.start,
                end = model.end,
                startValue = model.initial,
                endValue = model.finishing,
                countdownType = model.passageType.toEnum<CountdownType> { it.key }
                    ?: CountdownType.NUMBER
            )
        }
    }
}