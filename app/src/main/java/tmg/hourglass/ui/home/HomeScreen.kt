package tmg.hourglass.ui.home

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.RunCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import tmg.hourglass.R
import tmg.hourglass.home.HomeViewModel
import tmg.hourglass.ui.home.views.HomeBottomBar
import tmg.hourglass.ui.home.views.Tab.NOW

@Composable
fun HomeScreen() {
    Scaffold(
        bottomBar = {
            HomeBottomBar(
                selection = NOW,
                tabClicked = { }
            )
        },
        content = {
            Text("Hey!")
        }
    )
}

@Composable
@Preview
fun HomeScreenPreview() {
    HomeScreen()
}