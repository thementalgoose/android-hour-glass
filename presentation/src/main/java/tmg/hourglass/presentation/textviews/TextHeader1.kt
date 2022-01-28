package tmg.hourglass.presentation.textviews

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import tmg.hourglass.presentation.AppTheme
import tmg.hourglass.presentation.AppThemePreview

@Composable
fun TextHeader1(
    text: String,
    modifier: Modifier = Modifier,
    brand: Boolean = false,
    style: TextStyle = AppTheme.typography.h1.copy(
        color = when (brand) {
            true -> AppTheme.colors.primary
            false -> AppTheme.colors.textPrimary
        }
    )
) {
    Text(
        text,
        modifier = modifier.fillMaxWidth(),
        style = style
    )
}

@Preview
@Composable
private fun PreviewLight() {
    AppThemePreview(isLight = true) {
        TextHeader1(
            text = "Headline 1"
        )
    }
}

@Preview
@Composable
private fun PreviewDark() {
    AppThemePreview(isLight = false) {
        TextHeader1(
            text = "Headline 1"
        )
    }
}

@Preview
@Composable
private fun PreviewBrand() {
    AppThemePreview(isLight = true) {
        TextHeader1(
            text = "Headline 1",
            brand = true
        )
    }
}