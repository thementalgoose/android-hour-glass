package tmg.hourglass.widgets.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceTheme
import androidx.glance.text.TextDefaults.defaultTextStyle
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider

data class CountdownWidgetTheming(
    val backgroundColor: ColorProvider,
    val barBackgroundColor: ColorProvider,
    val textColor: ColorProvider
) {
    val title: TextStyle = defaultTextStyle.copy(
        color = textColor,
        fontSize = 18.sp
    )
    val content: TextStyle = defaultTextStyle.copy(
        color = textColor,
        fontSize = 14.sp
    )
}

@Composable
fun getCountdownWidgetColors(): CountdownWidgetTheming {
    return CountdownWidgetTheming(
        textColor = GlanceTheme.colors.onBackground,
        backgroundColor = GlanceTheme.colors.widgetBackground,
        barBackgroundColor = GlanceTheme.colors.secondaryContainer
    )
}