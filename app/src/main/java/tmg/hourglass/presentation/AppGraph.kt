@file:OptIn(ExperimentalMaterial3AdaptiveApi::class)

package tmg.hourglass.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import androidx.window.layout.WindowLayoutInfo
import tmg.hourglass.presentation.home.HomeScreenVM
import tmg.hourglass.presentation.modify.ModifyScreenVM
import tmg.hourglass.presentation.navigation.NavigationController
import tmg.hourglass.presentation.navigation.NavigationDestination
import tmg.hourglass.presentation.settings.SettingsScreenVM
import tmg.hourglass.presentation.settings.privacy.PrivacyPolicyLayout

@Composable
fun AppGraph(
    navController: NavigationController,
    windowSize: WindowSizeClass,
    paddingValues: PaddingValues,
    windowInfo: WindowLayoutInfo,
    deeplink: String?,
    goToMarketPage: () -> Unit,
    goToAboutThisApp: () -> Unit,
) {
    val listDetailStrategy = rememberListDetailSceneStrategy<Any>()
    NavDisplay(
        backStack = navController.navStack,
        onBack = { keysToRemove -> repeat(keysToRemove) { navController.popBack() } },
        sceneStrategy = listDetailStrategy,
        entryProvider = entryProvider {
            entry<NavigationDestination.Home>(
                metadata = ListDetailSceneStrategy.listPane()
            ) {
                HomeScreenVM(
                    paddingValues = paddingValues,
                    windowSize = windowSize,
                )
            }

            entry<NavigationDestination.AddCountdown>(
                metadata = ListDetailSceneStrategy.detailPane()
            ) {
                ModifyScreenVM(
                    windowSizeClass = windowSize,
                    actionUpClicked = { navController.popBack() },
                    id = null
                )
            }

            entry<NavigationDestination.ModifyCountdown>(
                metadata = ListDetailSceneStrategy.detailPane()
            ) { modify ->
                ModifyScreenVM(
                    windowSizeClass = windowSize,
                    actionUpClicked = { navController.popBack() },
                    id = modify.id
                )
            }

            entry<NavigationDestination.Settings>(
                metadata = ListDetailSceneStrategy.listPane()
            ) { modify ->
                SettingsScreenVM(
                    paddingValues = paddingValues,
                    windowSize = windowSize,
                    goToMarketPage = goToMarketPage,
                    goToAboutThisApp = goToAboutThisApp,
                    actionUpClicked = navController::popBack
                )
            }

            entry<NavigationDestination.PrivacyPolicy>(
                metadata = ListDetailSceneStrategy.detailPane()
            ) {
                PrivacyPolicyLayout(
                    actionUpClicked = navController::popBack
                )
            }
        }
    )
}