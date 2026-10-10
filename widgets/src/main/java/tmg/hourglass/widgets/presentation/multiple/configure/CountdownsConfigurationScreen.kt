package tmg.hourglass.widgets.presentation.multiple.configure

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import tmg.hourglass.domain.model.Tag
import tmg.hourglass.domain.model.TagOrdering
import tmg.hourglass.presentation.AppTheme
import tmg.hourglass.presentation.AppThemePreview
import tmg.hourglass.presentation.PreviewTheme
import tmg.hourglass.presentation.buttons.PrimaryButton
import tmg.hourglass.presentation.layouts.TitleBar
import tmg.hourglass.presentation.textviews.TextBody1
import tmg.hourglass.presentation.textviews.TextBody2
import tmg.hourglass.strings.R.string

@Composable
fun CountdownsConfigurationScreenVM(
    viewModel: CountdownsConfigurationViewModel = hiltViewModel(),
    backClicked: () -> Unit
) {
    val uiState = viewModel.uiState.collectAsState()
    CountdownsConfigurationScreen(
        uiState = uiState.value,
        save = viewModel::save,
        selectTag = viewModel::selectTag,
        openAppOnClick = viewModel::openAppOnClick,
        backClicked = backClicked
    )
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun CountdownsConfigurationScreen(
    uiState: CountdownsUiState,
    save: () -> Unit,
    selectTag: (String?) -> Unit,
    openAppOnClick: (Boolean) -> Unit,
    backClicked: () -> Unit,
) {
    AppTheme {
        Scaffold(content = {
            Column(
                Modifier
                    .background(AppTheme.colors.backgroundContainer)
                    .fillMaxSize()
                    .padding(it)
            ) {
                TitleBar(
                    title = stringResource(id = string.widget_title),
                    modifier = Modifier.padding(bottom = AppTheme.dimensions.paddingMedium),
                    showBack = true,
                    titleModifier = Modifier.padding(start = AppTheme.dimensions.paddingMedium),
                    actionUpClicked = backClicked
                )
                Column(Modifier.weight(1f)) {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.paddingXSmall),
                        content = {
                            item(key = "all_tags") {
                                SelectableTagItem(
                                    name = stringResource(id = string.title_all),
                                    description = null,
                                    colour = null,
                                    isChecked = uiState.selectedTagId == null,
                                    onClick = { selectTag(null) }
                                )
                            }
                            items(uiState.tags, key = { it.tagId }) { tag ->
                                SelectableTagItem(
                                    name = tag.name,
                                    description = null,
                                    colour = tag.colour,
                                    isChecked = uiState.selectedTagId == tag.tagId,
                                    onClick = { selectTag(tag.tagId) }
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
                    isEnabled = uiState.appWidgetId != -1,
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
private fun SelectableTagItem(
    name: String,
    description: String?,
    colour: String?,
    isChecked: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val selectionModifier = when (isChecked) {
        true -> Modifier
            .background(AppTheme.colors.backgroundTertiary)
            .border(1.dp, AppTheme.colors.primary, RoundedCornerShape(AppTheme.dimensions.radiusSmall))
        false -> Modifier
            .background(AppTheme.colors.backgroundSecondary)
    }

    Column(
        modifier = modifier
            .padding(horizontal = AppTheme.dimensions.paddingMedium)
            .clip(RoundedCornerShape(AppTheme.dimensions.radiusSmall))
            .then(selectionModifier)
            .clickable(onClick = onClick)
            .padding(
                horizontal = AppTheme.dimensions.paddingMedium,
                vertical = AppTheme.dimensions.paddingMedium
            )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (colour != null) {
                Box(
                    modifier = Modifier
                        .padding(end = AppTheme.dimensions.paddingMedium)
                        .size(16.dp)
                        .clip(CircleShape)
                        .background(Color(colour.toColorInt()))
                )
            }
            Column(
                modifier = Modifier.weight(1f)
            ) {
                TextBody1(
                    text = name,
                    bold = true
                )
                if (!description.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    TextBody2(
                        text = description,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            if (isChecked) {
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = stringResource(string.ab_selected),
                    tint = AppTheme.colors.textPrimary
                )
            }
        }
    }
}

@PreviewTheme
@Composable
private fun PreviewCountdownsConfigurationScreen() {
    AppThemePreview {
        CountdownsConfigurationScreen(
            uiState = CountdownsUiState(
                tags = listOf(
                    Tag(
                        tagId = "1",
                        name = "Holidays",
                        colour = "#CA2323",
                        sort = TagOrdering.ALPHABETICAL,
                        expanded = true
                    ),
                    Tag(
                        tagId = "2",
                        name = "Work",
                        colour = "#2196F3",
                        sort = TagOrdering.FINISHING_SOONEST,
                        expanded = true
                    )
                ),
                selectedTagId = "1",
                openAppOnClick = false,
                appWidgetId = 1
            ),
            save = { },
            selectTag = { },
            openAppOnClick = { },
            backClicked = { }
        )
    }
}
