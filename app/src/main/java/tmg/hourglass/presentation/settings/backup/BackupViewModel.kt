package tmg.hourglass.presentation.settings.backup

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tmg.hourglass.backup.BackupManager
import tmg.hourglass.domain.usecases.CancelAllNotificationsUseCase
import tmg.hourglass.domain.usecases.ScheduleAllNotificationsUseCase
import tmg.hourglass.room.backups.LegacyBackupManager
import javax.inject.Inject

@HiltViewModel
class BackupViewModel @Inject constructor(
    private val backupManager: BackupManager,
    private val legacyBackupManager: LegacyBackupManager,
    private val cancelAllNotificationsUseCase: CancelAllNotificationsUseCase,
    private val scheduleAllNotificationsUseCase: ScheduleAllNotificationsUseCase,
    @ApplicationContext private val context: Context
): ViewModel() {

    private val _uiState: MutableStateFlow<BackupUiState> = MutableStateFlow(BackupUiState())
    val uiState: StateFlow<BackupUiState> = _uiState

    fun createJsonBackup(uri: Uri?) {
        Log.d("Backup", "Create JSON backup - $uri")
        _uiState.update { it.copy(jsonBackupState = null) }
        val targetUri = uri ?: return
        viewModelScope.launch {
            val success = try {
                val jsonString = backupManager.generateBackupJSON()
                context.contentResolver.openOutputStream(targetUri)?.use { outputStream ->
                    outputStream.write(jsonString.toByteArray(Charsets.UTF_8))
                }
                true
            } catch (e: Exception) {
                Log.e("Backup", "Failed to export JSON backup", e)
                false
            }
            _uiState.update { it.copy(jsonBackupState = success) }
        }
    }

    fun restoreJsonBackup(uri: Uri?) {
        Log.d("Backup", "Restoring JSON backup - $uri")
        _uiState.update { it.copy(jsonRestoreState = null) }
        val targetUri = uri ?: return
        viewModelScope.launch {
            val result = try {
                val jsonString = context.contentResolver.openInputStream(targetUri)?.use { inputStream ->
                    inputStream.bufferedReader(Charsets.UTF_8).readText()
                } ?: ""
                backupManager.restoreBackupJSON(jsonString)
            } catch (e: Exception) {
                Log.e("Backup", "Failed to restore JSON backup", e)
                false
            }
            if (result) {
                cancelAllNotificationsUseCase(force = true)
                scheduleAllNotificationsUseCase()
            }
            _uiState.update { it.copy(jsonRestoreState = result) }
        }
    }

    fun restoreLegacyBackup(uri: Uri?) {
        Log.d("Backup", "Restoring legacy backup - $uri")
        _uiState.update { it.copy(legacyRestoreState = null) }
        val targetUri = uri ?: return
        viewModelScope.launch {
            val result = legacyBackupManager.restore(targetUri)
            if (result) {
                cancelAllNotificationsUseCase(force = true)
                scheduleAllNotificationsUseCase()
            }
            _uiState.update { it.copy(legacyRestoreState = result) }
        }
    }
}