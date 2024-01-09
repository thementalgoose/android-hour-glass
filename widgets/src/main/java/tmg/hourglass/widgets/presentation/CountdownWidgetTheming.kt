package tmg.hourglass.widgets.presentation

import android.content.Context
import android.os.Build
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
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

fun getCountdownWidgetColors(context: Context, isDarkMode: Boolean): CountdownWidgetTheming {
    return CountdownWidgetTheming(
        textColor = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && isDarkMode ->
                Color(ContextCompat.getColor(context, android.R.color.system_neutral1_50))
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ->
                Color(ContextCompat.getColor(context, android.R.color.system_neutral1_800))
            isDarkMode ->
                Color(0xFFFBFBFB)
            else ->
                Color(0xFF181818)
        },
        backgroundColor = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && isDarkMode ->
                Color(ContextCompat.getColor(context, android.R.color.system_neutral1_900))
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ->
                Color(ContextCompat.getColor(context, android.R.color.system_neutral2_10))
            isDarkMode ->
                Color(0xFF181818)
            else ->
                Color(0xFFFBFBFB)
        },
        barBackgroundColor = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && isDarkMode ->
                Color(ContextCompat.getColor(context, android.R.color.system_neutral1_800))
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ->
                Color(ContextCompat.getColor(context, android.R.color.system_neutral2_100))
            isDarkMode ->
                Color(0xFF282828)
            else ->
                Color(0xFFEFEFEF)
        }
    )
}