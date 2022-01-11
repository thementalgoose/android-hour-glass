package tmg.hourglass.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Modifier

object AppTheme {
    val colors: AppColors
        @Composable
        @ReadOnlyComposable
        get() = LocalColors.current

    internal var isLight: Boolean = true
        set(value) {
            when (value) {
                true -> LocalColors.provides(lightColors)
                false -> LocalColors.provides(darkColors)
            }
            field = value
        }

    val dimensions: AppDimensions = AppDimensions()

    val typography: AppTypography = AppTypography()
}


@Composable
fun AppTheme(
    isLight: Boolean = !isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (isLight) lightColors else darkColors
    AppTheme.isLight = isLight
    CompositionLocalProvider(
        LocalColors provides colors,
    ) {
        content()
    }
}

@Composable
fun AppThemePreview(
    isLight: Boolean = !isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    AppTheme(isLight = isLight) {
        Box(
            modifier = Modifier
                .background(AppTheme.colors.backgroundPrimary)
        ) {
            content()
        }
    }
}