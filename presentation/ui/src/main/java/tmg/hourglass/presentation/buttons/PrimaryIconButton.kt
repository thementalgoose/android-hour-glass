package tmg.hourglass.presentation.buttons

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import tmg.hourglass.presentation.AppTheme
import tmg.hourglass.presentation.AppThemePreview
import tmg.hourglass.presentation.PreviewTheme

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
        Icon(
            painter = painterResource(id = icon),
            contentDescription = contentDescription,
            tint = when (isEnabled) {
                true -> AppTheme.colors.onAccent
                false -> AppTheme.colors.onAccent.copy(alpha = 0.5f)
            }
        )
    }
}

@PreviewTheme
@Composable
private fun Preview() {
    AppThemePreview {
        Box(modifier = Modifier.padding(16.dp)) {
            PrimaryIconButton(
                icon = 0,
                onClick = {}
            )
        }
    }
}

@PreviewTheme
@Composable
private fun PreviewDisabled() {
    AppThemePreview(isLight = false) {
        Box(modifier = Modifier.padding(16.dp)) {
            PrimaryIconButton(
                icon = 0,
                onClick = {},
                isEnabled = false
            )
        }
    }
}