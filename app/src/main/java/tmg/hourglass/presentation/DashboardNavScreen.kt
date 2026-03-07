package tmg.hourglass.presentation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.window.layout.WindowLayoutInfo
import tmg.hourglass.navigation.Home
import tmg.hourglass.navigation.Settings
import tmg.hourglass.navigation.Tags
import tmg.hourglass.navigation.navigateToHome
import tmg.hourglass.navigation.navigateToSettings
import tmg.hourglass.navigation.replaceDetail
import tmg.hourglass.presentation.navigation.NavigationColumn
import tmg.utilities.extensions.toEnum

@Composable
internal fun DashboardNavScreen(
    windowSize: WindowSizeClass,
    windowLayoutInfo: WindowLayoutInfo,
    goToMarketPage: () -> Unit,
    goToAboutThisApp: () -> Unit,
    dashboardNavViewModel: DashboardNavViewModel = hiltViewModel(),
) {
    val uiState = dashboardNavViewModel.uiState.collectAsState()
    val tabs = listOf(Tab.DASHBOARD, Tab.SETTINGS)
        .map { it.toNavigationItem(isSelected = it == uiState.value.tab) }

    val backStack = rememberNavBackStack(Home)
    DisposableEffect(backStack.lastOrNull()) {
        Log.i("HourGlass", "Reacting to back stack changes")
        when (backStack.lastOrNull()) {
            Home -> dashboardNavViewModel.selectTab(Tab.DASHBOARD)
            Settings -> dashboardNavViewModel.selectTab(Tab.SETTINGS)
        }
        return@DisposableEffect onDispose {  }
    }

    Scaffold(
        containerColor = AppTheme.colors.backgroundPrimary,
        content = {
            Row(Modifier) {
                if (windowSize.widthSizeClass != WindowWidthSizeClass.Compact) {
                    NavigationColumn(
                        modifier = Modifier
                            .statusBarsPadding()
                            .navigationBarsPadding(),
                        list = tabs,
                        background = AppTheme.colors.backgroundPrimary,
                        itemClicked = {
                            val tab = it.id.toEnum<Tab>() ?: return@NavigationColumn
                            when (tab) {
                                Tab.DASHBOARD -> backStack.navigateToHome()
                                Tab.SETTINGS -> backStack.navigateToSettings()
                            }
                        }
                    )
                }
                Column(modifier = Modifier.fillMaxSize()) {
                    Box(Modifier.weight(1f)) {
                        AppGraph(
                            paddingValues = it,
                            windowSize = windowSize,
                            windowInfo = windowLayoutInfo,
                            goToMarketPage = goToMarketPage,
                            goToAboutThisApp= goToAboutThisApp,
                            backStack = backStack
                        )

                        // Fake translucent status bar
                        Box(
                            modifier = Modifier
                                .height(WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
                                .fillMaxWidth()
                                .align(Alignment.TopCenter)
                                .background(Brush.verticalGradient(
                                    colors = listOf(AppTheme.colors.backgroundPrimary, Color.Transparent)
                                ))
                        )
                    }
                }
            }
        }
    )
}
