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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import tmg.hourglass.presentation.AppTheme
import tmg.hourglass.presentation.inputs.Input
import tmg.hourglass.presentation.textviews.TextBody1
import tmg.hourglass.presentation.textviews.TextHeader2
import tmg.hourglass.strings.R

private val edgeAlignment: Dp = 32.dp

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
                    initial.isBlank() || initial == "0" -> 1f
                    else -> 8f
                },
                label = "StartWeight"
            )
            val endWeight = animateFloatAsState(
                targetValue = when {
                    finishing.isBlank() || finishing == "0" -> 1f
                    else -> 8f
                },
                label = "EndWeight"
            )

            Input(
                modifier = Modifier
                    .weight(startWeight.value)
                    .imePadding(),
                initial = initial,
                inputUpdated = initialUpdated,
                keyboardType = KeyboardType.Number,
                hint = "0"
            )
            Spacer(Modifier.width(4.dp))
            Input(
                modifier = Modifier
                    .weight(endWeight.value)
                    .imePadding(),
                initial = finishing,
                inputUpdated = finishingUpdated,
                keyboardType = KeyboardType.Number,
                hint = ""
            )
        }
    }
}