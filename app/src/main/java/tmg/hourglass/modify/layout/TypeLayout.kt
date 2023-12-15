package tmg.hourglass.modify.layout

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import tmg.hourglass.strings.R.string
import tmg.hourglass.domain.enums.CountdownType
import tmg.hourglass.extensions.label
import tmg.hourglass.presentation.AppTheme
import tmg.hourglass.presentation.AppThemePreview
import tmg.hourglass.presentation.textviews.TextBody1
import tmg.hourglass.presentation.textviews.TextHeader2

@Composable
fun TypeLayout(
    type: CountdownType,
    typeUpdated: (CountdownType) -> Unit,
    modifier: Modifier = Modifier
) {
    val openDialog = remember { mutableStateOf(false) }

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
        TextHeader2(text = stringResource(id = string.modify_field_type))
        Spacer(modifier = Modifier.height(8.dp))
        TextBody1(text = stringResource(id = string.modify_field_type_desc))
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(AppTheme.dimensions.radiusSmall))
                .background(AppTheme.colors.backgroundSecondary)
                .clickable(
                    onClick = {
                        openDialog.value = true
                    },
                )
                .padding(
                    start = AppTheme.dimensions.paddingMedium,
                    end = AppTheme.dimensions.paddingMedium,
                    top = AppTheme.dimensions.paddingMedium,
                    bottom = AppTheme.dimensions.paddingMedium
                )
        ) {
            TextBody1(
                text = stringResource(id = type.label()),
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .weight(1f)
            )
        }
    }

    if (openDialog.value) {
        TypeDialog(
            typeUpdated = typeUpdated,
            dismissed = { openDialog.value = false }
        )
    }
}

@Composable
private fun TypeDialog(
    typeUpdated: (CountdownType) -> Unit,
    dismissed: () -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(
        properties = DialogProperties(),
        onDismissRequest = dismissed,
        content = {
            Column(
                modifier = modifier
                    .background(AppTheme.colors.backgroundSecondary)
                    .padding(
                        start = AppTheme.dimensions.paddingSmall,
                        end = AppTheme.dimensions.paddingSmall,
                        top = AppTheme.dimensions.paddingMedium,
                        bottom = AppTheme.dimensions.paddingMedium
                    )
                    .verticalScroll(rememberScrollState())
            ) {
                TextHeader2(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = AppTheme.dimensions.paddingMedium,
                            top = AppTheme.dimensions.paddingMedium,
                            bottom = AppTheme.dimensions.paddingMedium,
                            end = AppTheme.dimensions.paddingMedium
                        ),
                    text = stringResource(id = string.modify_field_type)
                )
                CountdownType.values().forEach {
                    TextButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            typeUpdated(it)
                            dismissed()
                        }
                    ) {
                        TextBody1(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    start = AppTheme.dimensions.paddingSmall,
                                    end = AppTheme.dimensions.paddingSmall
                                ),
                            text = stringResource(it.label())
                        )
                    }
                }
            }
        }
    )
}

@Preview
@Composable
private fun Preview() {
    AppThemePreview {
        TypeLayout(
            type = CountdownType.GRAMS,
            typeUpdated = { }
        )
    }
}