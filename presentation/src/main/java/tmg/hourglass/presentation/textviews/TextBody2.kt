package tmg.hourglass.presentation.textviews

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import tmg.hourglass.presentation.AppTheme
import tmg.hourglass.presentation.AppThemePreview

@Composable
fun TextBody2(
    text: String,
    modifier: Modifier = Modifier,
    bold: Boolean = false,
    textColor: Color? = null,
    maxLines: Int? = null
) {
    Text(
        text,
        modifier = modifier,
        maxLines = maxLines ?: Int.MAX_VALUE,
        overflow = if (maxLines != null) TextOverflow.Ellipsis else TextOverflow.Clip,
        style = AppTheme.typography.body2.copy(
            fontWeight = when (bold) {
                true -> FontWeight.Bold
                false -> FontWeight.Normal
            },
            color = textColor ?: AppTheme.colors.textPrimary
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewLight() {
    AppThemePreview(isLight = true) {
        TextBody2(
            text = "Body 2"
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewDark() {
    AppThemePreview(isLight = false) {
        TextBody2(
            text = "Body 2"
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewLightBold() {
    AppThemePreview(isLight = true) {
        TextBody2(
            text = "Body 2 Bold",
            bold = true
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewDarkBold() {
    AppThemePreview(isLight = false) {
        TextBody2(
            text = "Body 2 Bold",
            bold = true
        )
    }
}