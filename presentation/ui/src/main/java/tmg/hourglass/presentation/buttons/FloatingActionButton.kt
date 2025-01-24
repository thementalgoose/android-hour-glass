package tmg.hourglass.presentation.buttons

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import tmg.hourglass.presentation.AppTheme
import tmg.hourglass.presentation.AppThemePreview
import tmg.hourglass.presentation.PreviewTheme
import tmg.hourglass.presentation.textviews.TextBody1

@Composable
fun FloatingActionButton(
    label: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    ExtendedFloatingActionButton(
        text = {
            TextBody1(
                text = label,
                textColor = AppTheme.colors.onAccent,
            )
        },
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = AppTheme.colors.onAccent
            )
        },
        containerColor = AppTheme.colors.accent,
        onClick = onClick
    )
}

@PreviewTheme
@Composable
private fun Preview() {
    AppThemePreview {
        Box(Modifier.padding(16.dp)) {
            FloatingActionButton(
                label = "New",
                icon = Icons.Outlined.Add,
                onClick = { }
            )
        }
    }
}