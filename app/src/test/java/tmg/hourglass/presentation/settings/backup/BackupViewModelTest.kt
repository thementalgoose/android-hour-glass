package tmg.hourglass.presentation.settings.backup

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tmg.hourglass.backup.BackupManager
import tmg.hourglass.domain.usecases.CancelAllNotificationsUseCase
import tmg.hourglass.domain.usecases.ScheduleAllNotificationsUseCase
import tmg.hourglass.room.backups.LegacyBackupManager
import tmg.testutils.BaseTest
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

internal class BackupViewModelTest : BaseTest() {

    private val backupManager: BackupManager = mockk(relaxed = true)
    private val legacyBackupManager: LegacyBackupManager = mockk(relaxed = true)
    private val cancelAllNotificationsUseCase: CancelAllNotificationsUseCase = mockk(relaxed = true)
    private val scheduleAllNotificationsUseCase: ScheduleAllNotificationsUseCase = mockk(relaxed = true)
    private val context: Context = mockk(relaxed = true)
    private val contentResolver: ContentResolver = mockk(relaxed = true)

    init {
        every { context.contentResolver } returns contentResolver
    }

    private val underTest = BackupViewModel(
        backupManager = backupManager,
        legacyBackupManager = legacyBackupManager,
        cancelAllNotificationsUseCase = cancelAllNotificationsUseCase,
        scheduleAllNotificationsUseCase = scheduleAllNotificationsUseCase,
        context = context
    )

    @Test
    fun `createJsonBackup writes json to content resolver and updates uiState`() = runTest {
        val mockUri: Uri = mockk()
        val outputStream = ByteArrayOutputStream()
        every { contentResolver.openOutputStream(mockUri) } returns outputStream
        coEvery { backupManager.generateBackupJSON() } returns "{\"schemaVersion\":\"v1\"}"

        underTest.uiState.test {
            assertEquals(BackupUiState(), awaitItem())

            underTest.createJsonBackup(mockUri)

            assertEquals(BackupUiState(jsonBackupState = true), awaitItem())
        }

        assertEquals("{\"schemaVersion\":\"v1\"}", outputStream.toString(Charsets.UTF_8.name()))
    }

    @Test
    fun `restoreJsonBackup when successful cancels and schedules notifications`() = runTest {
        val mockUri: Uri = mockk()
        val jsonInput = "{\"schemaVersion\":\"v1\"}"
        val inputStream = ByteArrayInputStream(jsonInput.toByteArray(Charsets.UTF_8))
        every { contentResolver.openInputStream(mockUri) } returns inputStream
        coEvery { backupManager.restoreBackupJSON(jsonInput) } returns true

        underTest.uiState.test {
            assertEquals(BackupUiState(), awaitItem())

            underTest.restoreJsonBackup(mockUri)

            assertEquals(BackupUiState(jsonRestoreState = true), awaitItem())
        }

        coVerify(exactly = 1) { cancelAllNotificationsUseCase(force = true) }
        coVerify(exactly = 1) { scheduleAllNotificationsUseCase() }
    }

    @Test
    fun `restoreJsonBackup when failed does not cancel or schedule notifications`() = runTest {
        val mockUri: Uri = mockk()
        val jsonInput = "{\"schemaVersion\":\"v999\"}"
        val inputStream = ByteArrayInputStream(jsonInput.toByteArray(Charsets.UTF_8))
        every { contentResolver.openInputStream(mockUri) } returns inputStream
        coEvery { backupManager.restoreBackupJSON(jsonInput) } returns false

        underTest.uiState.test {
            assertEquals(BackupUiState(), awaitItem())

            underTest.restoreJsonBackup(mockUri)

            assertEquals(BackupUiState(jsonRestoreState = false), awaitItem())
        }

        coVerify(exactly = 0) { cancelAllNotificationsUseCase(any()) }
        coVerify(exactly = 0) { scheduleAllNotificationsUseCase() }
    }

    @Test
    fun `restoreLegacyBackup when successful force cancels and schedules notifications`() = runTest {
        val mockUri: Uri = mockk()
        coEvery { legacyBackupManager.restore(mockUri) } returns true

        underTest.uiState.test {
            assertEquals(BackupUiState(), awaitItem())

            underTest.restoreLegacyBackup(mockUri)

            assertEquals(BackupUiState(legacyRestoreState = true), awaitItem())
        }

        coVerify(exactly = 1) { cancelAllNotificationsUseCase(force = true) }
        coVerify(exactly = 1) { scheduleAllNotificationsUseCase() }
    }

    @Test
    fun `restoreLegacyBackup when failed does not cancel or schedule notifications`() = runTest {
        val mockUri: Uri = mockk()
        coEvery { legacyBackupManager.restore(mockUri) } returns false

        underTest.uiState.test {
            assertEquals(BackupUiState(), awaitItem())

            underTest.restoreLegacyBackup(mockUri)

            assertEquals(BackupUiState(legacyRestoreState = false), awaitItem())
        }

        coVerify(exactly = 0) { cancelAllNotificationsUseCase(any()) }
        coVerify(exactly = 0) { scheduleAllNotificationsUseCase() }
    }
}
