package tmg.hourglass.widgets.presentation.configure

import android.annotation.SuppressLint
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
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import androidx.hilt.navigation.compose.hiltViewModel
import org.threeten.bp.LocalDateTime
import tmg.hourglass.domain.model.Countdown
import tmg.hourglass.domain.utils.ProgressUtils
import tmg.hourglass.presentation.AppTheme
import tmg.hourglass.presentation.buttons.PrimaryButton
import tmg.hourglass.presentation.layouts.TitleBar
import tmg.hourglass.presentation.textviews.TextBody1
import tmg.hourglass.presentation.textviews.TextBody2
import tmg.hourglass.presentation.views.ProgressBar
import tmg.hourglass.widgets.R
import tmg.hourglass.strings.R.string

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
        backClicked = backClicked
    )
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun CountdownConfigurationScreen(
    uiState: UiState,
    save: () -> Unit,
    select: (Countdown) -> Unit,
    backClicked: () -> Unit,
) {
    AppTheme {
        Scaffold(content = {
            Column(Modifier.fillMaxSize()) {
                TitleBar(
                    title = stringResource(id = string.widget_title),
                    backClicked = backClicked
                )
                if (uiState.items.isEmpty()) {
                    Box(Modifier.weight(1f)) {
                        Placeholder(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                } else {
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
                PrimaryButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = AppTheme.dimensions.paddingMedium,
                            start = AppTheme.dimensions.paddingMedium,
                            end = AppTheme.dimensions.paddingMedium,
                            bottom = AppTheme.dimensions.paddingMedium,
                        ),
                    isEnabled = uiState.selected != null,
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
            .fillMaxWidth()
            .clickable(onClick = { selectItem(countdown) })
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
                Spacer(modifier = Modifier.height(2.dp))
                TextBody2(
                    text = countdown.description,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(2.dp))
            }
            val icon = when (isChecked) {
                true -> Icons.Filled.CheckCircle
                false -> Icons.Outlined.Check
            }
            Icon(icon, contentDescription = stringResource(id = string.ab_select))
        }
        ProgressBar(
            barColor = Color(countdown.colour.toColorInt()),
            backgroundColor = AppTheme.colors.backgroundSecondary,
            endProgress = ProgressUtils.getProgress(countdown, now),
            label = { progress ->
                countdown.getProgress(progress = progress)
            }
        )
    }
}