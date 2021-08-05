package tmg.hourglass.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkPallette = darkColors(
    primary = brand,
    primaryVariant = brand,
    secondary = brand
)

private val LightPallette = lightColors(
    primary = brand,
    primaryVariant = brand,
    secondary = brand
)

@Composable
fun AppTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = when (darkTheme) {
        true -> DarkPallette
        false -> LightPallette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}