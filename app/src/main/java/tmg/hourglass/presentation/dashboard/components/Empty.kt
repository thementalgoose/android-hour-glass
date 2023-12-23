package tmg.hourglass.presentation.dashboard.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import tmg.hourglass.R
import tmg.hourglass.presentation.AppTheme
import tmg.hourglass.presentation.AppThemePreview
import tmg.hourglass.presentation.PreviewTheme
import tmg.hourglass.presentation.textviews.TextBody1
import tmg.hourglass.strings.R.string

@Composable
fun Empty(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = AppTheme.dimensions.paddingXLarge,
                vertical = AppTheme.dimensions.paddingMedium
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.paddingNSmall)
    ) {
        TextBody1(
            text = stringResource(id = string.dashboard_empty_title),
            textAlign = TextAlign.Center,
            textColor = AppTheme.colors.textSecondary.copy(alpha = 0.5f)
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_dashboard_empty),
            contentDescription = null,
            tint = AppTheme.colors.textSecondary.copy(alpha = 0.3f)
        )
        TextBody1(
            text = stringResource(id = string.dashboard_empty_subtitle),
            textAlign = TextAlign.Center,
            textColor = AppTheme.colors.textSecondary.copy(alpha = 0.5f)
        )
    }
}

@PreviewTheme
@Composable
private fun Preview() {
    AppThemePreview {
        Empty()
    }
}