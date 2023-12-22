package tmg.hourglass.presentation.settings

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import tmg.hourglass.ReleaseNotes
import tmg.hourglass.presentation.ThemePref
import tmg.hourglass.presentation.AppThemePreview
import tmg.hourglass.presentation.PreviewPhone
import tmg.hourglass.presentation.layouts.MasterDetailsPane
import tmg.hourglass.presentation.layouts.TitleBar
import tmg.hourglass.presentation.settings.components.SettingsHeader
import tmg.hourglass.presentation.settings.components.SettingsOption
import tmg.hourglass.presentation.settings.components.SettingsSwitch
import tmg.hourglass.presentation.settings.components.DeleteDialog
import tmg.hourglass.presentation.settings.components.ThemeDialog
import tmg.hourglass.settings.privacy.PrivacyPolicyLayout
import tmg.hourglass.settings.release.ReleaseLayout
import tmg.hourglass.strings.R.string

@Composable
internal fun SettingsScreen(
    windowSizeClass: WindowSizeClass,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsState()

    MasterDetailsPane(
        windowSizeClass = windowSizeClass,
        master = {
            SettingsOverviewScreen(
                windowSizeClass = windowSizeClass,
                uiState = uiState.value,
                setTheme = viewModel::setTheme,
                refreshWidgetsClicked = viewModel::refreshWidgets,
                aboutThisAppClicked = { },
                rateClicked = { },
                releaseNotesClicked = {
                    viewModel.clickScreen(SettingsType.RELEASE)
                },
                suggestionClicked = { },
                privacyPolicyClicked = {
                    viewModel.clickScreen(SettingsType.PRIVACY_POLICY)
                },
                deleteAllClicked = viewModel::deleteAll,
                setAnalytics = viewModel::setAnalytics,
                setCrashlytics = viewModel::setCrash,
                setShakeToReport = viewModel::setShakeToReport,
                setShowWidgetDate = viewModel::setWidgetDate,
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
                SettingsType.RELEASE -> {
                    ReleaseLayout(
                        content = ReleaseNotes.entries.reversed(),
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
    releaseNotesClicked: () -> Unit,
    suggestionClicked: () -> Unit,
    privacyPolicyClicked: () -> Unit,
    deleteAllClicked: () -> Unit,
    setAnalytics: (Boolean) -> Unit,
    setCrashlytics: (Boolean) -> Unit,
    setShakeToReport: (Boolean) -> Unit,
    setShowWidgetDate: (Boolean) -> Unit
) {

    val deletionConfirmationDialog = rememberSaveable { mutableStateOf(false) }
    val themeDialog = rememberSaveable { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        content = {
            item(key = "header") {
                TitleBar(
                    title = stringResource(id = string.settings_title),
                    showSpace = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact
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
            item(key = "widgets_2") {
                SettingsSwitch(
                    title = string.settings_widgets_updated_title,
                    subtitle = string.settings_widgets_updated_description,
                    isChecked = uiState.showWidgetUpdatedDate,
                    optionClicked = setShowWidgetDate
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
            item(key = "about_3") {
                SettingsOption(
                    title = string.settings_help_release_notes_title,
                    subtitle = string.settings_help_release_notes_description,
                    optionClicked = releaseNotesClicked
                )
            }
            item(key = "feedback_header") {
                SettingsHeader(title = string.settings_feedback)
            }
            item(key = "feedback_1") {
                SettingsOption(
                    title = string.settings_help_suggestions_title,
                    subtitle = string.settings_help_suggestions_description,
                    optionClicked = suggestionClicked
                )
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
        }
    )

    if (deletionConfirmationDialog.value) {
        DeleteDialog(
            confirmed = { deleteAllClicked() },
            dismissed = { deletionConfirmationDialog.value = false }
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
                showWidgetUpdatedDate = true,
                crashReporting = true,
                anonymousAnalytics = false,
                shakeToReport = true,
                theme = ThemePref.AUTO
            ),
            refreshWidgetsClicked = { },
            aboutThisAppClicked = { },
            rateClicked = { },
            releaseNotesClicked = { },
            suggestionClicked = { },
            setTheme = { },
            setAnalytics = { },
            setCrashlytics = { },
            setShakeToReport = { },
            setShowWidgetDate = { },
            privacyPolicyClicked = { },
            deleteAllClicked = { }
        )
    }
}