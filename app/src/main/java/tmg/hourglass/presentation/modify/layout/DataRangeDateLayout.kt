package tmg.hourglass.presentation.modify.layout

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.LocalDateTime
import tmg.hourglass.strings.R.string
import tmg.hourglass.presentation.AppTheme
import tmg.hourglass.presentation.AppThemePreview
import tmg.hourglass.presentation.PreviewTheme
import tmg.hourglass.presentation.buttons.PrimaryIconButton
import tmg.hourglass.presentation.buttons.SecondaryIconButton
import tmg.hourglass.presentation.pickers.DatePicker
import tmg.hourglass.presentation.textviews.TextBody1
import tmg.hourglass.presentation.textviews.TextBody2
import tmg.hourglass.presentation.textviews.TextHeader2
import tmg.hourglass.presentation.utils.Fade
import tmg.utilities.extensions.format

private val spacing = 8.dp

@Composable
fun DataRangeDateLayout(
    startDate: LocalDateTime?,
    startDateUpdated: (LocalDateTime) -> Unit,
    endDate: LocalDateTime?,
    endDateUpdated: (LocalDateTime) -> Unit,
    modifier: Modifier = Modifier,
    error: String? = null,
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
        TextHeader2(text = stringResource(id = string.modify_field_dates))
        Spacer(modifier = Modifier.height(8.dp))
        TextBody1(text = stringResource(id = string.modify_field_dates_desc))
        Spacer(modifier = Modifier.height(8.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(spacing)
        ) {
            DateSelection(
                text = string.modify_field_dates_start,
                date = startDate,
                dateUpdated = startDateUpdated
            )
            DateSelection(
                text = string.modify_field_dates_end,
                date = endDate,
                dateUpdated = endDateUpdated,
                minDate = startDate
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

@Composable
private fun ColumnScope.DateSelection(
    @StringRes
    text: Int,
    minDate: LocalDateTime? = null,
    date: LocalDateTime?,
    dateUpdated: (LocalDateTime) -> Unit,
) {
    val showDialog = remember { mutableStateOf(false) }

    Row(modifier = Modifier.fillMaxWidth()) {
        TextBody1(
            text = when (date?.toLocalDate()) {
                null -> stringResource(id = text)
                LocalDate.now() -> stringResource(id = string.modify_field_date_today)
                else -> date.format("dd MMM yyyy")
            },
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(AppTheme.dimensions.radiusSmall))
                .background(AppTheme.colors.backgroundSecondary)
                .clickable(
                    onClick = {
                        showDialog.value = true
                    }
                )
                .padding(AppTheme.dimensions.paddingMedium)
        )
        AnimatedVisibility(
            visible = date?.toLocalDate() != LocalDate.now(),
        ) {
            Row {
                Spacer(Modifier.width(spacing))
                SecondaryIconButton(
                    modifier = Modifier.height(IntrinsicSize.Max),
                    icon = Icons.Outlined.PlayArrow,
                    contentDescription = stringResource(string.modify_field_date_today),
                    onClick = {
                        dateUpdated(LocalDateTime.now())
                    }
                )
            }
        }
    }

    if (showDialog.value) {
        DatePicker(
            minDate = minDate?.toLocalDate(),
            currentlySelectedDate = date?.toLocalDate(),
            onDateSelected = {
                dateUpdated(it.atStartOfDay())
            },
            onDismissRequest = {
                showDialog.value = false
            }
        )
    }
}

@PreviewTheme
@Composable
private fun Preview() {
    AppThemePreview {
        DataRangeDateLayout(
            startDate = null,
            startDateUpdated = { },
            endDate = null,
            endDateUpdated = { }
        )
    }
}

@PreviewTheme
@Composable
private fun PreviewError() {
    AppThemePreview {
        DataRangeDateLayout(
            startDate = null,
            startDateUpdated = { },
            endDate = null,
            endDateUpdated = { },
            error = "We fucked up, sir"
        )
    }
}