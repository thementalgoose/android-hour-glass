package tmg.hourglass.room.backups

import android.net.Uri
import java.io.File

interface BackupManager {
    suspend fun backup(toFile: Uri): Boolean
    suspend fun restore(fromFile: Uri): Boolean
}