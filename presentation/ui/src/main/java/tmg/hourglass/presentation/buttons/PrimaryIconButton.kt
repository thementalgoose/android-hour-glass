package tmg.hourglass.presentation.buttons

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tmg.hourglass.presentation.AppTheme
import tmg.hourglass.presentation.AppThemePreview
import tmg.hourglass.presentation.R
import tmg.hourglass.presentation.textviews.TextBody1

@Composable
fun PrimaryIconButton(
    @DrawableRes
    icon: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    isEnabled: Boolean = true
) {
    Button(
        modifier = modifier,
        enabled = isEnabled,
        onClick = onClick
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = contentDescription,
            tint = AppTheme.colors.textPrimary
        )
    }
}

@Preview
@Composable
private fun PreviewLight() {
    AppThemePreview(isLight = true) {
        Box(modifier = Modifier.padding(16.dp)) {
            PrimaryIconButton(
                icon = 0,
                onClick = {}
            )
        }
    }
}

@Preview
@Composable
private fun PreviewDark() {
    AppThemePreview(isLight = false) {
        Box(modifier = Modifier.padding(16.dp)) {
            PrimaryIconButton(
                icon = 0,
                onClick = {}
            )
        }
    }
}