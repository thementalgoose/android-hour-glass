package tmg.hourglass.dashboard.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import org.threeten.bp.LocalDateTime
import tmg.hourglass.R
import tmg.hourglass.domain.model.Countdown
import tmg.hourglass.domain.model.preview
import tmg.hourglass.presentation.AppTheme
import tmg.hourglass.presentation.AppThemePreview
import tmg.hourglass.presentation.textviews.TextBody1
import tmg.hourglass.presentation.textviews.TextBody2
import tmg.hourglass.presentation.textviews.TextHeader2
import tmg.hourglass.presentation.views.ProgressBar
import tmg.hourglass.utils.ProgressUtils
import tmg.utilities.extensions.format
import tmg.utilities.utils.ColorUtils.Companion.darken

@Composable
fun DashboardFavouriteLayout(
    countdown: Countdown,
    editClicked: (id: String) -> Unit,
    deleteClicked: (id: String) -> Unit,
    modifier: Modifier = Modifier,
    now: LocalDateTime = LocalDateTime.now()
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = AppTheme.dimensions.paddingMedium,
                end = AppTheme.dimensions.paddingMedium,
                top = AppTheme.dimensions.paddingMedium,
                bottom = AppTheme.dimensions.paddingMedium
            )
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(AppTheme.dimensions.radiusSmall))
                .background(Color(countdown.colour.toColorInt()))
                .padding(
                    top = AppTheme.dimensions.paddingMedium,
                    bottom = 20.dp
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = AppTheme.dimensions.paddingMedium,
                        end = 4.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    TextHeader2(
                        text = countdown.name,
                        style = AppTheme.typography.h2.copy(
                            color = Color.White
                        )
                    )
                }
                IconButton(
                    onClick = {
                        editClicked(countdown.id)
                    }
                ) {
                    Icon(Icons.Outlined.Edit, tint = Color.White, contentDescription = stringResource(id = R.string.ab_edit))
                }
                IconButton(
                    onClick = {
                        deleteClicked(countdown.id)
                    }
                ) {
                    Icon(Icons.Outlined.Delete, tint = Color.White, contentDescription = stringResource(id = R.string.ab_delete))
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            TextBody1(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = AppTheme.dimensions.paddingMedium,
                        end = AppTheme.dimensions.paddingMedium
                    ),
                text = countdown.description,
                style = AppTheme.typography.body1.copy(
                    color = Color.White
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = AppTheme.dimensions.paddingMedium,
                        end = AppTheme.dimensions.paddingMedium
                    ),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextBody2(
                    text = countdown.countdownType.converter(countdown.initial),
                    style = AppTheme.typography.body2.copy(
                        color = Color.White
                    )
                )
                TextBody2(
                    text = countdown.countdownType.converter(countdown.finishing),
                    style = AppTheme.typography.body2.copy(
                        color = Color.White
                    )
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = AppTheme.dimensions.paddingMedium,
                        end = AppTheme.dimensions.paddingMedium
                    ),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextBody2(
                    text = countdown.startByType.format("d MMM yy"),
                    style = AppTheme.typography.body2.copy(
                        color = Color.White
                    )
                )
                TextBody2(
                    text = countdown.endByType.format("d MMM yy"),
                    style = AppTheme.typography.body2.copy(
                        color = Color.White
                    )
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            ProgressBar(
                modifier = Modifier.padding(
                    start = AppTheme.dimensions.paddingMedium,
                    end = AppTheme.dimensions.paddingMedium
                ),
                height = 48.dp,
                backgroundColor = Color(darken(countdown.colour.toColorInt())),
                backgroundOnColor = Color(0xFFE8E8E8),
                barColor = Color.White,
                barOnColor = Color(0xFF181818),
                endProgress = ProgressUtils.getProgress(countdown, now),
                label = { progress ->
                    countdown.getProgress(progress = progress)
                }
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    AppThemePreview {
        DashboardFavouriteLayout(
            editClicked = { },
            deleteClicked = { },
            countdown = Countdown.preview(),
            now = LocalDateTime.of(2022, 1, 29, 14, 0)
        )
    }
}