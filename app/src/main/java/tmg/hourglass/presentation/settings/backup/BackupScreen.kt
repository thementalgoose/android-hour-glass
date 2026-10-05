package tmg.hourglass.presentation.settings.backup

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import tmg.hourglass.core.crashlytics.screenview.ScreenView
import tmg.hourglass.presentation.AppTheme
import tmg.hourglass.presentation.layouts.TitleBar
import tmg.hourglass.presentation.settings.components.SettingsHeader
import tmg.hourglass.presentation.settings.components.SettingsOption
import tmg.hourglass.strings.R.string

const val JSON_FILE_NAME = "HourGlass.json"
const val JSON_MIME_TYPE = "application/json"
const val LEGACY_MIME_TYPE = "application/octet-stream"

@Composable
fun BackupScreen(
    paddingValues: PaddingValues,
    windowSizeClass: WindowSizeClass,
    backClicked: () -> Unit,
    viewModel: BackupViewModel = hiltViewModel()
) {
    ScreenView("Settings - Backup")

    val uiState = viewModel.uiState.collectAsState()
    val createJsonDocument = rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument(JSON_MIME_TYPE)) { uri ->
        viewModel.createJsonBackup(uri)
    }
    val openJsonDocument = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        viewModel.restoreJsonBackup(uri)
    }
    val openLegacyDocument = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        viewModel.restoreLegacyBackup(uri)
    }

    LazyColumn(
        contentPadding = paddingValues,
        modifier = Modifier.fillMaxSize()
    ) {
        item(key = "header") {
            TitleBar(
                titleModifier = Modifier.padding(start = AppTheme.dimensions.paddingMedium),
                title = stringResource(id = string.settings_backup_restore_title),
                showBack = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact,
                actionUpClicked = backClicked
            )
        }
        item(key = "auto_backup") {
            SettingsOption(
                title = string.settings_autobackup_title,
                subtitle = string.settings_autobackup_description,
                optionClicked = { }
            )
        }
        item(key = "json_backup_header") {
            SettingsHeader(title = string.settings_backup_restore_json_title)
        }
        item(key = "json_backup") {
            SettingsOption(
                title = string.settings_json_backup_title,
                subtitle = string.settings_json_backup_description,
                optionClicked = {
                    createJsonDocument.launch(JSON_FILE_NAME)
                },
                label = {
                    Label(uiState.value.jsonBackupState)
                }
            )
        }
        item(key = "json_restore") {
            SettingsOption(
                title = string.settings_json_restore_title,
                subtitle = string.settings_json_restore_description,
                optionClicked = {
                    openJsonDocument.launch(arrayOf(JSON_MIME_TYPE))
                },
                label = {
                    Label(uiState.value.jsonRestoreState)
                }
            )
        }
        item(key = "legacy_backup_header") {
            SettingsHeader(title = string.settings_restore_legacy_title)
        }
        item(key = "legacy_restore") {
            SettingsOption(
                title = string.settings_restore_title,
                subtitle = string.settings_restore_description,
                optionClicked = {
                    openLegacyDocument.launch(arrayOf(LEGACY_MIME_TYPE))
                },
                label = {
                    Label(uiState.value.legacyRestoreState)
                }
            )
        }
    }
}

@Composable
private fun Label(
    successful: Boolean?,
    modifier: Modifier = Modifier,
) {
    when (successful) {
        true -> {
            Icon(
                modifier = modifier,
                imageVector = Icons.Filled.CheckCircle,
                contentDescription = null,
                tint = AppTheme.colors.successColor
            )
        }
        false -> {
            Icon(
                modifier = modifier,
                imageVector = Icons.Default.Warning,
                contentDescription = null,
                tint = AppTheme.colors.errorColor
            )
        }
        null -> { }
    }
}