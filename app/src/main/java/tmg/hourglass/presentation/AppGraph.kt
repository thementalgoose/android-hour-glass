package tmg.hourglass.presentation

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.window.layout.WindowLayoutInfo
import tmg.hourglass.navigation.Screen
import tmg.hourglass.presentation.home.HomeScreenVM
import tmg.hourglass.presentation.navigation.navigateTo
import tmg.hourglass.presentation.settings.SettingsScreenVM

@Composable
fun AppGraph(
    navController: NavHostController,
    windowSize: WindowSizeClass,
    windowInfo: WindowLayoutInfo,
    deeplink: String?,
    goToMarketPage: () -> Unit,
    goToAboutThisApp: () -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = Modifier
    ) {
        composable(Screen.Home.route) {
            HomeScreenVM(
                windowSize = windowSize,
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreenVM(
                windowSize = windowSize,
                goToMarketPage = goToMarketPage,
                goToAboutThisApp = goToAboutThisApp
            )
        }
    }
    DisposableEffect(Unit) {
        when (deeplink) {
            Screen.Home.route -> navController.navigateTo(Screen.Home)
        }
        this.onDispose { }
    }
}