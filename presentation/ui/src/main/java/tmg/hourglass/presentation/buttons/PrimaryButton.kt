package tmg.hourglass.presentation.buttons

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
        colors = ButtonDefaults.buttonColors(
            containerColor = AppTheme.colors.accent,
            contentColor = AppTheme.colors.onAccent,
            disabledContainerColor = AppTheme.colors.accent.copy(alpha = 0.5f)
        ),
        modifier = modifier,
        enabled = isEnabled,
        shape = RoundedCornerShape(AppTheme.dimensions.radiusSmall),
        onClick = onClick
    ) {
        TextBody1(
            text = text,
            style = AppTheme.typography.body1.copy(
                color = when (isEnabled) {
                    true -> AppTheme.colors.onAccent
                    false -> AppTheme.colors.onAccent.copy(alpha = 0.5f)
                }
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

@PreviewTheme
@Composable
private fun PreviewDisabled() {
    AppThemePreview {
        Box(modifier = Modifier.padding(16.dp)) {
            PrimaryButton(
                text = "Primary Button",
                onClick = {},
                isEnabled = false
            )
        }
    }
}