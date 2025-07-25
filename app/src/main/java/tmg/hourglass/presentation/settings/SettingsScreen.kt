package tmg.hourglass.presentation.settings

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import tmg.hourglass.presentation.AppThemePreview
import tmg.hourglass.presentation.PreviewPhone
import tmg.hourglass.presentation.ThemePref
import tmg.hourglass.presentation.layouts.MasterDetailsPane
import tmg.hourglass.presentation.layouts.TitleBar
import tmg.hourglass.presentation.settings.components.SettingsHeader
import tmg.hourglass.presentation.settings.components.SettingsOption
import tmg.hourglass.presentation.settings.components.SettingsSwitch
import tmg.hourglass.presentation.settings.components.ThemeDialog
import tmg.hourglass.presentation.settings.privacy.PrivacyPolicyLayout
import tmg.hourglass.presentation.utils.DeleteDialog
import tmg.hourglass.strings.R.string
import tmg.hourglass.widgets.updateAllWidgets

@Composable
internal fun SettingsScreenVM(
    paddingValues: PaddingValues,
    windowSize: WindowSizeClass,
    viewModel: SettingsViewModel = hiltViewModel(),
    goToMarketPage: () -> Unit,
    goToAboutThisApp: () -> Unit,
    actionUpClicked: () -> Unit
) {
    val uiState = viewModel.uiState.collectAsState()
    val context = LocalContext.current

    MasterDetailsPane(
        windowSizeClass = windowSize,
        master = {
            SettingsOverviewScreen(
                windowSizeClass = windowSize,
                uiState = uiState.value,
                setTheme = viewModel::setTheme,
                refreshWidgetsClicked = {
                    context.updateAllWidgets()
                },
                aboutThisAppClicked = goToAboutThisApp,
                rateClicked = goToMarketPage,
                privacyPolicyClicked = {
                    viewModel.clickScreen(SettingsType.PRIVACY_POLICY)
                },
                deleteAllClicked = viewModel::deleteAll,
                setAnalytics = viewModel::setAnalytics,
                setCrashlytics = viewModel::setCrash,
                setShakeToReport = viewModel::setShakeToReport,
                actionUpClicked = actionUpClicked
            )
        },
        detailsShow = uiState.value.screen != null,
        details = {
            when (uiState.value.screen) {
                SettingsType.PRIVACY_POLICY -> {
                    PrivacyPolicyLayout(
                        backClicked = viewModel::closeDetails
                    )
                }
                else -> {}
            }
        },
        detailsActionUpClicked = viewModel::closeDetails
    )
}

@Composable
private fun SettingsOverviewScreen(
    windowSizeClass: WindowSizeClass,
    uiState: UiState,
    setTheme: (ThemePref) -> Unit,
    refreshWidgetsClicked: () -> Unit,
    aboutThisAppClicked: () -> Unit,
    rateClicked: () -> Unit,
    privacyPolicyClicked: () -> Unit,
    deleteAllClicked: () -> Unit,
    setAnalytics: (Boolean) -> Unit,
    setCrashlytics: (Boolean) -> Unit,
    setShakeToReport: (Boolean) -> Unit,
    actionUpClicked: () -> Unit,
) {
    val deletionConfirmationDialog = rememberSaveable { mutableStateOf(false) }
    val themeDialog = rememberSaveable { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        content = {
            item(key = "edgetoedge-header") {
                Spacer(Modifier.statusBarsPadding())
            }
            item(key = "header") {
                TitleBar(
                    title = stringResource(id = string.settings_title),
                    showBack = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact,
                    actionUpClicked = actionUpClicked
                )
            }
            item(key = "theme_header") {
                SettingsHeader(title = string.settings_theme)
            }
            item(key = "theme_1") {
                SettingsOption(
                    title = string.settings_theme_theme_title,
                    subtitle = string.settings_theme_theme_description,
                    optionClicked = {
                        themeDialog.value = true
                    }
                )
            }
            item(key = "widgets_header") {
                SettingsHeader(title = string.settings_widgets)
            }
            item(key = "widgets_1") {
                SettingsOption(
                    title = string.settings_widgets_refresh_title,
                    subtitle = string.settings_widgets_refresh_description,
                    optionClicked = refreshWidgetsClicked
                )
            }
            item(key = "delete_header") {
                SettingsHeader(title = string.settings_reset)
            }
            item(key = "delete_1") {
                SettingsOption(
                    title = string.settings_reset_all_title,
                    subtitle = string.settings_reset_all_description,
                    optionClicked = {
                        deletionConfirmationDialog.value = true
                    }
                )
            }
            item(key = "about_header") {
                SettingsHeader(title = string.settings_help)
            }
            item(key = "about_1") {
                SettingsOption(
                    title = string.settings_help_about_title,
                    subtitle = string.settings_help_about_description,
                    optionClicked = aboutThisAppClicked
                )
            }
            item(key = "about_2") {
                SettingsOption(
                    title = string.settings_help_review_title,
                    subtitle = string.settings_help_review_description,
                    optionClicked = rateClicked
                )
            }
            item(key = "feedback_header") {
                SettingsHeader(title = string.settings_feedback)
            }
            item(key = "feedback_2") {
                SettingsSwitch(
                    title = string.settings_help_crash_reporting_title,
                    subtitle = string.settings_help_crash_reporting_description,
                    isChecked = uiState.crashReporting,
                    optionClicked = setCrashlytics
                )
            }
            item(key = "feedback_3") {
                SettingsSwitch(
                    title = string.settings_help_shake_to_report_title,
                    subtitle = string.settings_help_shake_to_report_title,
                    isChecked = uiState.shakeToReport,
                    optionClicked = setShakeToReport
                )
            }
            item(key = "privacy_header") {
                SettingsHeader(title = string.settings_privacy)
            }
            item(key = "privacy_1") {
                SettingsOption(
                    title = string.settings_help_privacy_policy_title,
                    subtitle = string.settings_help_privacy_policy_description,
                    optionClicked = privacyPolicyClicked
                )
            }
            item(key = "privacy_2") {
                SettingsSwitch(
                    title = string.settings_help_analytics_title,
                    subtitle = string.settings_help_analytics_description,
                    isChecked = uiState.anonymousAnalytics,
                    optionClicked = setAnalytics
                )
            }
            item(key = "edgetoedge-footer") {
                Spacer(Modifier.navigationBarsPadding())
            }
        }
    )

    if (deletionConfirmationDialog.value) {
        DeleteDialog(
            confirmed = { deleteAllClicked() },
            dismissed = { deletionConfirmationDialog.value = false },
            title = string.settings_reset_all_title,
            subtitle = string.settings_reset_all_description
        )
    }

    if (themeDialog.value) {
        ThemeDialog(
            theme = uiState.theme,
            themePicked = setTheme,
            dismissed = { themeDialog.value = false }
        )
    }
}

@PreviewPhone
@Composable
private fun PreviewOverview() {
    AppThemePreview {
        SettingsOverviewScreen(
            windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(300.dp, 400.dp)),
            uiState = UiState(
                screen = null,
                crashReporting = true,
                anonymousAnalytics = false,
                shakeToReport = true,
                theme = ThemePref.AUTO
            ),
            refreshWidgetsClicked = { },
            aboutThisAppClicked = { },
            rateClicked = { },
            setTheme = { },
            setAnalytics = { },
            setCrashlytics = { },
            setShakeToReport = { },
            privacyPolicyClicked = { },
            deleteAllClicked = { },
            actionUpClicked = { }
        )
    }
}