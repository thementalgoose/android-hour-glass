package tmg.hourglass.utils

import android.view.animation.Interpolator
import org.threeten.bp.LocalDateTime
import tmg.hourglass.domain.data.CountdownInterpolator
import tmg.hourglass.domain.data.models.Countdown
import tmg.hourglass.extensions.millis
import tmg.hourglass.extensions.valueInterpolator

class ProgressUtils {
    companion object {

        fun getProgress(countdownItem: Countdown, current: LocalDateTime = LocalDateTime.now(), mockInterpolator: Interpolator? = null): Float {
            return getProgress(
                start = countdownItem.start,
                end = countdownItem.end,
                current = current,
                interpolator = countdownItem.interpolator,
                mockInterpolator = mockInterpolator
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
    }
}