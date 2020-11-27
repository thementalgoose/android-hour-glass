package tmg.hourglass.widget

import android.content.res.ColorStateList
import android.widget.RemoteViews
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import tmg.hourglass.BuildConfig
import tmg.hourglass.R
import java.lang.reflect.Method

/**
 * Attempt to set the progress bar color
 * Uses reflection, highly likely to fail but it's a nice to have anyway
 */
fun RemoteViews.setProgressBarColor(@IdRes progressBar: Int, @ColorInt color: Int) {
    var setTintMethod: Method? = null
    try {
        setTintMethod = this::class.java.getMethod(
            "setProgressTintList",
            Int::class.javaPrimitiveType,
            ColorStateList::class.java
        )
    } catch (e: Exception) {  // SecurityException, NoSuchMethodException
        if (BuildConfig.DEBUG) {
            e.printStackTrace()
        }
        /* Do nothing */
    }
    if (setTintMethod != null) {
        try {
            setTintMethod.invoke(
                this,
                progressBar,
                ColorStateList.valueOf(color)
            )
        } catch (e: Exception) { // IllegalAccessException, InvocationTargetException
            if (BuildConfig.DEBUG) {
                e.printStackTrace()
            }
            /* Do nothing */
        }
    }
}

/**
 * Due to limitations with RemoteViews and widget style updates we are showing
 *  progress by setting images at percentage points. Replace this with a circle
 *  icon
 */
enum class WidgetSquareProgress(
    @DrawableRes
    val res: Int
) {
    PROGRESS_1(R.drawable.widget_square_01),
    PROGRESS_2(R.drawable.widget_square_02),
    PROGRESS_3(R.drawable.widget_square_03),
    PROGRESS_4(R.drawable.widget_square_04),
    PROGRESS_5(R.drawable.widget_square_05),
    PROGRESS_6(R.drawable.widget_square_06),
    PROGRESS_7(R.drawable.widget_square_07),
    PROGRESS_8(R.drawable.widget_square_08),
    PROGRESS_9(R.drawable.widget_square_09),
    PROGRESS_10(R.drawable.widget_square_10),
    PROGRESS_11(R.drawable.widget_square_11),
    PROGRESS_12(R.drawable.widget_square_12),
    PROGRESS_13(R.drawable.widget_square_13),
    PROGRESS_14(R.drawable.widget_square_14),
    PROGRESS_15(R.drawable.widget_square_15),
    PROGRESS_16(R.drawable.widget_square_16),
    PROGRESS_17(R.drawable.widget_square_17),
    PROGRESS_18(R.drawable.widget_square_18),
    PROGRESS_19(R.drawable.widget_square_19),
    PROGRESS_20(R.drawable.widget_square_20);

    companion object {
        fun getImageFor(progress: Float): WidgetSquareProgress {
            val index =
                (progress * (values().size.toFloat())).toInt().coerceIn(0, values().size - 1)
            return values()[index]
        }
    }
}

enum class WidgetCircleProgress(
    @DrawableRes
    val res: Int
) {
    PROGRESS_1(R.drawable.widget_circle_01),
    PROGRESS_2(R.drawable.widget_circle_02),
    PROGRESS_3(R.drawable.widget_circle_03),
    PROGRESS_4(R.drawable.widget_circle_04),
    PROGRESS_5(R.drawable.widget_circle_05),
    PROGRESS_6(R.drawable.widget_circle_06),
    PROGRESS_7(R.drawable.widget_circle_07),
    PROGRESS_8(R.drawable.widget_circle_08),
    PROGRESS_9(R.drawable.widget_circle_09),
    PROGRESS_10(R.drawable.widget_circle_10),
    PROGRESS_11(R.drawable.widget_circle_11),
    PROGRESS_12(R.drawable.widget_circle_12),
    PROGRESS_13(R.drawable.widget_circle_13),
    PROGRESS_14(R.drawable.widget_circle_14),
    PROGRESS_15(R.drawable.widget_circle_15),
    PROGRESS_16(R.drawable.widget_circle_16);

    companion object {
        fun getImageFor(progress: Float): WidgetCircleProgress {
            val index =
                (progress * (values().size.toFloat())).toInt().coerceIn(0, values().size - 1)
            return values()[index]
        }
    }
}