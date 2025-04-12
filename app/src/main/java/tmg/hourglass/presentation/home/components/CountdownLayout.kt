package tmg.hourglass.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import java.time.LocalDateTime
import tmg.hourglass.domain.enums.CountdownType
import tmg.hourglass.domain.model.Countdown
import tmg.hourglass.domain.model.preview
import tmg.hourglass.domain.utils.ProgressUtils
import tmg.hourglass.presentation.AppTheme
import tmg.hourglass.presentation.AppThemePreview
import tmg.hourglass.presentation.PreviewTheme
import tmg.hourglass.presentation.textviews.TextBody1
import tmg.hourglass.presentation.textviews.TextBody2
import tmg.hourglass.presentation.utils.DeleteDialog
import tmg.hourglass.presentation.utils.darken
import tmg.hourglass.presentation.views.ProgressBar
import tmg.hourglass.strings.R
import tmg.hourglass.strings.R.string

@Composable
fun Countdown(
    countdown: Countdown,
    editClicked: ((countdown: Countdown) -> Unit)?,
    deleteClicked: ((countdown: Countdown) -> Unit)?,
    modifier: Modifier = Modifier,
    now: LocalDateTime = LocalDateTime.now()
) {
    val deleteConfirm = remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = AppTheme.dimensions.paddingMedium,
                top = AppTheme.dimensions.paddingSmall,
                bottom = AppTheme.dimensions.paddingMedium,
                end = AppTheme.dimensions.paddingMedium
            )
            .clip(RoundedCornerShape(AppTheme.dimensions.radiusMedium))
            .background(AppTheme.colors.backgroundSecondary)
            .padding(
                start = AppTheme.dimensions.paddingMedium,
                top = AppTheme.dimensions.paddingSmall,
                bottom = AppTheme.dimensions.paddingMedium
            )
    ) {
        Row(
            modifier = Modifier
                .height(IntrinsicSize.Min)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                TextBody1(
                    text = countdown.name,
                    bold = true
                )
                if (countdown.description.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    TextBody2(
                        text = countdown.description,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
            }
            if (editClicked != null) {
                IconButton(onClick = { editClicked.invoke(countdown) }) {
                    Icon(
                        Icons.Outlined.Edit,
                        contentDescription = stringResource(id = R.string.ab_edit),
                        tint = AppTheme.colors.textPrimary
                    )
                }
            }
            if (deleteClicked != null) {
                IconButton(onClick = { deleteConfirm.value = true }) {
                    Icon(
                        Icons.Outlined.Delete,
                        contentDescription = stringResource(id = R.string.ab_delete),
                        tint = AppTheme.colors.textPrimary
                    )
                }
            }
        }
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(end = AppTheme.dimensions.paddingMedium)
        ) {
            if (countdown.countdownType != CountdownType.DAYS) {
                Row {
                    TextBody2(countdown.countdownType.converter(countdown.startValue.toIntOrNull()?.toString() ?: ""))
                    Spacer(Modifier.weight(1f))
                    TextBody2(countdown.countdownType.converter(countdown.endValue.toIntOrNull()?.toString() ?: ""))
                }
            }
    //        TextBody2(
    //            text = stringResource(
    //                R.string.home_no_description,
    //                countdown.countdownType.converter(countdown.initial.toIntOrNull()?.toString() ?: ""),
    //                countdown.startAtStartOfDay.format("dd MMM yyyy"),
    //                countdown.countdownType.converter(countdown.finishing.toIntOrNull()?.toString() ?: ""),
    //                countdown.endAtStartOfDay.format("dd MMM yyyy")
    //            ).htmlEncode()
    //        )
            Spacer(modifier = Modifier.height(4.dp))
            ProgressBar(
                barColor = Color(countdown.colour.toColorInt()),
                backgroundColor = AppTheme.colors.backgroundTertiary,
                endProgress = ProgressUtils.getProgress(countdown, now),
                label = { progress ->
                    countdown.getProgress(progress = progress)
                }
            )
        }
    }

    if (deleteConfirm.value) {
        DeleteDialog(
            confirmed = { deleteClicked?.invoke(countdown) },
            dismissed = { deleteConfirm.value = false },
            title = string.dashboard_delete_confirmation_title,
            subtitle = string.dashboard_delete_confirmation_message
        )
    }
}

@PreviewTheme
@Composable
private fun PreviewDays() {
    AppThemePreview {
        Countdown(
            countdown = Countdown.preview(),
            editClicked = { },
            deleteClicked = { },
            now = LocalDateTime.of(2021, 1, 28, 12, 34)
        )
    }
}

@PreviewTheme
@Composable
private fun PreviewOther() {
    AppThemePreview {
        Countdown(
            countdown = Countdown.preview().copy(countdownType = CountdownType.NUMBER),
            editClicked = { },
            deleteClicked = { },
            now = LocalDateTime.of(2021, 1, 28, 12, 34)
        )
    }
}