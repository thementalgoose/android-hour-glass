package tmg.hourglass.presentation.inputs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import tmg.hourglass.presentation.AppTheme
import tmg.hourglass.presentation.AppThemePreview
import tmg.hourglass.presentation.textviews.TextBody1
import tmg.hourglass.presentation.textviews.TextBody2

@Composable
fun Input(
    input: State<TextFieldValue>,
    inputUpdated: (String) -> Unit,
    hint: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(AppTheme.dimensions.radiusSmall))
            .background(AppTheme.colors.backgroundSecondary)
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = input.value,
            onValueChange = {
                inputUpdated(it.text)
            },
            placeholder = {
                TextBody1(
                    modifier = Modifier
                        .align(Alignment.CenterStart),
                    text = hint
                )
            },
            colors = TextFieldDefaults.textFieldColors(
                textColor = AppTheme.colors.textPrimary,
                disabledTextColor = Color.Transparent,
                backgroundColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        )
    }
}

@Preview
@Composable
private fun PreviewLight() {
    AppThemePreview(isLight = true) {
        val input = remember { mutableStateOf(TextFieldValue()) }
        Input(
            input = input,
            inputUpdated = { },
            hint = "Name"
        )
    }
}

@Preview
@Composable
private fun PreviewDark() {
    AppThemePreview(isLight = false) {
        val input = remember { mutableStateOf(TextFieldValue()) }
        Input(
            input = input,
            inputUpdated = { },
            hint = "Name"
        )
    }
}