package tmg.hourglass.room.backups

import android.content.Context
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import tmg.hourglass.core.googleanalytics.CrashReporter
import tmg.hourglass.room.HourGlassDatabase
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject

internal class BackupManagerImpl @Inject constructor(
    private val database: HourGlassDatabase,
    private val crashReporter: CrashReporter,
    @param:ApplicationContext
    private val applicationContext: Context
): BackupManager {
    override suspend fun backup(toFile: Uri): Boolean {
        val path = database.openHelper.writableDatabase.path ?: return false
        try {
            val outputStream = applicationContext.contentResolver.openOutputStream(toFile)!!
            val originalFile = File(path)
            val inputStream = FileInputStream(originalFile)
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