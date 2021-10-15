package tmg.hourglass.extensions

import tmg.hourglass.R
import tmg.hourglass.prefs.ThemePref

val ThemePref.title: Int
    get() = when (this) {
        ThemePref.AUTO -> R.string.theme_auto
        ThemePref.LIGHT -> R.string.theme_light
        ThemePref.DARK -> R.string.theme_dark
    }