package tmg.hourglass.settings.layout

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
    checkbox: Boolean,
    onClick: (newState: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {

    val checkboxVal = remember { mutableStateOf(checkbox) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                onClick = {
                    onClick(!checkbox)
                    checkboxVal.value = !checkbox
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
            checked = checkboxVal.value,
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
            checkbox = true,
            onClick = { }
        )
    }
}