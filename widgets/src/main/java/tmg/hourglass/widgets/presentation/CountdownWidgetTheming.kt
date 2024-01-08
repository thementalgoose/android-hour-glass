package tmg.hourglass.widgets.presentation

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.glance.text.TextDefaults.defaultTextStyle
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider

data class CountdownWidgetTheming(
    val backgroundColor: Color,
    val barBackgroundColor: Color,
    val textColor: Color
) {
    val title: TextStyle = defaultTextStyle.copy(
        color = ColorProvider(textColor),
        fontSize = 18.sp
    )
    val content: TextStyle = defaultTextStyle.copy(
        color = ColorProvider(textColor),
        fontSize = 14.sp
    )
}

val countdownWidgetLight = CountdownWidgetTheming(
    backgroundColor = Color(0xFFF2F2F2),
    barBackgroundColor = Color(0xFFE8E8E8),
    textColor = Color(0xFF181818)
)

val countdownWidgetDark = CountdownWidgetTheming(
    backgroundColor = Color(0xFF111111),
    barBackgroundColor = Color(0xFF1B1B1B),
    textColor = Color(0xFFF2F2F2)
)