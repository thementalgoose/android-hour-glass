package tmg.hourglass.room.backups

import android.net.Uri

interface LegacyBackupManager {
    suspend fun restore(fromFile: Uri): Boolean
}
