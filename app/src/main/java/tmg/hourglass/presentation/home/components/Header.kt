package tmg.hourglass.presentation.home.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import tmg.hourglass.R
import tmg.hourglass.presentation.AppTheme
import tmg.hourglass.presentation.layouts.TitleBar
import tmg.hourglass.strings.R.string

@Composable
fun Header(
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier,
    navigateToSettings: () -> Unit
) {
    TitleBar(
        modifier = modifier,
        title = stringResource(id = R.string.app_name),
        overflowActions = {
            if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact) {
                IconButton(
                    onClick = navigateToSettings,
                    content = {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = stringResource(string.menu_settings),
                            tint = AppTheme.colors.textPrimary
                        )
                    }
                )
            }
        }
    )
}