package tmg.hourglass.presentation.settings.backup

import android.net.Uri
import android.util.Log
import androidx.core.net.toFile
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tmg.hourglass.room.backups.BackupManager
import javax.inject.Inject

@HiltViewModel
class BackupViewModel @Inject constructor(
    private val backupManager: BackupManager
): ViewModel() {

    private val _uiState: MutableStateFlow<BackupUiState> = MutableStateFlow(BackupUiState())
    val uiState: StateFlow<BackupUiState> = _uiState

    fun createBackup(uri: Uri?) {
        Log.d("Backup", "Create backup - $uri")
        _uiState.update { it.copy(backupState = null) }
        val uri = uri ?: return
        viewModelScope.launch {
            val result = backupManager.backup(uri)
            _uiState.update { it.copy(backupState = result) }
        }
    }

    fun restoreBackup(uri: Uri?) {
        Log.d("Backup", "Restoring backup - $uri")
        _uiState.update { it.copy(restoreState = null) }
        val uri = uri ?: return
        viewModelScope.launch {
            val result = backupManager.restore(uri)
            _uiState.update { it.copy(restoreState = result) }
        }
    }
}