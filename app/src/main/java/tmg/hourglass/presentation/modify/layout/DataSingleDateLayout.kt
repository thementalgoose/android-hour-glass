package tmg.hourglass.presentation.modify.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Updater
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import tmg.hourglass.domain.enums.CountdownType
import tmg.hourglass.domain.extensions.label
import tmg.hourglass.presentation.AppTheme
import tmg.hourglass.presentation.AppThemePreview
import tmg.hourglass.presentation.PreviewTheme
import tmg.hourglass.presentation.inputs.Input
import tmg.hourglass.presentation.pickers.DatePicker
import tmg.hourglass.presentation.textviews.TextBody1
import tmg.hourglass.presentation.textviews.TextBody2
import tmg.hourglass.presentation.textviews.TextHeader2
import tmg.hourglass.strings.R
import tmg.hourglass.strings.R.string
import tmg.utilities.extensions.format
import tmg.utilities.extensions.ordinalAbbreviation
import java.time.LocalDateTime
import java.time.Month
import java.time.format.TextStyle
import java.time.format.TextStyle.SHORT_STANDALONE
import java.util.Locale

@Composable
fun DataSingleDateLayout(
    day: String?,
    month: Month?,
    year: String?,
    dayUpdated: (String) -> Unit,
    monthUpdated: (Month) -> Unit,
    yearUpdated: (String) -> Unit,
    modifier: Modifier = Modifier,
    error: String? = null,
) {
    val showDialog = remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(AppTheme.dimensions.paddingMedium)
    ) {
        TextHeader2(text = stringResource(id = R.string.modify_field_date))
        Spacer(modifier = Modifier.height(8.dp))
        TextBody1(text = stringResource(id = R.string.modify_field_date_desc))
        Spacer(modifier = Modifier.height(8.dp))

        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.paddingMedium)
            ) {
                Input(
                    modifier = Modifier
                        .weight(1f)
                        .border(
                            width = 1.dp,
                            color = if (error != null) AppTheme.colors.errorColor else Color.Transparent,
                            shape = RoundedCornerShape(AppTheme.dimensions.radiusSmall),
                        ),
                    maxLines = 1,
                    initial = day.orEmpty(),
                    inputUpdated = dayUpdated,
                    keyboardType = KeyboardType.Number,
                    hint = stringResource(id = string.modify_field_day)
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable {
                            showDialog.value = true
                        }
                        .border(
                            width = 1.dp,
                            color = if (error != null) AppTheme.colors.errorColor else Color.Transparent,
                            shape = RoundedCornerShape(AppTheme.dimensions.radiusSmall),
                        )
                        .clip(RoundedCornerShape(AppTheme.dimensions.radiusSmall))
                        .background(AppTheme.colors.backgroundSecondary)
                        .padding(AppTheme.dimensions.paddingMedium)
                ) {
                    TextBody1(
                        modifier = Modifier.align(Alignment.CenterStart),
                        text = when (month) {
                            null -> "-"
                            else -> month.getDisplayName(
                                SHORT_STANDALONE,
                                Locale.getDefault()
                            )
                        }
                    )
                }
                Input(
                    modifier = Modifier
                        .weight(1f).border(
                            width = 1.dp,
                            color = if (error != null && !year.isNullOrBlank()) AppTheme.colors.errorColor else Color.Transparent,
                            shape = RoundedCornerShape(AppTheme.dimensions.radiusSmall),
                        ),
                    maxLines = 1,
                    initial = year.orEmpty(),
                    inputUpdated = yearUpdated,
                    keyboardType = KeyboardType.Number,
                    hint = stringResource(id = string.modify_field_year)
                )
            }
            if (error != null) {
                TextBody2(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = AppTheme.dimensions.paddingMedium,
                            end = AppTheme.dimensions.paddingMedium,
                            bottom = 4.dp,
                            top = 2.dp
                        ),
                    textColor = AppTheme.colors.appColors.error,
                    text = error
                )
            }
        }
    }

    if (showDialog.value) {
        MonthDialog(
            monthUpdated = monthUpdated,
            dismissed = { showDialog.value = false }
        )
    }
}


@Composable
private fun MonthDialog(
    monthUpdated: (Month) -> Unit,
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
                Month.entries.forEach {
                    TextButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            monthUpdated(it)
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
                            text = it.getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault())
                        )
                    }
                }
            }
        }
    )
}


@PreviewTheme
@Composable
private fun Preview() {
    AppThemePreview {
        DataSingleDateLayout(
            day = "11",
            month = Month.FEBRUARY,
            year = null,
            dayUpdated = { },
            monthUpdated = { },
            yearUpdated = { },
        )
    }
}

@PreviewTheme
@Composable
private fun PreviewError() {
    AppThemePreview {
        DataSingleDateLayout(
            day = "41",
            month = Month.FEBRUARY,
            year = null,
            dayUpdated = { },
            monthUpdated = { },
            yearUpdated = { },
            error = "Help me bro"
        )
    }
}