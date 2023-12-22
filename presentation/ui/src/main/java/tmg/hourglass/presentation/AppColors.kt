package tmg.hourglass.presentation

import androidx.compose.material.Colors
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

internal val LocalColors = staticCompositionLocalOf { lightColors }

data class AppColors(
    val primary: Color,
    val primaryDark: Color,
    val primaryAlpha: Color,
    val accent: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textPrimaryInverse: Color,
    val textSecondaryInverse: Color,
    val backgroundPrimary: Color,
    val backgroundSecondary: Color,
    val backgroundPrimaryInverse: Color,
    val backgroundSecondaryInverse: Color,
    val backgroundNav: Color,
    val inputBackground: Color,
    val isLight: Boolean
) {
    val appColors: Colors = Colors(
        primary = primary,
        primaryVariant = primary,
        secondary = accent,
        secondaryVariant = accent,
        background = backgroundPrimary,
        surface = backgroundSecondary,
        error = Color(0xFFCC0000),
        onPrimary = Color(0xFFF8F8F8),
        onSecondary = Color(0xFFF8F8F8),
        onBackground = textPrimary,
        onSurface = textSecondary,
        onError = Color(0xFFF8F8F8),
        isLight = isLight
    )
}

val lightColors = AppColors(
    primary = Color(0xFFAD1457),
    primaryDark = Color(0xFFFFFFFF),
    primaryAlpha = Color(0x44AD1457),
    accent = Color(0xFFAD1457),
    textPrimary = Color(0xFF181818),
    textSecondary = Color(0xFF383838),
    textPrimaryInverse = Color(0xFFFBFBFB),
    textSecondaryInverse = Color(0xFFE8E8E8),
    backgroundPrimary = Color(0xFFFFFFFF),
    backgroundSecondary = Color(0xFFF0F0F0),
    backgroundPrimaryInverse = Color(0xFF181818),
    backgroundSecondaryInverse = Color(0xFF383838),
    backgroundNav = Color(0xFFFCFCFC),
    inputBackground = Color(0xFFE8E8E8),
    isLight = true
)

val darkColors = AppColors(
    primary = Color(0xFFAD1457),
    primaryDark = Color(0xFF181818),
    primaryAlpha = Color(0x44AD1457),
    accent = Color(0xFFAD1457),
    textPrimary = Color(0xFFFBFBFB),
    textSecondary = Color(0xFFE8E8E8),
    textPrimaryInverse = Color(0xFF181818),
    textSecondaryInverse = Color(0xFF383838),
    backgroundPrimary = Color(0xFF181818),
    backgroundSecondary = Color(0xFF383838),
    backgroundPrimaryInverse = Color(0xFFFFFFFF),
    backgroundSecondaryInverse = Color(0xFFF8F8F8),
    backgroundNav = Color(0xFF383838),
    inputBackground = Color(0xFF282828),
    isLight = false
)