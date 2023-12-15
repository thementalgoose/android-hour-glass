package tmg.hourglass.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import tmg.hourglass.presentation.dashboard.DashboardScreen
import tmg.hourglass.presentation.navigation.NavigationBar
import tmg.hourglass.presentation.navigation.NavigationColumn
import tmg.hourglass.presentation.settings.SettingsScreen
import tmg.utilities.extensions.toEnum

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    windowSizeClass: WindowSizeClass,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsState()
    HomeScreen(
        windowSizeClass = windowSizeClass,
        uiState = uiState.value,
        tabClicked = viewModel::selectTab
    )
}

@Composable
internal fun HomeScreen(
    windowSizeClass: WindowSizeClass,
    uiState: UiState,
    tabClicked: (HomeTab) -> Unit,
) {
    val tabs = HomeTab.entries
        .map { it.toNavigationItem(it == uiState.tab) }
    Scaffold(
        content = {
            Row(Modifier.padding(it)) {
                if (windowSizeClass.widthSizeClass != WindowWidthSizeClass.Compact) {
                    NavigationColumn(
                        list = tabs,
                        itemClicked = {
                            it.id.toEnum<HomeTab>()?.let(tabClicked)
                        }
                    )
                }
                Box(Modifier.weight(1f)) {
                    when (uiState.tab) {
                        HomeTab.DASHBOARD -> {
                            DashboardScreen(
                                windowSizeClass = windowSizeClass,
                            )
                        }
                        HomeTab.SETTINGS -> {
                            SettingsScreen(
                                windowSizeClass = windowSizeClass,
                            )
                        }
                    }
                }
            }
        },
        bottomBar = {
            if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact) {
                NavigationBar(
                    list = tabs,
                    itemClicked = {
                        it.id.toEnum<HomeTab>()?.let(tabClicked)
                    }
                )
            }
        }
    )
}
