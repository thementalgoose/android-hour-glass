package tmg.hourglass.presentation.modify.tag

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import tmg.hourglass.domain.model.Tag
import tmg.hourglass.presentation.dialog.TextDialog
import tmg.hourglass.strings.R.string

@Composable
fun TagDialog(
    dismissed: () -> Unit,
    selectedTag: Tag?,
    selectTag: (Tag) -> Unit,
    tags: List<Tag>
) {
    TextDialog(
        items = tags,
        itemClicked = selectTag,
        dismissed = dismissed,
        title = stringResource(string.tag_title),
        itemSelected = selectedTag,
        showSelection = true,
        itemLabel = { it.name },
    )
}