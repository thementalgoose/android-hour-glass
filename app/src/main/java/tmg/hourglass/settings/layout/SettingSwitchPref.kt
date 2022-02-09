package tmg.hourglass.settings.layout

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tmg.hourglass.presentation.AppTheme
import tmg.hourglass.presentation.AppThemePreview
import tmg.hourglass.presentation.textviews.TextBody1
import tmg.hourglass.presentation.textviews.TextBody2

@Composable
fun SettingSwitchPref(
    title: String,
    subtitle: String,
    checkboxState: Boolean,
    onClick: (newState: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {

    val checkbox = remember { mutableStateOf(checkboxState) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                onClick = {
                    onClick(!checkboxState)
                    checkbox.value = !checkboxState
                }
            )
            .padding(
                start = AppTheme.dimensions.paddingMedium,
                end = AppTheme.dimensions.paddingMedium,
                top = AppTheme.dimensions.paddingNSmall,
                bottom = AppTheme.dimensions.paddingNSmall
            )
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(
                    end = AppTheme.dimensions.paddingSmall
                )
        ) {
            TextBody1(text = title)
            Spacer(modifier = Modifier.height(4.dp))
            TextBody2(text = subtitle)
        }
        Checkbox(
            checked = checkbox.value,
            onCheckedChange = null
        )
    }
}

@Preview
@Composable
private fun Preview() {
    AppThemePreview {
        SettingSwitchPref(
            title = "App Theme",
            subtitle = "Some kind of app theme preference that you need to click",
            checkboxState = true,
            onClick = { }
        )
    }
}