package tmg.hourglass.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material.Colors
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

internal val LocalColors = staticCompositionLocalOf { lightColors }

data class AppColors(
    val primary: Color,
    val primaryDark: Color,
    val primaryAlpha: Color,
    val accent: Color,
    val onAccent: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textPrimaryInverse: Color,
    val textSecondaryInverse: Color,
    val systemStatusBarColor: Color,
    val systemNavigationBarColor: Color,
    val backgroundContainer: Color,
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
    onAccent = Color(0xFFF2F2F2),
    textPrimary = Color(0xFF181818),
    textSecondary = Color(0xFF383838),
    textPrimaryInverse = Color(0xFFFBFBFB),
    textSecondaryInverse = Color(0xFFE8E8E8),
    systemStatusBarColor = Color(0xFFF4F4F4),
    systemNavigationBarColor = Color(0xFFFCFCFC),
    backgroundContainer = Color(0xFFF4F4F4),
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
    onAccent = Color(0xFFF2F2F2),
    textPrimary = Color(0xFFFBFBFB),
    textSecondary = Color(0xFFE8E8E8),
    textPrimaryInverse = Color(0xFF181818),
    textSecondaryInverse = Color(0xFF383838),
    systemStatusBarColor = Color(0xFF181818),
    systemNavigationBarColor = Color(0xFF202020),
    backgroundContainer = Color(0xFF111111),
    backgroundPrimary = Color(0xFF181818),
    backgroundSecondary = Color(0xFF282828),
    backgroundPrimaryInverse = Color(0xFFFFFFFF),
    backgroundSecondaryInverse = Color(0xFFF8F8F8),
    backgroundNav = Color(0xFF202020),
    inputBackground = Color(0xFF282828),
    isLight = false
)

@RequiresApi(Build.VERSION_CODES.S)
fun AppColors.dynamic(colorScheme: ColorScheme, isLightMode: Boolean) = copy(
    primary = colorScheme.primary,
    primaryDark = colorScheme.primary,
    accent = colorScheme.surfaceTint,
    onAccent = colorScheme.inverseOnSurface,

    backgroundContainer = when (isLightMode) {
        true -> colorScheme.surface
        false -> colorScheme.surface.copy(
            alpha = colorScheme.surface.alpha,
            red = colorScheme.surface.red * 0.8f,
            green = colorScheme.surface.green * 0.8f,
            blue = colorScheme.surface.blue * 0.8f
        )
    },
    backgroundPrimary = colorScheme.background,
    backgroundSecondary = colorScheme.surfaceColorAtElevation(3.dp),
    backgroundPrimaryInverse = colorScheme.inverseSurface,
    backgroundSecondaryInverse = colorScheme.inversePrimary,
    backgroundNav = colorScheme.surfaceColorAtElevation(6.dp),

    systemStatusBarColor = when (isLightMode) {
        true -> colorScheme.background
        false -> colorScheme.background
    },
    systemNavigationBarColor = colorScheme.surfaceColorAtElevation(6.dp)
)