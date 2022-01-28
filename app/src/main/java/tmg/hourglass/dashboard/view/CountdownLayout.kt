package tmg.hourglass.dashboard.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import androidx.core.text.htmlEncode
import org.threeten.bp.LocalDateTime
import tmg.hourglass.R
import tmg.hourglass.domain.model.Countdown
import tmg.hourglass.domain.model.preview
import tmg.hourglass.presentation.AppTheme
import tmg.hourglass.presentation.AppThemePreview
import tmg.hourglass.presentation.textviews.TextBody1
import tmg.hourglass.presentation.textviews.TextBody2
import tmg.hourglass.presentation.views.ProgressBar
import tmg.hourglass.utils.ProgressUtils.Companion.getProgress
import tmg.utilities.extensions.format

@Composable
fun DashboardCountdownLayout(
    countdown: Countdown,
    editClicked: (id: String) -> Unit,
    deleteClicked: (id: String) -> Unit,
    modifier: Modifier = Modifier,
    now: LocalDateTime = LocalDateTime.now()
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = AppTheme.dimensions.paddingMedium,
                top = AppTheme.dimensions.paddingSmall,
                bottom = AppTheme.dimensions.paddingMedium,
                end = AppTheme.dimensions.paddingMedium
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                TextBody1(
                    text = countdown.name,
                    bold = true
                )
                Spacer(modifier = Modifier.height(4.dp))
                TextBody2(
                    text = countdown.description,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            IconButton(
                onClick = {
                    editClicked(countdown.id)
                }
            ) {
                Icon(Icons.Outlined.Edit, contentDescription = stringResource(id = R.string.ab_edit))
            }
            IconButton(
                onClick = {
                    deleteClicked(countdown.id)
                }
            ) {
                Icon(Icons.Outlined.Delete, contentDescription = stringResource(id = R.string.ab_delete))
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
            endProgress = getProgress(countdown, now),
            label = { progress ->
                countdown.getProgress(progress = progress)
            }
        )
    }
}

@Preview
@Composable
private fun Preview() {
    AppThemePreview {
        DashboardCountdownLayout(
            countdown = Countdown.preview(),
            editClicked = { },
            deleteClicked = { },
            now = LocalDateTime.of(2021, 1, 28, 12, 34)
        )
    }
}