package tmg.hourglass.presentation.modify.tag

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import tmg.hourglass.presentation.AppTheme
import tmg.hourglass.presentation.PreviewTheme
import tmg.hourglass.presentation.textviews.TextBody2
import tmg.hourglass.domain.model.Tag
import tmg.hourglass.domain.model.TagOrdering

@Composable
fun TagLabel(
    tag: Tag,
    selected: Boolean,
    selectTag: (Tag) -> Unit,
    modifier: Modifier = Modifier,
) {
    val selectionModifier = when (selected) {
        true -> Modifier
            .background(AppTheme.colors.backgroundTertiary)
            .border(1.dp, AppTheme.colors.primary, RoundedCornerShape(AppTheme.dimensions.radiusSmall))
        false -> Modifier
            .background(AppTheme.colors.backgroundSecondary)
    }
    Row(modifier = modifier
        .clip(RoundedCornerShape(AppTheme.dimensions.radiusSmall))
        .then(selectionModifier)
        .clickable {
            selectTag(tag)
        }
        .padding(
            vertical = AppTheme.dimensions.paddingMedium,
            horizontal = AppTheme.dimensions.paddingMedium
        )
    ) {
        TextBody2(tag.name)
    }
}

@PreviewTheme
@Composable
private fun PreviewUnselected() {
    AppTheme {
        TagLabel(
            tag = Tag("id", "Name", "#888888", TagOrdering.ALPHABETICAL, false),
            selected = false,
            selectTag = { }
        )
    }
}

@PreviewTheme
@Composable
private fun PreviewSelected() {
    AppTheme {
        TagLabel(
            tag = Tag("id", "Name", "#888888", TagOrdering.ALPHABETICAL, false),
            selected = true,
            selectTag = { }
        )
    }
}