package tmg.hourglass.presentation.textviews

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import tmg.hourglass.presentation.AppTheme
import tmg.hourglass.presentation.AppThemePreview

@Composable
fun TextBody1(
    text: String,
    modifier: Modifier = Modifier,
    bold: Boolean = false,
    textAlign: TextAlign = TextAlign.Start,
    textColor: Color? = null,
    fontStyle: FontStyle? = null,
    style: TextStyle = AppTheme.typography.body1.copy(
        fontWeight = when (bold) {
            true -> FontWeight.Bold
            false -> FontWeight.Normal
        },
        color = textColor ?: AppTheme.colors.textPrimary
    ),
    maxLines: Int? = null
) {
    Text(
        text,
        textAlign = textAlign,
        modifier = modifier,
        maxLines = maxLines ?: Int.MAX_VALUE,
        overflow = if (maxLines != null) TextOverflow.Ellipsis else TextOverflow.Clip,
        fontStyle = fontStyle,
        style = style
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewLight() {
    AppThemePreview(isLight = true) {
        TextBody1(
            text = "Body 1"
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewDark() {
    AppThemePreview(isLight = false) {
        TextBody1(
            text = "Body 1"
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewLightBold() {
    AppThemePreview(isLight = true) {
        TextBody1(
            text = "Body 1 Bold",
            bold = true
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewDarkBold() {
    AppThemePreview(isLight = false) {
        TextBody1(
            text = "Body 1 Bold",
            bold = true
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewLightItalic() {
    AppThemePreview(isLight = true) {
        TextBody1(
            text = "Body 1 Italic",
            fontStyle = FontStyle.Italic
        )
    }
}