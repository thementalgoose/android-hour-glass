package tmg.hourglass.widgets.presentation.configure

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import androidx.hilt.navigation.compose.hiltViewModel
import java.time.LocalDateTime
import tmg.hourglass.domain.enums.CountdownInterpolator
import tmg.hourglass.domain.enums.CountdownType
import tmg.hourglass.domain.model.Countdown
import tmg.hourglass.domain.utils.ProgressUtils
import tmg.hourglass.presentation.AppTheme
import tmg.hourglass.presentation.AppThemePreview
import tmg.hourglass.presentation.PreviewTheme
import tmg.hourglass.presentation.buttons.PrimaryButton
import tmg.hourglass.presentation.inputs.Switch
import tmg.hourglass.presentation.layouts.TitleBar
import tmg.hourglass.presentation.textviews.TextBody1
import tmg.hourglass.presentation.textviews.TextBody2
import tmg.hourglass.presentation.views.ProgressBar
import tmg.hourglass.strings.R.string
import tmg.hourglass.widgets.R

private val barBackground: Color
    @Composable
    get() = AppTheme.colors.backgroundSecondary.copy(
        red = AppTheme.colors.backgroundSecondary.red * 0.89f,
        blue = AppTheme.colors.backgroundSecondary.blue * 0.89f,
        green = AppTheme.colors.backgroundSecondary.green * 0.89f
    )

@Composable
fun CountdownConfigurationScreenVM(
    viewModel: CountdownConfigurationViewModel = hiltViewModel(),
    backClicked: () -> Unit
) {
    val uiState = viewModel.uiState.collectAsState()
    CountdownConfigurationScreen(
        uiState = uiState.value,
        save = viewModel::save,
        select = viewModel::select,
        openAppOnClick = viewModel::openAppOnClick,
        backClicked = backClicked
    )
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun CountdownConfigurationScreen(
    uiState: UiState,
    save: () -> Unit,
    select: (Countdown) -> Unit,
    openAppOnClick: (Boolean) -> Unit,
    backClicked: () -> Unit,
) {
    AppTheme {
        Scaffold(content = {
            Column(Modifier
                .background(AppTheme.colors.backgroundContainer)
                .fillMaxSize()
                .padding(it)
            ) {
                TitleBar(
                    title = stringResource(id = string.widget_title),
                    showBack = true,
                    actionUpClicked = backClicked
                )
                if (uiState.items.isEmpty()) {
                    Box(Modifier.weight(1f)) {
                        Placeholder(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                } else {
                    Column(Modifier.weight(1f)) {
                        LazyColumn(
                            modifier = Modifier.weight(1f),
                            content = {
                                items(uiState.items, key = { it.id }) {
                                    SelectableItem(
                                        countdown = it,
                                        selectItem = select,
                                        isChecked = uiState.selected == it
                                    )
                                }
                            }
                        )
                    }
//                    Switch(
//                        modifier = Modifier
//                            .clickable {
//                                openAppOnClick(!uiState.openAppOnClick)
//                            }
//                            .padding(
//                                horizontal = AppTheme.dimensions.paddingMedium,
//                                vertical = AppTheme.dimensions.paddingSmall
//                            ),
//                        isChecked = uiState.openAppOnClick,
//                        label = stringResource(string.widget_open_app_on_click)
//                    )
                }
                PrimaryButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = AppTheme.dimensions.paddingMedium,
                            start = AppTheme.dimensions.paddingMedium,
                            end = AppTheme.dimensions.paddingMedium,
                            bottom = AppTheme.dimensions.paddingMedium,
                        ),
                    isEnabled = uiState.selected != null && uiState.appWidgetId != -1,
                    text = stringResource(id = string.modify_header_save),
                    onClick = {
                        save()
                        backClicked()
                    }
                )
            }
        })
    }
}




@Composable
private fun Placeholder(
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Icon(
            imageVector = ImageVector.vectorResource(
                id = R.drawable.ic_no_items,
            ),
            tint = AppTheme.colors.textSecondary,
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextBody1(text = stringResource(id = string.placeholder_title))
    }
}

@Composable
private fun SelectableItem(
    countdown: Countdown,
    selectItem: (Countdown) -> Unit,
    modifier: Modifier = Modifier,
    now: LocalDateTime = LocalDateTime.now(),
    isChecked: Boolean = false,
) {
    Column(
        modifier = modifier
            .padding(
                horizontal = AppTheme.dimensions.paddingSmall,
                vertical = AppTheme.dimensions.paddingSmall
            )
            .clip(RoundedCornerShape(AppTheme.dimensions.radiusMedium))
            .background(when (isChecked) {
                true -> AppTheme.colors.backgroundSecondary
                false -> Color.Transparent
            })
            .fillMaxWidth()
            .clickable(onClick = { selectItem(countdown) })
            .padding(
                start = AppTheme.dimensions.paddingSmall,
                top = AppTheme.dimensions.paddingNSmall,
                bottom = AppTheme.dimensions.paddingNSmall,
                end = AppTheme.dimensions.paddingSmall
            )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                TextBody1(
                    text = countdown.name,
                    bold = true
                )
                Spacer(modifier = Modifier.height(2.dp))
                TextBody2(
                    text = countdown.description,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(2.dp))
            }
            if (isChecked) {
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = stringResource(string.ab_selected),
                    tint = AppTheme.colors.textPrimary
                )
            }
        }
        ProgressBar(
            barColor = Color(countdown.colour.toColorInt()),
            backgroundColor = barBackground,
            endProgress = ProgressUtils.getProgress(countdown, now),
            label = { progress ->
                countdown.getProgress(progress = progress)
            }
        )
    }
}

@PreviewTheme
@Composable
private fun Preview() {
    AppThemePreview {
        CountdownConfigurationScreen(
            uiState = uiState,
            save = { },
            backClicked = { },
            select = { },
            openAppOnClick = { }
        )
    }
}

private val uiState: UiState = UiState(
    items = listOf(generateCountdown(id = "1"),generateCountdown(id = "2"),generateCountdown(id = "3")),
    selected = generateCountdown(id = "1"),
    openAppOnClick = false,
    appWidgetId = 1
)
private fun generateCountdown(id: String) = Countdown(
    id = id,
    name = "Countdown $id",
    description = "Description",
    colour = "#C2B2A3",
    start = LocalDateTime.of(2024, 1, 1, 1, 1),
    end = LocalDateTime.of(2026, 1, 1, 1, 1),
    startValue = "0",
    endValue = "100",
    countdownType = CountdownType.METRES,
    interpolator = CountdownInterpolator.LINEAR,
    notifications = emptyList()
)