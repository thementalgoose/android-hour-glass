package tmg.hourglass.presentation.modify.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.threeten.bp.LocalDateTime
import tmg.hourglass.domain.enums.CountdownType
import tmg.hourglass.presentation.AppTheme
import tmg.hourglass.presentation.AppThemePreview
import tmg.hourglass.presentation.PreviewTheme
import tmg.hourglass.presentation.pickers.DatePicker
import tmg.hourglass.presentation.textviews.TextBody1
import tmg.hourglass.presentation.textviews.TextHeader2
import tmg.hourglass.strings.R
import tmg.utilities.extensions.format

@Composable
fun DataSingleDateLayout(
    date: LocalDateTime?,
    dateUpdated: (LocalDateTime) -> Unit,
    modifier: Modifier = Modifier
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
        TextBody1(
            text = when (date) {
                null -> stringResource(id = R.string.modify_field_date_placeholder)
                else -> date.format("dd MMM yyyy")
            },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(AppTheme.dimensions.radiusSmall))
                .background(AppTheme.colors.backgroundSecondary)
                .clickable(
                    onClick = {
                        showDialog.value = true
                    }
                )
                .padding(AppTheme.dimensions.paddingMedium)
        )
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