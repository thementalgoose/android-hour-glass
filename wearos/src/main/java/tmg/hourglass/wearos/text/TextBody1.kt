package tmg.hourglass.wearos.text

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.wear.compose.material.Text
import tmg.hourglass.wearos.style.WearTheme

@Composable
fun TextBody1(
    text: String,
    modifier: Modifier = Modifier,
    bold: Boolean = false,
    textAlign: TextAlign = TextAlign.Start,
    textColor: Color? = null,
    fontStyle: FontStyle? = null,
    style: TextStyle = WearTheme.typography.body1.copy(
        fontWeight = when (bold) {
            true -> FontWeight.Bold
            false -> FontWeight.Normal
        },
        color = textColor ?: WearTheme.colors.textPrimary
    ),
    maxLines: Int? = null
) {
    Text(
        text = text,
        textAlign = textAlign,
        modifier = modifier,
        maxLines = maxLines ?: Int.MAX_VALUE,
        overflow = if (maxLines != null) TextOverflow.Ellipsis else TextOverflow.Clip,
        fontStyle = fontStyle,
        style = style
    )
}
