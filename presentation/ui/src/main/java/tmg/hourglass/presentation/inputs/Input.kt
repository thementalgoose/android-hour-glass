package tmg.hourglass.presentation.inputs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tmg.hourglass.presentation.AppTheme
import tmg.hourglass.presentation.AppThemePreview
import tmg.hourglass.presentation.PreviewTheme
import tmg.hourglass.presentation.textviews.TextBody1
import tmg.hourglass.presentation.textviews.TextBody2

@Composable
fun Input(
    inputUpdated: (String) -> Unit,
    hint: String,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    initial: String = "",
) {

    val input = remember { mutableStateOf(initial) }

    DisposableEffect(initial) {
        input.value = initial
        return@DisposableEffect onDispose { }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(AppTheme.dimensions.radiusSmall))
            .background(AppTheme.colors.backgroundSecondary)
    ) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            value = input.value,
            onValueChange = {
                input.value = it
                inputUpdated(it)
            },
            placeholder = {
                TextBody1(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .alpha(0.5f),
                    text = hint
                )
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = AppTheme.colors.backgroundSecondary,
                focusedTextColor = AppTheme.colors.textPrimary,
                focusedIndicatorColor = Color.Transparent,
                disabledTextColor = AppTheme.colors.textSecondary,
                disabledIndicatorColor = Color.Transparent,
                disabledLabelColor = AppTheme.colors.backgroundSecondary,
                unfocusedLabelColor = AppTheme.colors.backgroundSecondary,
                unfocusedContainerColor = AppTheme.colors.backgroundSecondary,
                unfocusedTextColor = AppTheme.colors.textSecondary,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = AppTheme.colors.accent,
                errorCursorColor = AppTheme.colors.appColors.error,
            )
        )
    }
}

@PreviewTheme
@Composable
private fun Preview() {
    AppThemePreview {
        Box(Modifier.padding(16.dp)) {
            Input(
                initial = "testInput",
                inputUpdated = { },
                hint = "Name"
            )
        }
    }
}
@PreviewTheme
@Composable
private fun PreviewHint() {
    AppThemePreview {
        Box(Modifier.padding(16.dp)) {
            Input(
                inputUpdated = { },
                hint = "Name"
            )
        }
    }
}