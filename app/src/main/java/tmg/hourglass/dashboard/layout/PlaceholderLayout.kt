package tmg.hourglass.dashboard.layout

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tmg.hourglass.R
import tmg.hourglass.strings.R.string
import tmg.hourglass.presentation.AppTheme
import tmg.hourglass.presentation.AppThemePreview
import tmg.hourglass.presentation.textviews.TextBody1

@Composable
fun PlaceholderLayout(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = AppTheme.dimensions.paddingMedium,
                end = AppTheme.dimensions.paddingMedium,
                top = AppTheme.dimensions.paddingMedium,
                bottom = AppTheme.dimensions.paddingMedium
            )
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(
                id = R.drawable.ic_no_items,
            ),
            tint = AppTheme.colors.textSecondary,
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextBody1(text = stringResource(id = string.placeholder_title))
    }
}

@Preview
@Composable
private fun Preview() {
AppThemePreview {
    PlaceholderLayout()

}
    PlaceholderLayout()
}