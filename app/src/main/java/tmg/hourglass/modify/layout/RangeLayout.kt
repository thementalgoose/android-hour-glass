package tmg.hourglass.modify.layout

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tmg.hourglass.R
import tmg.hourglass.presentation.AppTheme
import tmg.hourglass.presentation.AppThemePreview
import tmg.hourglass.presentation.inputs.Input
import tmg.hourglass.presentation.textviews.TextBody1
import tmg.hourglass.presentation.textviews.TextHeader2

@Composable
fun RangeLayout(
    initial: State<TextFieldValue>,
    initialUpdated: (String) -> Unit,
    finished: State<TextFieldValue>,
    finishedUpdated: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = AppTheme.dimensions.paddingMedium,
                end = AppTheme.dimensions.paddingMedium,
                top = AppTheme.dimensions.paddingMedium,
                bottom = AppTheme.dimensions.paddingMedium
            )
    ) {
        TextHeader2(text = stringResource(id = R.string.modify_field_range))
        Spacer(modifier = Modifier.height(8.dp))
        TextBody1(text = stringResource(id = R.string.modify_field_range_desc))
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Input(
                modifier = Modifier.weight(1f),
                input = initial,
                inputUpdated = initialUpdated,
                hint = stringResource(id = R.string.modify_field_range_initial)
            )
            Spacer(modifier = Modifier.width(AppTheme.dimensions.paddingMedium))
            Input(
                modifier = Modifier.weight(1f),
                input = finished,
                inputUpdated = finishedUpdated,
                hint = stringResource(id = R.string.modify_field_range_final)
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    AppThemePreview {
        RangeLayout(
            initial = remember { mutableStateOf(TextFieldValue()) },
            initialUpdated = { },
            finished = remember { mutableStateOf(TextFieldValue()) },
            finishedUpdated = { }
        )
    }
}