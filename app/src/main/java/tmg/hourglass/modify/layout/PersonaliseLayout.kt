package tmg.hourglass.modify.layout

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.graphics.toColor
import androidx.core.graphics.toColorInt
import tmg.hourglass.R
import tmg.hourglass.domain.enums.CountdownColors
import tmg.hourglass.presentation.AppTheme
import tmg.hourglass.presentation.AppThemePreview
import tmg.hourglass.presentation.inputs.Input
import tmg.hourglass.presentation.textviews.TextBody1
import tmg.hourglass.presentation.textviews.TextHeader2
import tmg.hourglass.widget.WidgetBarColours

@Composable
fun PersonaliseLayout(
    name: String,
    nameUpdated: (String) -> Unit,
    description: String,
    descriptionUpdated: (String) -> Unit,
    color: String,
    colorPicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    val colorPicker = remember { mutableStateOf(false) }

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
        TextHeader2(text = stringResource(id = R.string.modify_field_name))
        Spacer(modifier = Modifier.height(8.dp))
        TextBody1(text = stringResource(id = R.string.modify_field_name_desc))
        Spacer(modifier = Modifier.height(8.dp))
        Input(
            initial = name,
            inputUpdated = nameUpdated,
            hint = stringResource(id = R.string.modify_field_name_hint)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Input(
            initial = description,
            inputUpdated = descriptionUpdated,
            hint = stringResource(id = R.string.modify_field_description_hint)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(AppTheme.dimensions.radiusSmall))
                .background(AppTheme.colors.backgroundSecondary)
                .clickable(
                    onClick = {
                        colorPicker.value = true
                    }
                )
                .padding(
                    start = AppTheme.dimensions.paddingMedium,
                    end = AppTheme.dimensions.paddingMedium,
                    top = AppTheme.dimensions.paddingMedium,
                    bottom = AppTheme.dimensions.paddingMedium
                )
        ) {
            TextBody1(
                text = stringResource(id = R.string.modify_field_colour_hint),
                modifier = Modifier
                    .padding(
                        end = AppTheme.dimensions.paddingMedium
                    )
                    .align(Alignment.CenterVertically)
                    .weight(1f)
            )
            Box(
                modifier = Modifier
                    .size(
                        width = 24.dp,
                        height = 24.dp
                    )
                    .clip(RoundedCornerShape(AppTheme.dimensions.radiusSmall))
                    .background(Color(color.toColorInt()))
            )
        }
    }

    if (colorPicker.value) {
        ColorPicker(
            colorPicked = colorPicked,
            dismiss = {
                colorPicker.value = false
            }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ColorPicker(
    colorPicked: (String) -> Unit,
    dismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = dismiss,
        properties = DialogProperties(),
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AppTheme.colors.backgroundSecondary)
            ) {
                TextHeader2(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = AppTheme.dimensions.paddingMedium,
                            top = AppTheme.dimensions.paddingMedium,
                            end = AppTheme.dimensions.paddingMedium
                        ),
                    text = stringResource(id = R.string.modify_field_colour_hint)
                )
                LazyVerticalGrid(
                    cells = GridCells.Fixed(5),
                    contentPadding = PaddingValues(AppTheme.dimensions.paddingMedium),
                    content = {
                        items(CountdownColors.values().toList()) {
                            Box(
                                modifier = Modifier
                                    .height(64.dp)
                                    .padding(AppTheme.dimensions.paddingSmall)
                                    .background(Color(it.hex.toColorInt()))
                                    .clickable(
                                        onClick = {
                                            colorPicked(it.hex)
                                            dismiss()
                                        }
                                    )
                            )
                        }
                    }
                )
            }
        }
    )
}

@Preview
@Composable
private fun Preview() {
    AppThemePreview {
        PersonaliseLayout(
            name = "name",
            nameUpdated = { },
            description = "description",
            descriptionUpdated = { },
            color = "#263892",
            colorPicked = { }
        )
    }
}