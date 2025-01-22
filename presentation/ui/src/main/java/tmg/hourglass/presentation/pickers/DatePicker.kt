package tmg.hourglass.presentation.pickers

import android.widget.CalendarView
import androidx.appcompat.view.ContextThemeWrapper
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneOffset
import org.threeten.bp.format.DateTimeFormatter
import tmg.hourglass.presentation.AppTheme
import tmg.hourglass.presentation.R
import tmg.hourglass.presentation.textviews.TextBody1
import tmg.hourglass.strings.R.string

@Composable
fun DatePicker(
    minDate: LocalDate? = null,
    maxDate: LocalDate? = null,
    currentlySelectedDate: LocalDate? = null,
    onDateSelected: (LocalDate) -> Unit, onDismissRequest: () -> Unit
) {
    val selDate = remember { mutableStateOf(LocalDate.now()) }

    Dialog(onDismissRequest = { onDismissRequest() }, properties = DialogProperties()) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .background(
                    color = AppTheme.colors.backgroundPrimary,
                    shape = RoundedCornerShape(size = 16.dp)
                )
        ) {
            Column(
                Modifier
                    .defaultMinSize(minHeight = 72.dp)
                    .fillMaxWidth()
                    .background(
                        color = AppTheme.colors.backgroundSecondary,
                        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                    )
                    .padding(16.dp)
            ) {
                TextBody1(text = stringResource(id = string.select_date))

                Spacer(modifier = Modifier.size(24.dp))

                TextBody1(
                    text = selDate.value.format(DateTimeFormatter.ofPattern("MMM d, YYYY")),
                    style = AppTheme.typography.body1.copy(
                        fontSize = 24.sp,
                        color = AppTheme.colors.textPrimary
                    )
                )

                Spacer(modifier = Modifier.size(16.dp))
            }

            CustomCalendarView(
                minDate = minDate,
                maxDate = maxDate,
                currentlySelectedDate = currentlySelectedDate,
                onDateSelected = {
                    selDate.value = it
                }
            )

            Spacer(modifier = Modifier.size(8.dp))

            Row(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(bottom = 16.dp, end = 16.dp)
            ) {
                TextButton(
                    onClick = onDismissRequest
                ) {
                    TextBody1(text = stringResource(id = string.select_date_cancel))
                }

                TextButton(
                    onClick = {
                        onDateSelected(selDate.value)
                        onDismissRequest()
                    }
                ) {
                    TextBody1(text = stringResource(id = string.select_date_confirm))
                }
            }
        }
    }
}

@Composable
fun CustomCalendarView(
    minDate: LocalDate? = null,
    maxDate: LocalDate? = null,
    currentlySelectedDate: LocalDate? = null,
    onDateSelected: (LocalDate) -> Unit
) {
    val initialDate = remember { mutableStateOf(currentlySelectedDate ?: LocalDate.now())}

    // Adds view to Compose
    AndroidView(
        modifier = Modifier.wrapContentSize(),
        factory = { context ->
            CalendarView(ContextThemeWrapper(context, R.style.CalenderViewCustom)).apply {}
        },
        update = { view ->
            view.weekDayTextAppearance = Int.MAX_VALUE

            if (minDate != null) {
                view.minDate = minDate.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()
            }
            if (maxDate != null) {
                view.maxDate = maxDate.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()
            }

            val millis = initialDate.value.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()
            view.setDate(millis, true, true)

            view.setOnDateChangeListener { _, year, month, dayOfMonth ->
                val date = LocalDate
                    .now()
                    .withMonth(month + 1)
                    .withYear(year)
                    .withDayOfMonth(dayOfMonth)
                initialDate.value = date
                onDateSelected(date)
            }
        }
    )
}