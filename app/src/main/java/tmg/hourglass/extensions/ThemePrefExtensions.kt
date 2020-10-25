package tmg.hourglass.extensions

import android.content.Context
import tmg.hourglass.R
import tmg.hourglass.prefs.ThemePref
import tmg.utilities.extensions.isInNightMode
import java.security.AccessControlContext

val ThemePref.title: Int
    get() = when (this) {
        ThemePref.AUTO -> R.string.theme_auto
        ThemePref.LIGHT -> R.string.theme_light
        ThemePref.DARK -> R.string.theme_dark
    }

fun ThemePref.getTheme(context: Context): Int = when (this) {
    ThemePref.AUTO -> if (context.isInNightMode()) R.style.DarkTheme else R.style.LightTheme
    ThemePref.LIGHT -> R.style.LightTheme
    ThemePref.DARK -> R.style.DarkTheme
}
