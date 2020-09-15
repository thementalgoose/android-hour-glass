package tmg.hourglass.utils

import android.view.animation.Interpolator
import org.threeten.bp.LocalDateTime
import tmg.hourglass.data.CountdownInterpolator
import tmg.hourglass.extensions.millis
import tmg.hourglass.extensions.valueInterpolator

class ProgressUtils {
    companion object {
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