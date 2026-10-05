package tmg.hourglass.room.backups

import android.content.Context
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import tmg.hourglass.core.googleanalytics.CrashReporter
import tmg.hourglass.room.HourGlassDatabase
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject

internal class LegacyBackupManagerImpl @Inject constructor(
    private val database: HourGlassDatabase,
    private val crashReporter: CrashReporter,
    @param:ApplicationContext
    private val applicationContext: Context
): LegacyBackupManager {

    override suspend fun restore(fromFile: Uri): Boolean {
        val path = database.openHelper.writableDatabase.path ?: return false
        try {
            val inputStream = applicationContext.contentResolver.openInputStream(fromFile)!!
            val originalFile = File(path)
            val outputStream = FileOutputStream(originalFile)
            inputStream.copyTo(outputStream)
            outputStream.close()
            inputStream.close()
            return true
        } catch (e: IOException) {
            crashReporter.logException(e)
            return false
        } catch (e: NullPointerException) {
            crashReporter.logException(e)
            return false
        }
    }
}
