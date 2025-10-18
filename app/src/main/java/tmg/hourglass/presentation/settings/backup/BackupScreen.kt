package tmg.hourglass.presentation.settings.backup

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import tmg.hourglass.presentation.AppTheme
import tmg.hourglass.presentation.layouts.TitleBar
import tmg.hourglass.presentation.settings.components.SettingsHeader
import tmg.hourglass.presentation.settings.components.SettingsOption
import tmg.hourglass.strings.R.string

const val FILE_NAME = "HourGlass.backup"
const val MIME_TYPE = "application/octet-stream"

@Composable
fun BackupScreen(
    windowSizeClass: WindowSizeClass,
    backClicked: () -> Unit,
    viewModel: BackupViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsState()
    val createDocument = rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("application/octet-stream")) {
        viewModel.createBackup(it)
    }
    val openDocument = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) {
        viewModel.restoreBackup(it)
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item(key = "edgetoedge-header") {
            Spacer(Modifier.statusBarsPadding())
        }
        item(key = "header") {
            TitleBar(
                title = stringResource(id = string.settings_backup_restore_title),
                showBack = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact,
                actionUpClicked = backClicked
            )
        }
        item(key = "backup_1") {
            SettingsOption(
                title = string.settings_autobackup_title,
                subtitle = string.settings_autobackup_description,
                optionClicked = { }
            )
        }
        item(key = "backup_header") {
            SettingsHeader(title = string.settings_backup_restore_title)
        }
        item(key = "backup_2") {
            SettingsOption(
                title = string.settings_backup_title,
                subtitle = string.settings_backup_description,
                optionClicked = {
                    createDocument.launch(FILE_NAME)
                },
                label = {
                    Label(uiState.value.backupState)
                }
            )
        }
        item(key = "backup_3") {
            SettingsOption(
                title = string.settings_restore_title,
                subtitle = string.settings_restore_description,
                optionClicked = {
                    openDocument.launch(arrayOf(MIME_TYPE))
                },
                label = {
                    Label(uiState.value.restoreState)
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