package tmg.hourglass.extensions

import android.view.animation.*
import tmg.hourglass.R
import tmg.hourglass.data.CountdownInterpolator

fun CountdownInterpolator.label(): Int {
    return when (this) {
        CountdownInterpolator.LINEAR -> R.string.modify_field_interpolator_linear
        CountdownInterpolator.ACCELERATE -> R.string.modify_field_interpolator_accelerate
        CountdownInterpolator.DECELERATE -> R.string.modify_field_interpolator_decelerate
        CountdownInterpolator.ACCELERATE_DECELERATE -> R.string.modify_field_interpolator_accelerate_decelerate
    }
}

fun CountdownInterpolator.valueInterpolator(): Interpolator {
    return when (this) {
        CountdownInterpolator.ACCELERATE -> AccelerateInterpolator()
        CountdownInterpolator.DECELERATE -> DecelerateInterpolator()
        CountdownInterpolator.LINEAR -> LinearInterpolator()
        CountdownInterpolator.ACCELERATE_DECELERATE -> AccelerateDecelerateInterpolator()
    }

}
