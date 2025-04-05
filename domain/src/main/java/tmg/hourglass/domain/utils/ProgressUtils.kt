package tmg.hourglass.domain.utils

import android.view.animation.Interpolator
import java.time.LocalDateTime
import java.time.ZoneOffset
import tmg.hourglass.domain.enums.CountdownInterpolator
import tmg.hourglass.domain.extensions.valueInterpolator
import tmg.hourglass.domain.model.Countdown

class ProgressUtils {
    companion object {

        fun getProgress(countdown: Countdown, current: LocalDateTime = LocalDateTime.now()): Float {
            return getProgress(
                start = countdown.startAtStartOfDay,
                end = countdown.endAtStartOfDay,
                current = current,
                interpolator = countdown.interpolator
            )
        }

        fun getProgress(start: LocalDateTime, end: LocalDateTime, current: LocalDateTime = LocalDateTime.now(), interpolator: CountdownInterpolator, mockInterpolator: Interpolator? = null): Float {
            val startMillis: Long = start.millis
            val endMillis: Long = end.millis
            val currentMillis: Long = current.millis

            val valueInterpolator = interpolator.valueInterpolator(mockInterpolator)

            if (currentMillis >= endMillis) return 1.0f
            if (currentMillis <= startMillis) return 0.0f

            val progress = ((currentMillis - startMillis).toDouble() / (endMillis - startMillis).toDouble()).toFloat()
            if (progress >= 1.0f) return 1.0f
            if (progress <= 0.0f) return 0.0f

            return valueInterpolator.getInterpolation(progress)
        }


        internal val LocalDateTime.millis: Long
            get() = this.toInstant(ZoneOffset.UTC).toEpochMilli()
    }
}