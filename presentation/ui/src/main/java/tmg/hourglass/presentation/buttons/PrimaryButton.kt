package tmg.hourglass.presentation.buttons

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tmg.hourglass.presentation.AppTheme
import tmg.hourglass.presentation.AppThemePreview
import tmg.hourglass.presentation.PreviewTheme
import tmg.hourglass.presentation.textviews.TextBody1

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true
) {
    Button(
        modifier = modifier,
        enabled = isEnabled,
        onClick = onClick
    ) {
        TextBody1(
            text = text,
            style = AppTheme.typography.body1.copy(
                color = AppTheme.colors.onAccent
            )
        )
    }
}

@PreviewTheme
@Composable
private fun Preview() {
    AppThemePreview {
        Box(modifier = Modifier.padding(16.dp)) {
            PrimaryButton(
                text = "Primary Button",
                onClick = {}
            )
        }
    }
}