package tmg.hourglass.presentation.layouts

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Indication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
    showSpace: Boolean = true,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        if (showSpace) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
            ) {
                Spacer(modifier = Modifier.width(AppTheme.dimensions.paddingXSmall))
            }
        }
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = AppTheme.dimensions.paddingMedium,
                end = AppTheme.dimensions.paddingMedium,
                bottom = AppTheme.dimensions.paddingMedium
            )
        ) {
            Spacer(modifier = Modifier.height(AppTheme.dimensions.paddingMedium))
            TextHeader1(text = title)
        }
    }
}

@Composable
fun TitleBar(
    title: String,
    modifier: Modifier = Modifier,
    backClicked: () -> Unit = { },
    @DrawableRes
    backIcon: Int = R.drawable.ic_back
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .height(54.dp)
        ) {
            Spacer(modifier = Modifier.width(AppTheme.dimensions.paddingXSmall))
            IconButton(
                onClick = backClicked
            ) {
                Icon(
                    painter = painterResource(id = backIcon),
                    tint = AppTheme.colors.textPrimary,
                    contentDescription = stringResource(id = string.ab_back)
                )
            }
        }
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = AppTheme.dimensions.paddingMedium,
                end = AppTheme.dimensions.paddingMedium,
                bottom = AppTheme.dimensions.paddingMedium
            )
        ) {
            Spacer(modifier = Modifier.height(AppTheme.dimensions.paddingMedium))
            TextHeader1(text = title)
        }
    }
}

@PreviewTheme
@Composable
private fun PreviewWithIcon() {
    AppThemePreview {
        TitleBar(
            title = "Settings",
            backClicked = { }
        )
    }
}

@PreviewTheme
@Composable
private fun PreviewNoIcon() {
    AppThemePreview {
        TitleBar(
            title = "Settings"
        )
    }
}


@PreviewTheme
@Composable
private fun PreviewNoSpaceIcon() {
    AppThemePreview {
        TitleBar(
            title = "Settings",
            showSpace = false
        )
    }
}