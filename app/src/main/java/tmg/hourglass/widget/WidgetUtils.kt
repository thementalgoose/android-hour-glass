package tmg.hourglass.widget

import android.content.res.ColorStateList
import android.widget.RemoteViews
import androidx.annotation.ColorInt
import androidx.annotation.IdRes
import tmg.hourglass.BuildConfig
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