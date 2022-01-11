package tmg.hourglass.settings.release

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tmg.hourglass.R
import tmg.hourglass.presentation.AppTheme
import tmg.hourglass.presentation.AppThemePreview
import tmg.hourglass.presentation.textviews.TextBody1
import tmg.hourglass.presentation.textviews.TextBody2

@Composable
fun ReleaseItemLayout(
    title: String,
    content: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = AppTheme.dimensions.paddingMedium,
                end = AppTheme.dimensions.paddingMedium,
                top = AppTheme.dimensions.paddingSmall,
                bottom = AppTheme.dimensions.paddingSmall
            )
    ) {
        TextBody1(
            text = title,
            bold = true
        )
        Spacer(modifier = Modifier.height(4.dp))
        TextBody1(
            text = content
        )
    }
}

@Preview
@Composable
private fun Preview() {
    AppThemePreview(isLight = true) {
        ReleaseItemLayout(
            title = stringResource(id = R.string.release_17_title),
            content = stringResource(id = R.string.release_17)
        )
    }
}