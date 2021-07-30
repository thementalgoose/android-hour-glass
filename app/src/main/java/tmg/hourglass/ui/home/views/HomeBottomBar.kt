package tmg.hourglass.ui.home.views

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RunCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import tmg.hourglass.R

enum class Tab {
    NOW,
    PREVIOUS,
    SETTINGS
}

@Composable
fun HomeBottomBar(
    selection: Tab,
    tabClicked: (Tab) -> Unit
) {
    BottomAppBar(
        content = {
            BottomNavigation {
                BottomNavigationItem(
                    icon = {
                        Icon(Icons.Filled.RunCircle, stringResource(R.string.menu_now))
                    },
                    label = { Text(stringResource(R.string.menu_now)) },
                    onClick = { tabClicked(Tab.NOW) },
                    selected = selection == Tab.NOW
                )
                BottomNavigationItem(
                    icon = {
                        Icon(Icons.Filled.SkipPrevious, stringResource(R.string.menu_previous))
                    },
                    label = { Text(stringResource(R.string.menu_previous)) },
                    onClick = { tabClicked(Tab.PREVIOUS) },
                    selected = selection == Tab.PREVIOUS
                )
                BottomNavigationItem(
                    icon = {
                        Icon(Icons.Filled.Settings, stringResource(R.string.menu_settings))
                    },
                    label = { Text(stringResource(R.string.menu_settings)) },
                    onClick = { tabClicked(Tab.SETTINGS) },
                    selected = selection == Tab.SETTINGS
                )
            }
        }
    )
}

@Composable
@Preview
private fun Preview() {
    HomeBottomBar(selection = Tab.NOW, tabClicked = { })
}
