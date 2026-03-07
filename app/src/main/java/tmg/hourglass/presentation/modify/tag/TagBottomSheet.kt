@file:OptIn(ExperimentalMaterial3Api::class)

package tmg.hourglass.presentation.modify.tag

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import kotlinx.coroutines.launch
import tmg.hourglass.R
import tmg.hourglass.domain.model.Tag
import tmg.hourglass.domain.model.TagOrdering
import tmg.hourglass.presentation.AppTheme
import tmg.hourglass.presentation.PreviewTheme
import tmg.hourglass.presentation.buttons.PrimaryButton
import tmg.hourglass.presentation.inputs.Input
import tmg.hourglass.presentation.textviews.TextBody1
import tmg.hourglass.presentation.textviews.TextHeader1
import tmg.hourglass.presentation.textviews.TextHeader2
import tmg.hourglass.strings.R.string

@Composable
internal fun TagBottomSheet(
    viewModel: TagViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val uiState = viewModel.uiState.collectAsState()
    val sheetState = rememberModalBottomSheetState { true }

    LaunchedEffect(Unit) {
        coroutineScope.launch { sheetState.expand() }
    }

    TagBottomSheet(
        uiState = uiState.value,
        insertTag = viewModel::insertTag,
        deleteTag = viewModel::deleteTag,
        inputTag = viewModel::inputTag,
        sheetState = sheetState
    )
}

@Composable
private fun TagBottomSheet(
    uiState: TagUiState,
    insertTag: (String) -> Unit,
    deleteTag: (Tag) -> Unit,
    inputTag: (String) -> Unit,
    sheetState: SheetState,
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()

    ModalBottomSheet(
        modifier = modifier,
        sheetState = sheetState,
        onDismissRequest = {
            coroutineScope.launch { sheetState.hide() }
        },
        content = {
            TagContent(
                uiState = uiState,
                close = {
                    coroutineScope.launch { sheetState.hide() }
                },
                insertTag = insertTag,
                deleteTag = deleteTag,
                inputTag = inputTag,
            )
        }
    )
}

@Composable
private fun TagContent(
    uiState: TagUiState,
    close: () -> Unit,
    insertTag: (String) -> Unit,
    deleteTag: (Tag) -> Unit,
    inputTag: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .padding(
                horizontal = AppTheme.dimensions.paddingMedium,
                vertical = AppTheme.dimensions.paddingMedium
            ),
        verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.paddingXSmall)
    ) {
        item(key = "header") {
            Row {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    TextHeader1(stringResource(string.tag_title))
                    TextBody1(stringResource(string.tag_subtitle))
                }
                IconButton(
                    onClick = close
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_close),
                        contentDescription = null,
                        tint = AppTheme.colors.textSecondary
                    )
                }
            }
        }
        items(uiState.tags, key = { it.tagId }) {
            TagView(
                tag = it,
                deleteTag = deleteTag,
            )
        }
        item("add") {
            Column(Modifier.fillMaxWidth()) {
                TextHeader2(stringResource(string.tag_save))
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
                horizontal = AppTheme.dimensions.paddingMedium,
                vertical = AppTheme.dimensions.paddingSmall
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextBody1(
            modifier = Modifier.weight(1f),
            text = tag.name
        )
        IconButton(
            onClick = { }
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
            uiState = uiState,
            close = { },
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