package tmg.hourglass.presentation.buttons

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import tmg.hourglass.presentation.AppTheme
import tmg.hourglass.presentation.AppThemePreview
import tmg.hourglass.presentation.PreviewTheme
import tmg.hourglass.presentation.textviews.TextBody1

@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true
) {
    Button(
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = AppTheme.colors.backgroundSecondary,
            disabledContainerColor = AppTheme.colors.backgroundSecondary.copy(alpha = 0.5f)
        ),
        shape = RoundedCornerShape(AppTheme.dimensions.radiusSmall),
        modifier = modifier,
        enabled = isEnabled,
        onClick = onClick
    ) {
        TextBody1(
            text = text,
            textColor = when (isEnabled) {
                true -> AppTheme.colors.textPrimary
                false -> AppTheme.colors.textPrimary.copy(alpha = 0.5f)
            }
        )
    }
}

@PreviewTheme
@Composable
private fun Preview() {
    AppThemePreview {
        Box(modifier = Modifier.padding(16.dp)) {
            SecondaryButton(
                text = "Secondary Button",
                onClick = {}
            )
        }
    }
}

@PreviewTheme
@Composable
private fun PreviewDisabled() {
    AppThemePreview {
        Box(modifier = Modifier.padding(16.dp)) {
            SecondaryButton(
                text = "Secondary Button",
                onClick = {},
                isEnabled = false
            )
        }
    }
}