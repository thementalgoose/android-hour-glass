@file:OptIn(ExperimentalMaterial3Api::class)

package tmg.hourglass.presentation.tag

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import kotlinx.coroutines.launch
import tmg.hourglass.R
import tmg.hourglass.domain.model.Tag
import tmg.hourglass.domain.model.TagOrdering
import tmg.hourglass.presentation.AppTheme
import tmg.hourglass.presentation.PreviewTheme
import tmg.hourglass.presentation.buttons.PrimaryButton
import tmg.hourglass.presentation.inputs.Input
import tmg.hourglass.presentation.layouts.TitleBar
import tmg.hourglass.presentation.textviews.TextBody1
import tmg.hourglass.presentation.textviews.TextHeader2
import tmg.hourglass.strings.R.string

@Composable
internal fun TagScreenVM(
    paddingValues: PaddingValues,
    actionUpClicked: () -> Unit,
    windowSizeClass: WindowSizeClass,
    viewModel: TagViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val uiState = viewModel.uiState.collectAsState()
    val sheetState = rememberModalBottomSheetState { true }

    LaunchedEffect(Unit) {
        coroutineScope.launch { sheetState.expand() }
    }

    TagContent(
        paddingValues = paddingValues,
        uiState = uiState.value,
        showActionUp = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact,
        actionUpClicked = actionUpClicked,
        insertTag = viewModel::insertTag,
        deleteTag = viewModel::deleteTag,
        inputTag = viewModel::inputTag,
    )
}

@Composable
private fun TagContent(
    paddingValues: PaddingValues,
    uiState: TagUiState,
    showActionUp: Boolean,
    actionUpClicked: () -> Unit,
    insertTag: (String) -> Unit,
    deleteTag: (Tag) -> Unit,
    inputTag: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .padding(
                vertical = AppTheme.dimensions.paddingMedium
            ),
        verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.paddingSmall)
    ) {
        item(key = "header") {
            TitleBar(
                modifier = Modifier.animateItem(),
                titleModifier = Modifier.padding(start = AppTheme.dimensions.paddingMedium),
                title = stringResource(string.tag_title),
                actionUpClicked = actionUpClicked,
                showBack = showActionUp
            )
        }
        item("add") {
            Column(
                modifier = Modifier
                    .animateItem()
                    .fillMaxWidth()
                    .padding(horizontal = AppTheme.dimensions.paddingMedium),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                TextHeader2(
                    text = stringResource(string.tag_save)
                )
                Input(
                    inputUpdated = inputTag,
                    hint = stringResource(string.tag_hint),
                    maxLines = 1,
                )
                PrimaryButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(string.tag_save),
                    isEnabled = uiState.tagInput.isNotBlank(),
                    onClick = {
                        insertTag(uiState.tagInput)
                    },
                )
            }
        }
        items(uiState.tags, key = { it.tagId }) {
            TagView(
                modifier = Modifier
                    .animateItem()
                    .padding(horizontal = AppTheme.dimensions.paddingMedium),
                tag = it,
                deleteTag = deleteTag,
            )
        }
    }
}

@Composable
private fun TagView(
    tag: Tag,
    deleteTag: (Tag) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(AppTheme.dimensions.radiusSmall))
            .background(AppTheme.colors.backgroundSecondary)
            .padding(
                start = AppTheme.dimensions.paddingMedium,
                top = AppTheme.dimensions.paddingSmall,
                bottom = AppTheme.dimensions.paddingSmall
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextBody1(
            modifier = Modifier.weight(1f),
            text = tag.name
        )
        IconButton(
            onClick = { deleteTag(tag) }
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_delete),
                contentDescription = null,
                tint = AppTheme.colors.textSecondary
            )
        }
    }
}

@PreviewTheme
@Composable
private fun Preview() {
    AppTheme {
        val uiState = TagUiState(
            tags = listOf(
                tagPreview("1"),
                tagPreview("2"),
                tagPreview("3")
            ),
            tagInput = ""
        )
        TagContent(
            paddingValues = PaddingValues(),
            uiState = uiState,
            showActionUp = true,
            actionUpClicked = { },
            insertTag = { },
            deleteTag = { },
            inputTag = { },
        )
    }
}

private fun tagPreview(id: String = "1") = Tag(
    tagId = id,
    name = "Tag $id",
    colour = "#888888",
    sort = TagOrdering.FINISHING_SOONEST
)