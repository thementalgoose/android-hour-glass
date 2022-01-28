package tmg.hourglass.dashboard.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import org.threeten.bp.LocalDateTime
import tmg.hourglass.domain.model.Countdown
import tmg.hourglass.domain.model.preview
import tmg.hourglass.presentation.AppTheme
import tmg.hourglass.presentation.AppThemePreview
import tmg.hourglass.presentation.textviews.TextBody1
import tmg.hourglass.presentation.textviews.TextHeader2
import tmg.hourglass.presentation.views.ProgressBar
import tmg.hourglass.utils.ProgressUtils
import tmg.utilities.utils.ColorUtils.Companion.darken

@Composable
fun DashboardFavouriteLayout(
    countdown: Countdown,
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
                    start = AppTheme.dimensions.paddingMedium,
                    end = AppTheme.dimensions.paddingMedium,
                    top = AppTheme.dimensions.paddingMedium,
                    bottom = AppTheme.dimensions.paddingMedium
                )
        ) {
            TextHeader2(
                text = countdown.name,
                style = AppTheme.typography.h2.copy(
                    color = Color.White
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            TextBody1(
                text = countdown.description,
                style = AppTheme.typography.body1.copy(
                    color = Color.White
                )
            )
            Spacer(modifier = Modifier.height(8.dp))

            ProgressBar(
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
            countdown = Countdown.preview()
        )
    }
}