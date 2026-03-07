package tmg.hourglass.presentation.modify.tag

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import tmg.hourglass.presentation.AppTheme
import tmg.hourglass.presentation.PreviewTheme
import tmg.hourglass.presentation.textviews.TextBody2
import tmg.hourglass.domain.model.Tag
import tmg.hourglass.domain.model.TagOrdering

@Composable
fun TagLabel(
    tag: Tag,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier
        .clip(CircleShape)
        .background(AppTheme.colors.backgroundTertiary)
        .padding(
            vertical = AppTheme.dimensions.paddingXSmall,
            horizontal = AppTheme.dimensions.paddingSmall
        )
    ) {
        TextBody2(tag.name)
    }
}

@PreviewTheme
@Composable
private fun Preview() {
    AppTheme {
        TagLabel(Tag("id", "Name", "#888888", TagOrdering.ALPHABETICAL))
    }
}