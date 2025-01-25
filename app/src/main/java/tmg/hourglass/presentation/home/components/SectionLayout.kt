package tmg.hourglass.presentation.home.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tmg.hourglass.presentation.AppTheme
import tmg.hourglass.presentation.AppThemePreview
import tmg.hourglass.presentation.textviews.TextHeader2

@Composable
fun DashboardSectionLayout(
    text: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = AppTheme.dimensions.paddingMedium,
                end = AppTheme.dimensions.paddingMedium
            )
    ) {
        Spacer(modifier = Modifier.height(AppTheme.dimensions.paddingMedium))
        TextHeader2(text = text)
        Spacer(modifier = Modifier.height(4.dp))
        HorizontalDivider(color = AppTheme.colors.backgroundSecondary)
        Spacer(modifier = Modifier.height(AppTheme.dimensions.paddingSmall))
    }
}

@Preview
@Composable
private fun Preview() {
    AppThemePreview {
        DashboardSectionLayout("Header")
    }
}