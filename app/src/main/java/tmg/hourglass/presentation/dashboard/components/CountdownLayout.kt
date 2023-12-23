package tmg.hourglass.presentation.dashboard.components

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
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import androidx.core.text.htmlEncode
import org.threeten.bp.LocalDateTime
import tmg.hourglass.domain.model.Countdown
import tmg.hourglass.domain.model.preview
import tmg.hourglass.presentation.AppTheme
import tmg.hourglass.presentation.AppThemePreview
import tmg.hourglass.presentation.textviews.TextBody1
import tmg.hourglass.presentation.textviews.TextBody2
import tmg.hourglass.presentation.utils.DeleteDialog
import tmg.hourglass.presentation.views.ProgressBar
import tmg.hourglass.strings.R
import tmg.hourglass.strings.R.string
import tmg.hourglass.utils.ProgressUtils
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
        TextBody2(
            text = stringResource(
                R.string.home_no_description,
                countdown.countdownType.converter(countdown.initial.toIntOrNull()?.toString() ?: ""),
                countdown.start.format("dd MMM yyyy"),
                countdown.countdownType.converter(countdown.finishing.toIntOrNull()?.toString() ?: ""),
                countdown.end.format("dd MMM yyyy")
            ).htmlEncode()
        )
        Spacer(modifier = Modifier.height(4.dp))
        ProgressBar(
            barColor = Color(countdown.colour.toColorInt()),
            backgroundColor = AppTheme.colors.backgroundSecondary,
            endProgress = ProgressUtils.getProgress(countdown, now),
            label = { progress ->
                countdown.getProgress(progress = progress)
            }
        )
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

@Preview
@Composable
private fun Preview() {
    AppThemePreview {
        Countdown(
            countdown = Countdown.preview(),
            editClicked = { },
            deleteClicked = { },
            now = LocalDateTime.of(2021, 1, 28, 12, 34)
        )
    }
}