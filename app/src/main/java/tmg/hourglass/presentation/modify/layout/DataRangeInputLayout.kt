package tmg.hourglass.presentation.modify.layout

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import tmg.hourglass.presentation.AppTheme
import tmg.hourglass.presentation.AppThemePreview
import tmg.hourglass.presentation.PreviewTheme
import tmg.hourglass.presentation.inputs.Input
import tmg.hourglass.presentation.textviews.TextBody1
import tmg.hourglass.presentation.textviews.TextHeader2
import tmg.hourglass.strings.R

private const val weightMax = 6f
private const val weightMin = 1f

@Composable
fun DataRangeInputLayout(
    initial: String,
    initialUpdated: (String) -> Unit,
    finishing: String,
    finishingUpdated: (String) -> Unit,
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

        Row(modifier = Modifier.fillMaxWidth()) {
            val startWeight = animateFloatAsState(
                targetValue = when {
                    initial.isBlank() || initial == "0" -> weightMin
                    else -> weightMax
                },
                label = "StartWeight"
            )
            val endWeight = animateFloatAsState(
                targetValue = when {
                    finishing.isBlank() || finishing == "0" -> weightMin
                    else -> weightMax
                },
                label = "EndWeight"
            )

            Input(
                modifier = Modifier
                    .weight(startWeight.value),
                initial = initial,
                inputUpdated = initialUpdated,
                keyboardType = KeyboardType.Number,
                hint = "0"
            )
            Spacer(Modifier.width(8.dp))
            Input(
                modifier = Modifier
                    .weight(endWeight.value),
                initial = finishing,
                inputUpdated = finishingUpdated,
                keyboardType = KeyboardType.Number,
                hint = "0"
            )
        }
    }
}

@PreviewTheme
@Composable
private fun PreviewEndingValue() {
    AppThemePreview {
        DataRangeInputLayout(
            initial = "0",
            initialUpdated = { },
            finishing = "100",
            finishingUpdated = { },
        )
    }
}

@PreviewTheme
@Composable
private fun PreviewStartingValue() {
    AppThemePreview {
        DataRangeInputLayout(
            initial = "100",
            initialUpdated = { },
            finishing = "0",
            finishingUpdated = { },
        )
    }
}

@PreviewTheme
@Composable
private fun PreviewDiffValues() {
    AppThemePreview {
        DataRangeInputLayout(
            initial = "100",
            initialUpdated = { },
            finishing = "100",
            finishingUpdated = { },
        )
    }
}