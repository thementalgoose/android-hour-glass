package tmg.hourglass.presentation.utils

import androidx.annotation.FloatRange
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.util.fastCoerceIn

fun Color.darken(@FloatRange(from = 0.0, to = 1.0) multiplier: Float = 0.99f): Color {
    val multiplier = multiplier.fastCoerceIn(0f, 1f)
    return this.copy(
        red = this.red * multiplier,
        green = this.green * multiplier,
        blue = this.blue * multiplier
    )
}