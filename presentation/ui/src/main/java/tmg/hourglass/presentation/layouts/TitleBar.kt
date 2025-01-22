package tmg.hourglass.presentation.layouts

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import tmg.hourglass.presentation.AppTheme
import tmg.hourglass.presentation.AppThemePreview
import tmg.hourglass.presentation.PreviewTheme
import tmg.hourglass.presentation.R
import tmg.hourglass.presentation.textviews.TextHeader1
import tmg.hourglass.strings.R.string

@Composable
fun TitleBar(
    title: String,
    modifier: Modifier = Modifier,
    overflowActions: @Composable RowScope.() -> Unit = { },
) {
    TitleBar(
        title = title,
        modifier = modifier,
        overflowActions = overflowActions,
        showBack = false,
        actionUpClicked = { }
    )
}

@Composable
fun TitleBar(
    title: String,
    modifier: Modifier = Modifier,
    showBack: Boolean,
    actionUpClicked: () -> Unit,
    @DrawableRes
    backIcon: Int = R.drawable.ic_back,
    overflowActions: @Composable RowScope.() -> Unit = { },
) {
    Column(modifier = modifier
        .fillMaxWidth()
        .padding(top = AppTheme.dimensions.paddingSmall)
    ) {
        if (showBack) {
            Row(modifier = Modifier.fillMaxWidth()) {
                IconButton(
                    onClick = actionUpClicked
                ) {
                    Icon(
                        painter = painterResource(id = backIcon),
                        tint = AppTheme.colors.textPrimary,
                        contentDescription = stringResource(id = string.ab_back)
                    )
                }
                Spacer(Modifier.weight(1f))
                overflowActions()
            }
        }
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = AppTheme.dimensions.paddingSmall,
                bottom = AppTheme.dimensions.paddingMedium
            )
        ) {
            TextHeader1(
                text = title,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = AppTheme.dimensions.paddingMedium)
                    .align(Alignment.CenterVertically),
            )
            if (!showBack) {
                overflowActions()
            }
        }
    }
}

@PreviewTheme
@Composable
private fun Preview() {
    AppThemePreview {
        TitleBar(
            title = "Settings"
        )
    }
}

@PreviewTheme
@Composable
private fun PreviewWithIcons() {
    AppThemePreview {
        TitleBar(
            title = "Settings",
            overflowActions = {
                FakeIconButton()
            }
        )
    }
}


@PreviewTheme
@Composable
private fun PreviewBack() {
    AppThemePreview {
        TitleBar(
            title = "Settings",
            showBack = true,
            actionUpClicked = { }
        )
    }
}

@PreviewTheme
@Composable
private fun PreviewBackWithIcons() {
    AppThemePreview {
        TitleBar(
            title = "Settings",
            showBack = true,
            actionUpClicked = { },
            overflowActions = {
                FakeIconButton()
            }
        )
    }
}

@Composable
private fun FakeIconButton() {
    IconButton(
        onClick = {},
        content = {
            Icon(
                imageVector = Icons.Outlined.Settings,
                contentDescription = null,
                tint = AppTheme.colors.textPrimary
            )
        }
    )
}