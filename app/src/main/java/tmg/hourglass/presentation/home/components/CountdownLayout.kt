package tmg.hourglass.presentation.home.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import tmg.utilities.extensions.format

@Composable
fun Countdown(
    countdown: Countdown,
    editClicked: ((countdown: Countdown) -> Unit)?,
    deleteClicked: ((countdown: Countdown) -> Unit)?,
    modifier: Modifier = Modifier,
    now: LocalDateTime = LocalDateTime.now()
) {
    val deleteConfirm = remember { mutableStateOf(false) }
    val expanded = remember { mutableStateOf(false) }

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

        if (countdown.countdownType == CountdownType.DAYS) {
            CountdownDays(
                modifier = Modifier.padding(end = AppTheme.dimensions.paddingMedium),
                countdown = countdown,
                now = now
            )
        } else {
            CountdownOther(
                modifier = Modifier.padding(end = AppTheme.dimensions.paddingMedium),
                countdown = countdown,
                now = now,
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

@Composable
private fun CountdownDays(
    countdown: Countdown,
    now: LocalDateTime,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
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

@Composable
private fun CountdownOther(
    countdown: Countdown,
    now: LocalDateTime,
    modifier: Modifier = Modifier
) {
    val expanded = remember { mutableStateOf(false) }
    Column(modifier = modifier) {
        AnimatedVisibility(expanded.value) {
            Row(modifier = Modifier.padding(bottom = 4.dp)) {
                TextBody2(
                    text = countdown.countdownType.converter(countdown.startValue.toIntOrNull()?.toString().orEmpty())
                )
                Spacer(Modifier.weight(1f))
                TextBody2(
                    text = countdown.countdownType.converter(countdown.endValue.toIntOrNull()?.toString().orEmpty())
                )
            }
        }
        ProgressBar(
            barColor = Color(countdown.colour.toColorInt()),
            backgroundColor = AppTheme.colors.backgroundTertiary,
            endProgress = ProgressUtils.getProgress(countdown, now),
            label = { progress ->
                countdown.getProgress(progress = progress)
            },
            modifier = Modifier
                .clickable {
                    expanded.value = !expanded.value
                }
        )
        AnimatedVisibility(expanded.value) {
            Row(modifier = Modifier.padding(top = 4.dp)) {
                TextBody2(
                    text = countdown.startDate.toLocalDate().format("dd MMM yyyy").orEmpty()
                )
                Spacer(Modifier.weight(1f))
                TextBody2(
                    text = countdown.endDate.toLocalDate().format("dd MMM yyyy").orEmpty()
                )
            }
        }
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
            countdown = Countdown.preview(type = CountdownType.NUMBER),
            editClicked = { },
            deleteClicked = { },
            now = LocalDateTime.of(2021, 1, 28, 12, 34)
        )
    }
}