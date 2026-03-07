package tmg.hourglass.presentation.modify.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import tmg.hourglass.domain.model.Tag
import tmg.hourglass.domain.model.TagOrdering
import tmg.hourglass.presentation.AppTheme
import tmg.hourglass.presentation.AppThemePreview
import tmg.hourglass.presentation.PreviewTheme
import tmg.hourglass.presentation.modify.tag.TagLabel
import tmg.hourglass.presentation.textviews.TextBody1
import tmg.hourglass.presentation.textviews.TextHeader2
import tmg.hourglass.strings.R.string

@Composable
fun TagLayout(
    tags: List<Tag>,
    selected: Tag?,
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

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            tags.forEach { tag ->
                TagLabel(
                    tag = tag,
                    selected = tag == selected
                )
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
            selected = tagPreview("2")
        )
    }
}


private fun tagPreview(id: String = "1") = Tag(
    tagId = id,
    name = "Tag $id",
    colour = "#888888",
    sort = TagOrdering.FINISHING_SOONEST
)