package tmg.hourglass.presentation.views

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import tmg.hourglass.presentation.AppTheme
import tmg.hourglass.presentation.AppThemePreview
import tmg.hourglass.presentation.utils.MeasureTextWidth

@Composable
fun ProgressBar(
    endProgress: Float,
    label: (Float) -> String,
    modifier: Modifier = Modifier,
    animationDuration: Int = 40000,
    textPadding: Dp = AppTheme.dimensions.paddingSmall,
    barColor: Color = AppTheme.colors.primary,
    barOnColor: Color = Color.White,
    backgroundColor: Color = AppTheme.colors.backgroundPrimary,
    backgroundOnColor: Color = AppTheme.colors.textPrimary,
) {

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(AppTheme.dimensions.radiusSmall))
    ) {
        val progress by animateFloatAsState(
            visibilityThreshold = 0.0f,
            targetValue = endProgress,
            animationSpec = tween(durationMillis = animationDuration, easing = LinearEasing, delayMillis = 5000)
        )

        LinearProgressIndicator()

        Box(
            modifier = Modifier
                .size(
                    width = maxWidth,
                    height = maxHeight
                )
                .background(backgroundColor)
        )

        Box(
            modifier = Modifier
                .size(
                    width = maxWidth * progress,
                    height = maxHeight
                )
                .clip(RoundedCornerShape(AppTheme.dimensions.radiusSmall))
                .background(barColor)
        )

        MeasureTextWidth(
            text = label(endProgress),
            modifier = Modifier
                .align(Alignment.CenterStart)
        ) { textWidth ->

            val onBar = when {
                maxWidth * progress > ((textPadding * 2) + textWidth) -> true
                else -> false
            }

            Text(
                text = label(progress),
                style = AppTheme.typography.body1.copy(
                    color = when (onBar) {
                        true -> barOnColor
                        false -> backgroundOnColor
                    }
                ),
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .offset(
                        x = when (onBar) {
                            true -> (maxWidth * progress) - (textPadding + textWidth)
                            false -> (textPadding * 3) + textWidth
                        }
                    )
            )
        }
    }
}

@Preview
@Composable
private fun Preview10() {
    AppThemePreview(isLight = true) {
        ProgressBar(
            endProgress = 0.1f,
            label = { "$it" }
        )
    }
}

@Preview
@Composable
private fun Preview50() {
    AppThemePreview(isLight = true) {
        ProgressBar(
            endProgress = 0.5f,
            label = { "$it" }
        )
    }
}


@Preview
@Composable
private fun Preview95() {
    AppThemePreview(isLight = true) {
        ProgressBar(
            endProgress = 0.95f,
            label = { "$it" }
        )
    }
}
