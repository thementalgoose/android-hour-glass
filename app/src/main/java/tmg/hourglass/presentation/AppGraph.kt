package tmg.hourglass.presentation

import android.util.Log
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import androidx.window.layout.WindowLayoutInfo
import tmg.hourglass.navigation.Add
import tmg.hourglass.navigation.Home
import tmg.hourglass.navigation.Modify
import tmg.hourglass.navigation.Settings
import tmg.hourglass.navigation.SettingsBackup
import tmg.hourglass.navigation.SettingsPrivacyPolicy
import tmg.hourglass.navigation.Tags
import tmg.hourglass.navigation.navigateToHome
import tmg.hourglass.navigation.navigateToSettings
import tmg.hourglass.navigation.removeDetail
import tmg.hourglass.navigation.replaceDetail
import tmg.hourglass.presentation.home.HomeScreenVM
import tmg.hourglass.presentation.modify.ModifyScreenVM
import tmg.hourglass.presentation.scene.SplitPaneScene.Companion.detailPane
import tmg.hourglass.presentation.scene.SplitPaneScene.Companion.listPane
import tmg.hourglass.presentation.scene.rememberSplitPaneSceneStrategy
import tmg.hourglass.presentation.settings.SettingsScreenVM
import tmg.hourglass.presentation.settings.backup.BackupScreen
import tmg.hourglass.presentation.settings.privacy.PrivacyPolicyLayout
import tmg.hourglass.presentation.tag.TagScreenVM

@Composable
fun AppGraph(
    windowSize: WindowSizeClass,
    paddingValues: PaddingValues,
    windowInfo: WindowLayoutInfo,
    goToMarketPage: () -> Unit,
    goToAboutThisApp: () -> Unit,
    backStack: NavBackStack<NavKey>,
) {
    val listDetailStrategy = rememberSplitPaneSceneStrategy<NavKey>(windowSize)
    NavDisplay(
        backStack = backStack,
        onBack = {
            backStack.removeLastOrNull()
        },
        sceneStrategy = listDetailStrategy,
        transitionSpec = {
            fadeIn() togetherWith fadeOut()
        },
        popTransitionSpec = {
            fadeIn() togetherWith fadeOut()
        },
        predictivePopTransitionSpec = {
            fadeIn() togetherWith fadeOut()
        },
        modifier = Modifier,
        entryProvider = entryProvider {
            entry<Home>(metadata = listPane()) {
                HomeScreenVM(
                    paddingValues = paddingValues,
                    windowSize = windowSize,
                    navigateToAdd = { backStack.replaceDetail(Add) },
                    navigateToModify = { backStack.replaceDetail(Modify(it)) },
                    navigateToSettings = { backStack.navigateToSettings() },
                    navigateToTags = {
                        backStack.replaceDetail(Tags)
                    }
                )
            }
            entry<Add>(metadata = detailPane()) {
                ModifyScreenVM(
                    paddingValues = paddingValues,
                    windowSizeClass = windowSize,
                    actionUpClicked = {
                        backStack.removeDetail()
                    },
                    countdownId = null
                )
            }
            entry<Modify>(metadata = detailPane()) {
                ModifyScreenVM(
                    paddingValues = paddingValues,
                    windowSizeClass = windowSize,
                    actionUpClicked = {
                        backStack.removeDetail()
                    },
                    countdownId = it.id
                )
            }
            entry<Tags>(metadata = detailPane()) {
                TagScreenVM(
                    paddingValues = paddingValues,
                    windowSizeClass = windowSize,
                    actionUpClicked = {
                        backStack.removeDetail()
                    }
                )
            }
            entry<Settings>(metadata = listPane()) {
                SettingsScreenVM(
                    paddingValues = paddingValues,
                    windowSize = windowSize,
                    goToMarketPage = goToMarketPage,
                    goToAboutThisApp = goToAboutThisApp,
                    navigateToBackup = { backStack.replaceDetail(SettingsBackup) },
                    navigateToPrivacy = { backStack.replaceDetail(SettingsPrivacyPolicy) },
                    actionUpClicked = {
                        backStack.navigateToHome()
                    }
                )
            }
            entry<SettingsPrivacyPolicy>(metadata = listPane()) {
                PrivacyPolicyLayout(
                    windowSizeClass = windowSize,
                    backClicked = {
                        backStack.removeDetail()
                    }
                )
            }
            entry<SettingsBackup>(metadata = listPane()) {
                BackupScreen(
                    windowSizeClass = windowSize,
                    backClicked = {
                        backStack.removeDetail()
                    }
                )
            }
        }
    )
}