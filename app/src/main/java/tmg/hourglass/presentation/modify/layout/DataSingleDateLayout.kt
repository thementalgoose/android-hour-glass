package tmg.hourglass.presentation.modify.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import tmg.hourglass.presentation.AppTheme
import tmg.hourglass.presentation.AppThemePreview
import tmg.hourglass.presentation.PreviewTheme
import tmg.hourglass.presentation.pickers.DatePicker
import tmg.hourglass.presentation.textviews.TextBody1
import tmg.hourglass.presentation.textviews.TextBody2
import tmg.hourglass.presentation.textviews.TextHeader2
import tmg.hourglass.strings.R
import tmg.utilities.extensions.format
import java.time.LocalDateTime

@Composable
fun DataSingleDateLayout(
    date: LocalDateTime?,
    dateUpdated: (LocalDateTime) -> Unit,
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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(AppTheme.dimensions.radiusSmall))
                .background(AppTheme.colors.backgroundSecondary)
                .clickable(
                    onClick = {
                        showDialog.value = true
                    }
                )
        ) {
            TextBody1(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = if (error != null) AppTheme.colors.errorColor else Color.Transparent,
                        shape = RoundedCornerShape(AppTheme.dimensions.radiusSmall)
                    )
                    .padding(AppTheme.dimensions.paddingMedium),
                text = when (date) {
                    null -> stringResource(id = R.string.modify_field_date_placeholder)
                    else -> date.format("dd MMM yyyy")
                })
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
        DatePicker(
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
        DataSingleDateLayout(
            date = LocalDateTime.of(2025, 1, 1, 1, 1),
            dateUpdated = { },
        )
    }
}

@PreviewTheme
@Composable
private fun PreviewError() {
    AppThemePreview {
        DataSingleDateLayout(
            date = LocalDateTime.of(2025, 1, 1, 1, 1),
            dateUpdated = { },
            error = "Help me bro"
        )
    }
}