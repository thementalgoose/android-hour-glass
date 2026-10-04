package tmg.hourglass.wearos.style

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.wear.compose.material.Colors
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Typography

internal val LocalWearColors = staticCompositionLocalOf { darkWearColors }

data class WearColors(
    val primary: Color,
    val primaryDark: Color,
    val accent: Color,
    val onAccent: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val backgroundPrimary: Color,
    val backgroundSecondary: Color,
    val isLight: Boolean
) {
    val wearColors: Colors = Colors(
        primary = primary,
        primaryVariant = primaryDark,
        secondary = accent,
        secondaryVariant = accent,
        background = backgroundPrimary,
        surface = backgroundSecondary,
        onPrimary = if (isLight) textPrimary else primaryDark,
        onSecondary = onAccent,
        onBackground = textPrimary,
        onSurface = textSecondary
    )
}

val lightWearColors = WearColors(
    primary = Color(0xFFF1A16B),
    primaryDark = Color(0xFFFFFFFF),
    accent = Color(0xFFED835B),
    onAccent = Color(0xFFF2F2F2),
    textPrimary = Color(0xFF181818),
    textSecondary = Color(0xFF383838),
    backgroundPrimary = Color(0xFFFFFFFF),
    backgroundSecondary = Color(0xFFF0F0F0),
    isLight = true
)

val darkWearColors = WearColors(
    primary = Color(0xFFF1A16B),
    primaryDark = Color(0xFF181818),
    accent = Color(0xFFED835B),
    onAccent = Color(0xFFF2F2F2),
    textPrimary = Color(0xFFFBFBFB),
    textSecondary = Color(0xFFE8E8E8),
    backgroundPrimary = Color(0xFF181818),
    backgroundSecondary = Color(0xFF282828),
    isLight = false
)

object WearTheme {
    val colors: WearColors
        @Composable
        @ReadOnlyComposable
        get() = LocalWearColors.current

    val typography: Typography
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.typography
}

@Composable
fun WearTheme(
    isLight: Boolean = false,
    content: @Composable () -> Unit
) {
    val colors = if (isLight) lightWearColors else darkWearColors

    CompositionLocalProvider(
        LocalWearColors provides colors
    ) {
        MaterialTheme(
            colors = colors.wearColors,
            content = content
        )
    }
}
