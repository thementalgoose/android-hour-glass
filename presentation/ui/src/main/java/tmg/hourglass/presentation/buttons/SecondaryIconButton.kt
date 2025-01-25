package tmg.hourglass.presentation.buttons

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import tmg.hourglass.presentation.AppTheme
import tmg.hourglass.presentation.AppThemePreview
import tmg.hourglass.presentation.PreviewTheme

@Composable
fun SecondaryIconButton(
    icon: ImageVector,
    contentDescription: String,
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
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = when (isEnabled) {
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
            SecondaryIconButton(
                icon = Icons.Outlined.PlayArrow,
                contentDescription = "",
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
            SecondaryIconButton(
                icon = Icons.Outlined.PlayArrow,
                contentDescription = "",
                onClick = {},
                isEnabled = false
            )
        }
    }
}