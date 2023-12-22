package tmg.hourglass.extensions

import tmg.hourglass.strings.R.string
import tmg.hourglass.presentation.ThemePref

val ThemePref.title: Int
    get() = when (this) {
        ThemePref.AUTO -> string.theme_auto
        ThemePref.LIGHT -> string.theme_light
        ThemePref.DARK -> string.theme_dark
    }