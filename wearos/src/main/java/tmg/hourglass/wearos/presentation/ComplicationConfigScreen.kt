package tmg.hourglass.wearos.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.foundation.rememberSwipeToDismissBoxState
import androidx.wear.compose.material.Card
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.SwipeToDismissBox
import tmg.hourglass.domain.enums.CountdownType
import tmg.hourglass.domain.model.Countdown
import tmg.hourglass.domain.model.preview
import tmg.hourglass.wearos.R
import tmg.hourglass.wearos.style.PreviewWearOS
import tmg.hourglass.wearos.style.WearTheme
import tmg.hourglass.wearos.text.TextBody1
import tmg.hourglass.wearos.text.TextBody2
import tmg.hourglass.wearos.text.TextBodyTitle

@Composable
fun ComplicationConfigScreen(
    countdowns: List<Countdown>,
    onCountdownSelected: (Countdown) -> Unit,
    onDismiss: () -> Unit,
    onRefresh: () -> Unit = {},
    isLight: Boolean = !isSystemInDarkTheme()
) {
    val swipeState = rememberSwipeToDismissBoxState()

    SwipeToDismissBox(
        state = swipeState,
        onDismissed = onDismiss
    ) { isBackground ->
        if (!isBackground) {
            val listState = rememberScalingLazyListState()

            Scaffold(
                positionIndicator = {
                    PositionIndicator(scalingLazyListState = listState)
                }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(WearTheme.colors.backgroundPrimary)
                ) {
                    ScalingLazyColumn(
                        state = listState,
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 24.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        item {
                            TextBodyTitle(
                                text = stringResource(id = R.string.select_countdown),
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 8.dp)
                            )
                        }
                        if (countdowns.isEmpty()) {
                            item {
                                TextBody1(
                                    text = stringResource(id = R.string.no_countdowns_synced),
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 12.dp)
                                )
                            }
                            item {
                                Chip(
                                    onClick = onRefresh,
                                    colors = ChipDefaults.primaryChipColors(),
                                    label = {
                                        TextBody1(
                                            text = stringResource(id = R.string.refresh),
                                            textAlign = TextAlign.Center,
                                            bold = true,
                                            textColor = WearTheme.colors.wearColors.onPrimary,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 4.dp)
                                )
                            }
                        } else {
                            items(countdowns) { countdown ->
                                Card(
                                    onClick = { onCountdownSelected(countdown) },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(4.dp)
                                    ) {
                                        val titleText = if (!countdown.emoji.isNullOrBlank()) {
                                            "${countdown.emoji} ${countdown.name}"
                                        } else {
                                            countdown.name
                                        }
                                        TextBody1(
                                            text = titleText,
                                            bold = true
                                        )
                                        if (countdown.description.isNotBlank()) {
                                            Spacer(modifier = Modifier.height(2.dp))
                                            TextBody2(
                                                text = countdown.description
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@PreviewWearOS
@Composable
private fun Preview() {
    val sampleCountdowns = listOf(
        Countdown.preview(type = CountdownType.DAYS, color = "#F1A16B", emoji = "🚀"),
        Countdown.preview(type = CountdownType.DAYS, color = "#ED835B")
    )

    WearTheme {
        ComplicationConfigScreen(
            countdowns = sampleCountdowns,
            onCountdownSelected = {},
            onDismiss = {}
        )
    }
}

@PreviewWearOS
@Composable
private fun PreviewNoItems() {
    WearTheme {
        ComplicationConfigScreen(
            countdowns = emptyList(),
            onCountdownSelected = {},
            onDismiss = {}
        )
    }
}
