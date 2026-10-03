package tmg.hourglass.presentation.settings.backup

import android.net.Uri
import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import tmg.hourglass.domain.usecases.CancelAllNotificationsUseCase
import tmg.hourglass.domain.usecases.ScheduleAllNotificationsUseCase
import tmg.hourglass.room.backups.BackupManager
import tmg.testutils.BaseTest

internal class BackupViewModelTest : BaseTest() {

    private val backupManager: BackupManager = mockk(relaxed = true)
    private val cancelAllNotificationsUseCase: CancelAllNotificationsUseCase = mockk(relaxed = true)
    private val scheduleAllNotificationsUseCase: ScheduleAllNotificationsUseCase = mockk(relaxed = true)

    private val underTest = BackupViewModel(
        backupManager = backupManager,
        cancelAllNotificationsUseCase = cancelAllNotificationsUseCase,
        scheduleAllNotificationsUseCase = scheduleAllNotificationsUseCase
    )

    @Test
    fun `createBackup updates uiState with backup result`() = runTest {
        val mockUri: Uri = mockk()
        coEvery { backupManager.backup(mockUri) } returns true

        underTest.uiState.test {
            assertEquals(BackupUiState(), awaitItem())

            underTest.createBackup(mockUri)

            assertEquals(BackupUiState(backupState = true), awaitItem())
        }
    }

    @Test
    fun `restoreBackup when successful force cancels and schedules notifications`() = runTest {
        val mockUri: Uri = mockk()
        coEvery { backupManager.restore(mockUri) } returns true

        underTest.uiState.test {
            assertEquals(BackupUiState(), awaitItem())

            underTest.restoreBackup(mockUri)

            assertEquals(BackupUiState(restoreState = true), awaitItem())
        }

        coVerify(exactly = 1) { cancelAllNotificationsUseCase(force = true) }
        coVerify(exactly = 1) { scheduleAllNotificationsUseCase() }
    }

    @Test
    fun `restoreBackup when failed does not cancel or schedule notifications`() = runTest {
        val mockUri: Uri = mockk()
        coEvery { backupManager.restore(mockUri) } returns false

        underTest.uiState.test {
            assertEquals(BackupUiState(), awaitItem())

            underTest.restoreBackup(mockUri)

            assertEquals(BackupUiState(restoreState = false), awaitItem())
        }

        coVerify(exactly = 0) { cancelAllNotificationsUseCase(any()) }
        coVerify(exactly = 0) { scheduleAllNotificationsUseCase() }
    }
}
