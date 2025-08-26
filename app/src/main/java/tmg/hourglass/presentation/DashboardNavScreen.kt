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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelStore
import androidx.navigation.compose.rememberNavController
import androidx.window.layout.WindowLayoutInfo
import tmg.hourglass.presentation.navigation.NavigationBar
import tmg.hourglass.presentation.navigation.NavigationColumn
import tmg.hourglass.presentation.navigation.NavigationController
import tmg.utilities.extensions.toEnum

@Composable
internal fun DashboardNavScreen(
    windowSize: WindowSizeClass,
    windowLayoutInfo: WindowLayoutInfo,
    navigator: NavigationController,
    viewModelStore: ViewModelStore,
    closeApp: () -> Unit,
    deeplink: String?,
    goToMarketPage: () -> Unit,
    goToAboutThisApp: () -> Unit,
    dashboardNavViewModel: DashboardNavViewModel = hiltViewModel(),
) {
    val navController = rememberNavController()
    DisposableEffect(key1 = navController, effect = {
        Log.i("Dashboard", "Configuring navController to viewmodel")
        navigator.navHostController = navController
        navController.setViewModelStore(viewModelStore)
        navController.addOnDestinationChangedListener(dashboardNavViewModel)
        return@DisposableEffect onDispose {  }
    })

    val uiState = dashboardNavViewModel.uiState.collectAsState()
    val tabs = listOf(Tab.DASHBOARD, Tab.SETTINGS)
        .map { it.toNavigationItem(isSelected = it == uiState.value.tab) }

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
                            it.id.toEnum<Tab>()?.let(dashboardNavViewModel::selectTab)
                        }
                    )
                }
                Column(modifier = Modifier.fillMaxSize()) {
                    Box(Modifier.weight(1f)) {
                        AppGraph(
                            paddingValues = it,
                            navController = navController,
                            windowSize = windowSize,
                            windowInfo = windowLayoutInfo,
                            deeplink = deeplink,
                            goToMarketPage = goToMarketPage,
                            goToAboutThisApp= goToAboutThisApp,
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
