package tmg.hourglass.presentation.textviews

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import tmg.hourglass.presentation.AppTheme
import tmg.hourglass.presentation.AppThemePreview

@Composable
fun TextHeader2(
    text: String,
    modifier: Modifier = Modifier,
    brand: Boolean = false,
    style: TextStyle = AppTheme.typography.h2.copy(
        color = when (brand) {
            true -> AppTheme.colors.primary
            false -> AppTheme.colors.textPrimary
        }
    )
) {
    Text(
        text,
        modifier = modifier,
        style = style
    )
}

@Preview
@Composable
private fun PreviewLight() {
    AppThemePreview(isLight = true) {
        TextHeader2(
            text = "Headline 2"
        )
    }
}

@Preview
@Composable
private fun PreviewDark() {
    AppThemePreview(isLight = false) {
        TextHeader2(
            text = "Headline 2"
        )
    }
}

@Preview
@Composable
private fun PreviewBrand() {
    AppThemePreview(isLight = true) {
        TextHeader2(
            text = "Headline 2",
            brand = true
        )
    }
}