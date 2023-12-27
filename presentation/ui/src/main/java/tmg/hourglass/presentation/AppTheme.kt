package tmg.hourglass.presentation

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.jakewharton.threetenabp.AndroidThreeTen
import org.threeten.bp.LocalDateTime

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
    val colors = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        if (isLight) {
            lightColors.dynamic(dynamicLightColorScheme(LocalContext.current), isLightMode = true)
        } else {
            darkColors.dynamic(dynamicDarkColorScheme(LocalContext.current), isLightMode = false)
        }
    } else {
        if (isLight) lightColors else darkColors
    }

    AppTheme.isLight = isLight

    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(color = colors.systemStatusBarColor)
    systemUiController.setNavigationBarColor(color = colors.systemNavigationBarColor)

    CompositionLocalProvider(
        LocalColors provides colors,
    ) {
        MaterialTheme(
            colors = colors.appColors
        ) {
            content()
        }
    }
}

@Composable
fun AppThemePreview(
    isLight: Boolean = !isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    AppTheme(isLight = isLight) {
        AndroidThreeTen.init(LocalContext.current)
        Box(
            modifier = Modifier
                .background(AppTheme.colors.backgroundPrimary)
        ) {
            content()
        }
    }
}