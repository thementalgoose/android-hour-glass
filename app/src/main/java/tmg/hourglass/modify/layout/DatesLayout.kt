package tmg.hourglass.modify.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.threeten.bp.LocalDateTime
import tmg.hourglass.R
import tmg.hourglass.presentation.AppTheme
import tmg.hourglass.presentation.AppThemePreview
import tmg.hourglass.presentation.pickers.DatePicker
import tmg.hourglass.presentation.textviews.TextBody1
import tmg.hourglass.presentation.textviews.TextHeader2
import tmg.utilities.extensions.format

private enum class DateDialogType {
    START,
    END
}

@Composable
fun DatesLayout(
    startDate: LocalDateTime?,
    startDateUpdated: (LocalDateTime) -> Unit,
    endDate: LocalDateTime?,
    endDateUpdated: (LocalDateTime) -> Unit,
    modifier: Modifier = Modifier
) {
    val showDialog = remember { mutableStateOf<DateDialogType?>(null) }

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
        TextHeader2(text = stringResource(id = R.string.modify_field_dates))
        Spacer(modifier = Modifier.height(8.dp))
        TextBody1(text = stringResource(id = R.string.modify_field_dates_desc))
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            TextBody1(
                text = when (startDate) {
                    null -> stringResource(id = R.string.modify_field_dates_start)
                    else -> startDate.format("dd MMM yyyy")
                },
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(AppTheme.dimensions.radiusSmall))
                    .background(AppTheme.colors.backgroundSecondary)
                    .alpha(if (startDate == null) 0.6f else 1f)
                    .clickable(
                        onClick = {
                            showDialog.value = DateDialogType.START
                        }
                    )
                    .padding(
                        start = AppTheme.dimensions.paddingMedium,
                        end = AppTheme.dimensions.paddingMedium,
                        top = AppTheme.dimensions.paddingMedium,
                        bottom = AppTheme.dimensions.paddingMedium
                    )
            )
            Spacer(modifier = Modifier.width(AppTheme.dimensions.paddingMedium))
            TextBody1(
                text = when (endDate) {
                    null -> stringResource(id = R.string.modify_field_dates_end)
                    else -> endDate.format("dd MMM yyyy")
                },
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(AppTheme.dimensions.radiusSmall))
                    .background(AppTheme.colors.backgroundSecondary)
                    .alpha(if (endDate == null) 0.6f else 1f)
                    .clickable(
                        onClick = {
                            showDialog.value = DateDialogType.END
                        }
                    )
                    .padding(
                        start = AppTheme.dimensions.paddingMedium,
                        end = AppTheme.dimensions.paddingMedium,
                        top = AppTheme.dimensions.paddingMedium,
                        bottom = AppTheme.dimensions.paddingMedium
                    )
            )
        }
    }

    when (showDialog.value) {
        DateDialogType.START -> {
            DatePicker(
                currentlySelectedDate = startDate?.toLocalDate(),
                onDateSelected = {
                    startDateUpdated(it.atStartOfDay())
                },
                onDismissRequest = {
                    showDialog.value = null
                }
            )
        }
        DateDialogType.END -> {
            DatePicker(
                minDate = startDate?.toLocalDate(),
                currentlySelectedDate = endDate?.toLocalDate(),
                onDateSelected = {
                    endDateUpdated(it.atStartOfDay())
                },
                onDismissRequest = {
                    showDialog.value = null
                }
            )
        }
        null -> { }
    }
}

@Preview
@Composable
private fun Preview() {
    AppThemePreview {
        DatesLayout(
            startDate = null,
            startDateUpdated = { },
            endDate = null,
            endDateUpdated = { }
        )
    }
}