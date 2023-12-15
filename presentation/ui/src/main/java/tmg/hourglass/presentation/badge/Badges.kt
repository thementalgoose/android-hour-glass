package tmg.hourglass.presentation.badge

import tmg.hourglass.presentation.AppTheme
import tmg.hourglass.presentation.AppThemePreview
import tmg.hourglass.presentation.PreviewTheme
import tmg.hourglass.presentation.R
import tmg.hourglass.presentation.textviews.TextBody2
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Badge(
    val label: String,
    @DrawableRes
    val icon: Int? = null,
)

@Composable
fun BadgesView(
    list: List<Badge>,
    modifier: Modifier = Modifier,
    spacing: Dp = AppTheme.dimensions.paddingSmall
) {
    Row(
        modifier = modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(spacing)
    ) {
        list.forEach {
            BadgeView(model = it)
        }
    }
}

@Composable
fun BadgeView(
    model: Badge,
    modifier: Modifier = Modifier,
    tintIcon: Color? = AppTheme.colors.textPrimary,
) {
    Row(modifier = modifier
        .clip(RoundedCornerShape(AppTheme.dimensions.radiusSmall))
        .background(AppTheme.colors.backgroundSecondary)
        .border(
            1.dp,
            color = AppTheme.colors.backgroundSecondary.copy(alpha = 0.35f),
            shape = RoundedCornerShape(AppTheme.dimensions.radiusSmall)
        )
        .padding(
            horizontal = AppTheme.dimensions.paddingSmall,
            vertical = AppTheme.dimensions.paddingXSmall
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (model.icon != null) {
            if (tintIcon != null) {
                Icon(
                    painter = painterResource(id = model.icon),
                    contentDescription = null,
                    tint = tintIcon,
                    modifier = Modifier.size(16.dp)
                )
            } else {
                Image(
                    painter = painterResource(id = model.icon),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(Modifier.width(6.dp))
        }
        TextBody2(
            text = model.label,
            bold = true
        )
    }
}

@PreviewTheme
@Composable
private fun PreviewList() {
    AppThemePreview {
        BadgesView(list = listOf(fakeMenuBadge, fakeBackIconBadge))
    }
}

@PreviewTheme
@Composable
private fun Preview() {
    AppThemePreview {
        BadgeView(fakeBackBadge)
    }
}

private val fakeMenuBadge = Badge(
    label = "Play"
)
private val fakeMenuIconBadge = Badge(
    label = "Pause",
    icon = R.drawable.ic_menu
)
private val fakeBackBadge = Badge(
    label = "Play"
)
private val fakeBackIconBadge = Badge(
    label = "Pause",
    icon = R.drawable.ic_back
)