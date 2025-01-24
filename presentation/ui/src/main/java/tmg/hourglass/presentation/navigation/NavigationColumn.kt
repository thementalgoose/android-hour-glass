package tmg.hourglass.presentation.navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import tmg.hourglass.presentation.AppTheme
import tmg.hourglass.presentation.AppThemePreview
import tmg.hourglass.presentation.PreviewTheme
import tmg.hourglass.presentation.R
import tmg.hourglass.presentation.textviews.TextBody1
import tmg.hourglass.strings.R.string

val columnWidthCollapsed: Dp = 64.dp
private val itemSize: Dp = 48.dp
private val iconSize: Dp = 24.dp
val columnWidthExpanded: Dp = 240.dp

@Composable
fun NavigationColumn(
    list: List<NavigationItem>,
    itemClicked: (NavigationItem) -> Unit,
    modifier: Modifier = Modifier,
    lockExpanded: Boolean = false,
    background: Color = AppTheme.colors.backgroundNav,
    contentHeader: @Composable ColumnScope.() -> Unit = {}
) {
    val expanded = remember { mutableStateOf(lockExpanded) }
    val width = animateDpAsState(targetValue = when (expanded.value) {
        true -> columnWidthExpanded
        false -> columnWidthCollapsed
    }, label = "width")

    Column(modifier = modifier
        .width(width.value)
        .fillMaxHeight()
        .background(background)
        .padding(
            vertical = AppTheme.dimensions.paddingSmall
        )
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            content = {
                item {
                    Column(Modifier.fillMaxWidth()) {
                        Spacer(modifier = Modifier.height(AppTheme.dimensions.paddingSmall))
                    }
                }
                items(list) {
                    NavigationItem(
                        item = it,
                        isExpanded = expanded.value,
                        onClick = itemClicked,
                    )
                    Spacer(Modifier.height(AppTheme.dimensions.paddingSmall))
                }
                item {
                    Spacer(modifier = Modifier.height(AppTheme.dimensions.paddingSmall))
                }
            }
        )
        if (!lockExpanded) {
            NavigationItem(
                item = NavigationItem(
                    id = "menu",
                    label = string.empty,
                    icon = R.drawable.ic_menu
                ),
                onClick = {
                    expanded.value = !expanded.value
                },
                isExpanded = expanded.value
            )
        }
    }
}

@Composable
private fun NavigationItem(
    item: NavigationItem,
    isExpanded: Boolean,
    onClick: ((NavigationItem) -> Unit)?,
    modifier: Modifier = Modifier,
) {
    val backgroundColor = animateColorAsState(targetValue = when (item.isSelected) {
        true -> AppTheme.colors.primary.copy(alpha = 0.2f)
        else -> AppTheme.colors.backgroundPrimary
    }, label = "backgroundColor")
    val iconPadding = animateDpAsState(targetValue = when (isExpanded) {
        true -> AppTheme.dimensions.paddingMedium
        false -> (itemSize - iconSize) / 2
    }, label = "iconPadding")

    Row(modifier = modifier
        .padding(
            horizontal = (columnWidthCollapsed - itemSize) / 2
        )
        .fillMaxWidth()
        .height(itemSize)
        .clip(RoundedCornerShape(AppTheme.dimensions.radiusMedium))
        .background(backgroundColor.value)
        .clickable(
            enabled = onClick != null,
            onClick = {
                onClick?.invoke(item)
            }
        )
        .padding(
            horizontal = iconPadding.value,
        )
    ) {
        Icon(
            modifier = Modifier
                .size(iconSize)
                .align(Alignment.CenterVertically),
            painter = painterResource(id = item.icon),
            tint = AppTheme.colors.textPrimary,
            contentDescription = stringResource(id = item.label)
        )
        if (isExpanded) {
            TextBody1(
                modifier = Modifier
                    .padding(start = AppTheme.dimensions.paddingSmall)
                    .align(Alignment.CenterVertically)
                    .fillMaxWidth(),
                maxLines = 1,
                text = item.label.let { stringResource(id = it) }
            )
        }
    }
}

@PreviewTheme
@Composable
private fun PreviewCompact() {
    AppThemePreview {
        NavigationColumn(
            lockExpanded = false,
            itemClicked = { },
            list = fakeNavigationItems
        )
    }
}

@PreviewTheme
@Composable
private fun PreviewExpanded() {
    AppThemePreview {
        NavigationColumn(
            lockExpanded = true,
            itemClicked = { },
            list = fakeNavigationItems
        )
    }
}