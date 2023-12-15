package tmg.hourglass.extensions

import android.view.animation.*
import tmg.hourglass.strings.R.string
import tmg.hourglass.domain.enums.CountdownInterpolator

fun CountdownInterpolator.label(): Int {
    return when (this) {
        CountdownInterpolator.LINEAR -> string.modify_field_interpolator_linear
        CountdownInterpolator.ACCELERATE -> string.modify_field_interpolator_accelerate
        CountdownInterpolator.DECELERATE -> string.modify_field_interpolator_decelerate
        CountdownInterpolator.ACCELERATE_DECELERATE -> string.modify_field_interpolator_accelerate_decelerate
    }
}

fun CountdownInterpolator.valueInterpolator(interpolator: Interpolator? = null): Interpolator {
    if (interpolator != null) {
        return interpolator
    }
    return when (this) {
        CountdownInterpolator.ACCELERATE -> AccelerateInterpolator()
        CountdownInterpolator.DECELERATE -> DecelerateInterpolator()
        CountdownInterpolator.LINEAR -> LinearInterpolator()
        CountdownInterpolator.ACCELERATE_DECELERATE -> AccelerateDecelerateInterpolator()
    }
}
