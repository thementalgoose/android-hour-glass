package tmg.hourglass.widget

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import androidx.lifecycle.viewmodel.compose.viewModel
import org.threeten.bp.LocalDateTime
import tmg.hourglass.R
import tmg.hourglass.strings.R.string
import tmg.hourglass.domain.model.Countdown
import tmg.hourglass.presentation.AppTheme
import tmg.hourglass.presentation.buttons.PrimaryButton
import tmg.hourglass.presentation.layouts.TitleBar
import tmg.hourglass.presentation.textviews.TextBody1
import tmg.hourglass.presentation.textviews.TextBody2
import tmg.hourglass.presentation.views.ProgressBar
import tmg.hourglass.utils.ProgressUtils

@Composable
fun ItemWidgetPickerScreenVM(
    appWidgetId: Int,
    actionUpClicked: () -> Unit,
    saveClicked: (String) -> Unit,
) {
    val viewModel = viewModel<ItemWidgetPickerViewModel>()
    viewModel.inputs.supplyAppWidgetId(appWidgetId)
    val list = viewModel.outputs.list.observeAsState(emptyList())

    Column {
        LazyColumn(
            modifier = Modifier.weight(1f),
            content = {
                item {
                    TitleBar(
                        title = stringResource(id = R.string.app_name),
                        backClicked = actionUpClicked
                    )
                }
                items(list.value) {
                    when (it) {
                        is ItemWidgetPickerItem.Item -> {
                            SelectableItem(
                                countdown = it.countdown,
                                selectItem = {
                                    viewModel.inputs.checkedItem(it.id)
                                },
                                isChecked = it.isEnabled
                            )
                        }
                        ItemWidgetPickerItem.Placeholder -> Placeholder()
                    }
                }
            }
        )

        val saveState = viewModel.outputs.isSavedEnabled.observeAsState(false)
        PrimaryButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = AppTheme.dimensions.paddingMedium,
                    start = AppTheme.dimensions.paddingMedium,
                    end = AppTheme.dimensions.paddingMedium,
                    bottom = AppTheme.dimensions.paddingMedium,
                ),
            isEnabled = saveState.value,
            text = stringResource(id = string.modify_header_save),
            onClick = {

                val checkedId = list.value
                    .filterIsInstance<ItemWidgetPickerItem.Item>()
                    .firstOrNull { it.isEnabled }

                checkedId?.let {
                    viewModel.inputs.clickSave()
                    saveClicked(it.countdown.id)
                }
            }
        )
    }
}

@Composable
private fun Placeholder(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = AppTheme.dimensions.paddingMedium,
                end = AppTheme.dimensions.paddingMedium,
                top = AppTheme.dimensions.paddingMedium,
                bottom = AppTheme.dimensions.paddingMedium
            )
    ) {
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