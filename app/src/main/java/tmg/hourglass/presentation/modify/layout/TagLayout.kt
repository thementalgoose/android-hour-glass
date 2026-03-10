package tmg.hourglass.presentation.modify.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import tmg.hourglass.domain.model.Tag
import tmg.hourglass.domain.model.TagOrdering
import tmg.hourglass.presentation.AppTheme
import tmg.hourglass.presentation.AppThemePreview
import tmg.hourglass.presentation.PreviewTheme
import tmg.hourglass.presentation.buttons.PrimaryButton
import tmg.hourglass.presentation.modify.tag.TagLabel
import tmg.hourglass.presentation.textviews.TextBody1
import tmg.hourglass.presentation.textviews.TextHeader2
import tmg.hourglass.strings.R.string

@Composable
fun TagLayout(
    tags: List<Tag>,
    selected: Tag?,
    selectTag: (Tag) -> Unit,
    navigateToTag: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(AppTheme.dimensions.paddingMedium)
    ) {
        TextHeader2(text = stringResource(id = string.modify_field_tags))
        Spacer(modifier = Modifier.height(8.dp))
        TextBody1(text = stringResource(id = string.modify_field_tags_desc))
        Spacer(modifier = Modifier.height(8.dp))

        if (tags.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(AppTheme.dimensions.radiusSmall))
                    .background(AppTheme.colors.backgroundSecondary)
                    .clickable(
                        onClick = navigateToTag
                    )
                    .padding(AppTheme.dimensions.paddingMedium)
            ) {
                TextBody1(
                    text = stringResource(string.modify_field_tags_no_tags),
                )
            }
        } else {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                tags.forEach { tag ->
                    TagLabel(
                        tag = tag,
                        selected = tag == selected,
                        selectTag = selectTag,
                    )
                }
            }
        }
    }
}

@PreviewTheme
@Composable
private fun Preview() {
    AppThemePreview {
        TagLayout(
            tags = List(10) { tagPreview(it.toString())},
            selected = tagPreview("2"),
            selectTag = { },
            navigateToTag = { }
        )
    }
}

@PreviewTheme
@Composable
private fun PreviewEmpty() {
    AppThemePreview {
        TagLayout(
            tags = emptyList(),
            selectTag = { },
            selected = null,
            navigateToTag = { }
        )
    }
}


private fun tagPreview(id: String = "1") = Tag(
    tagId = id,
    name = "Tag $id",
    colour = "#888888",
    sort = TagOrdering.FINISHING_SOONEST,
    expanded = true,
)