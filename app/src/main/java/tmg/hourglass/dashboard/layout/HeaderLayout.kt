package tmg.hourglass.dashboard.layout

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tmg.hourglass.R
import tmg.hourglass.strings.R.string
import tmg.hourglass.presentation.AppTheme
import tmg.hourglass.presentation.AppThemePreview
import tmg.hourglass.presentation.textviews.TextHeader1

@Composable
fun DashboardHeaderLayout(
    clickSettings: (() -> Unit)?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                bottom = AppTheme.dimensions.paddingSmall
            )
    ) {
        Column(
            modifier = Modifier
                .padding(
                    top = AppTheme.dimensions.paddingMedium,
                    start = AppTheme.dimensions.paddingMedium,
                    end = AppTheme.dimensions.paddingXSmall
                )
                .align(Alignment.End)
        ) {
            clickSettings?.let {
                IconButton(onClick = it) {
                    Icon(Icons.Default.Settings, contentDescription = stringResource(id = string.ab_settings))
                }
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
        TextHeader1(
            modifier = Modifier
                .padding(
                    start = AppTheme.dimensions.paddingMedium,
                    end = AppTheme.dimensions.paddingMedium
                ),
            text = stringResource(id = R.string.app_name)
        )
    }
}

@Preview
@Composable
private fun Preview() {
    AppThemePreview {
        DashboardHeaderLayout(
            clickSettings = { }
        )
    }
}