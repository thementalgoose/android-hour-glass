@file:OptIn(ExperimentalFoundationApi::class)

package tmg.hourglass.presentation.navigation

import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import tmg.hourglass.presentation.AppTheme
import tmg.hourglass.presentation.AppThemePreview
import tmg.hourglass.presentation.PreviewTheme
import tmg.hourglass.presentation.textviews.TextBody1

private val selectedPillWidth: Dp = 64.dp
private val pillHeight: Dp = 32.dp
private val iconSize: Dp = 24.dp
val appBarHeight: Dp = 80.dp

@Composable
fun NavigationBar(
    list: List<NavigationItem>,
    itemClicked: (NavigationItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .height(appBarHeight)
            .shadow(elevation = 8.dp)
            .background(AppTheme.colors.backgroundNav),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        list.forEach { item ->
            Item(
                item = item,
                itemClicked = itemClicked,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun Item(
    item: NavigationItem,
    itemClicked: (NavigationItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedWidth = animateDpAsState(targetValue = when (item.isSelected ?: false) {
        true -> selectedPillWidth
        false -> iconSize
    }, label = "selectedWidth")
    val selectedX = animateDpAsState(targetValue = when (item.isSelected ?: false) {
        true -> 0.dp
        false -> (selectedPillWidth - iconSize) / 2
    }, label = "selectedX")
    val backgroundColor = animateColorAsState(targetValue = when (item.isSelected ?: false) {
        true -> AppTheme.colors.primary.copy(alpha = 0.3f)
        false -> Color.Transparent
    }, label = "backgroundColor")

    val context = LocalContext.current
    Column(
        modifier = modifier
            .combinedClickable(
                onClick = { itemClicked(item) },
                onLongClick = {
                    Toast.makeText(context, context.getString(item.label), Toast.LENGTH_LONG).show()
                }
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = modifier
            .width(selectedPillWidth)
            .height(pillHeight)
        ) {
            Box(modifier = Modifier
                .width(selectedWidth.value)
                .height(pillHeight)
                .align(Alignment.CenterStart)
                .offset(x = selectedX.value)
                .clip(RoundedCornerShape(pillHeight / 2))
                .background(backgroundColor.value))
            Icon(
                modifier = Modifier
                    .size(iconSize)
                    .align(Alignment.Center),
                painter = painterResource(id = item.icon),
                tint = AppTheme.colors.textPrimary,
                contentDescription = null,
            )
        }
        Box(
            Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            TextBody1(
                textAlign = TextAlign.Center,
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        bottom = 10.dp,
                        start = 4.dp,
                        end = 4.dp
                    ),
                text = stringResource(id = item.label),
                textColor = AppTheme.colors.textPrimary,
                bold = item.isSelected == true
            )
            Box(
                Modifier
                    .fillMaxHeight()
                    .width(AppTheme.dimensions.paddingSmall)
                    .align(Alignment.CenterEnd)
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                Color.Transparent,
                                AppTheme.colors.backgroundNav
                            )
                        )
                    )
            )
        }
    }
}

@PreviewTheme
@Composable
private fun Preview() {
    AppThemePreview {
        NavigationBar(
            list = fakeNavigationItems,
            itemClicked = {}
        )
    }
}