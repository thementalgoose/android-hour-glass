package tmg.hourglass.presentation.settings.backup

data class BackupUiState(
    val jsonBackupState: Boolean? = null,
    val jsonRestoreState: Boolean? = null,
    val legacyRestoreState: Boolean? = null
)