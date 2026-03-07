package tmg.hourglass.presentation.home.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import tmg.hourglass.domain.model.TagOrdering
import tmg.hourglass.presentation.dialog.TextDialog
import tmg.hourglass.strings.R.string

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortDialog(
    itemSelected: TagOrdering,
    sortUpdated: (TagOrdering) -> Unit,
    dismissed: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextDialog(
        modifier = modifier,
        items = TagOrdering.entries,
        itemSelected = itemSelected,
        itemClicked = sortUpdated,
        dismissed = dismissed,
        title = stringResource(id = string.menu_sort),
        itemLabel = {
            stringResource(it.label())
        }
    )
}

@Composable
fun TagOrdering.label(): Int {
    return when (this) {
        TagOrdering.ALPHABETICAL -> string.menu_sort_alphabetical
        TagOrdering.FINISHING_SOONEST -> string.menu_finishing_soonest
        TagOrdering.FINISHING_LATEST -> string.menu_finishing_latest
        TagOrdering.PROGRESS -> string.menu_progress
    }
}