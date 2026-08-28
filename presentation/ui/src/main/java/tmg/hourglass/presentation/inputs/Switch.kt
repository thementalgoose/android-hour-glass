package tmg.hourglass.presentation.inputs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import tmg.hourglass.presentation.AppTheme
import tmg.hourglass.presentation.AppThemePreview
import tmg.hourglass.presentation.PreviewTheme
import tmg.hourglass.presentation.textviews.TextBody1

@Composable
fun Switch(
    isChecked: Boolean,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TextBody1(
            modifier = Modifier.weight(1f).align(Alignment.CenterVertically),
            text = label
        )
        androidx.compose.material3.Switch(
            checked = isChecked,
            onCheckedChange = null,
            enabled = enabled,
            colors = SwitchDefaults.colors(
                checkedThumbColor = AppTheme.colors.accent,
                checkedTrackColor = AppTheme.colors.accent
            )
        )
    }
}

@PreviewTheme
@Composable
private fun PreviewChecked() {
    AppThemePreview {
        Switch(
            label = "Enabled",
            isChecked = true
        )
    }
}

@PreviewTheme
@Composable
private fun PreviewUnchecked() {
    AppThemePreview {
        Switch(
            label = "Disabled",
            isChecked = false
        )
    }
}