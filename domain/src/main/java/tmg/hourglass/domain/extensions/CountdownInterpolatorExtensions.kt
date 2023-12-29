package tmg.hourglass.domain.extensions

import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import tmg.hourglass.domain.enums.CountdownInterpolator
import tmg.hourglass.strings.R.string

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
