package tmg.hourglass.presentation

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp


data class AppTypography(
    val h1: TextStyle = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp
    ),
    val h2: TextStyle = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
    ),
    val title: TextStyle = TextStyle(
        fontSize = 16.sp
    ),
    val section: TextStyle = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 13.sp
    ),
    val body1: TextStyle = TextStyle(
        fontSize = 14.sp
    ),
    val body2: TextStyle = TextStyle(
        fontSize = 12.sp
    ),
    val caption: TextStyle = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 11.sp
    )
)
