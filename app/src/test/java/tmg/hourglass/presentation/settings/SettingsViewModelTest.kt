package tmg.hourglass.presentation.settings

import app.cash.turbine.test
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.hourglass.domain.repositories.CountdownRepository
import tmg.hourglass.prefs.PreferencesManager
import tmg.hourglass.presentation.ThemePref
import tmg.hourglass.presentation.usecases.ChangeThemeUseCase
import tmg.testutils.BaseTest

internal class SettingsViewModelTest: BaseTest() {

    private lateinit var underTest: SettingsViewModel

    private val mockCountdownRepository: CountdownRepository = mockk(relaxed = true)
    private val mockPreferenceManager: PreferencesManager = mockk(relaxed = true)
    private val mockChangeThemeUseCase: ChangeThemeUseCase = mockk(relaxed = true)

    private fun initUnderTest() {
        underTest = SettingsViewModel(
            prefManager = mockPreferenceManager,
            countdownRepository = mockCountdownRepository,
            changeThemeUseCase = mockChangeThemeUseCase
        )
    }

    @BeforeEach
    fun setUp() {
        every { mockPreferenceManager.theme } returns ThemePref.DARK
        every { mockPreferenceManager.crashReporting } returns true
        every { mockPreferenceManager.analyticsEnabled } returns true
    }

    @Test
    fun `loads initial state`() = runTest {
        initUnderTest()
        underTest.uiState.test {
            val item = awaitItem()
            assertEquals(ThemePref.DARK, item.theme)
            assertEquals(true, item.crashReporting)
            assertEquals(true, item.anonymousAnalytics)
        }
    }

    @Test
    fun `click screen updates screen state`() = runTest {
        initUnderTest()
        underTest.uiState.test {
            val item1 = awaitItem()
            assertEquals(null, item1.screen)

            underTest.clickScreen(SettingsType.PRIVACY_POLICY)
            val item2 = awaitItem()
            assertEquals(SettingsType.PRIVACY_POLICY, item2.screen)
        }
    }

    @Test
    fun `closing details sets screen to null`() = runTest {
        initUnderTest()
        underTest.uiState.test {
            val item1 = awaitItem()
            assertEquals(null, item1.screen)

            underTest.clickScreen(SettingsType.PRIVACY_POLICY)
            val item2 = awaitItem()
            assertEquals(SettingsType.PRIVACY_POLICY, item2.screen)

            underTest.closeDetails()
            val item3 = awaitItem()
            assertEquals(null, item3.screen)
        }
    }

    @Test
    fun `crash updates value`() = runTest {
        initUnderTest()
        underTest.setCrash(true)
        verify {
            mockPreferenceManager.crashReporting = true
        }
    }


    @Test
    fun `analytics updates value`() = runTest {
        initUnderTest()
        underTest.setAnalytics(true)
        verify {
            mockPreferenceManager.analyticsEnabled = true
        }
    }

    @Test
    fun `set theme calls change theme use case`() = runTest {
        initUnderTest()
        underTest.setTheme(ThemePref.DARK)
        verify {
            mockChangeThemeUseCase.update(ThemePref.DARK)
            mockPreferenceManager.theme = ThemePref.DARK
        }
    }

    @Test
    fun `delete all calls countdown connector`() = runTest {
        initUnderTest()
        underTest.deleteAll()
        verify {
            mockCountdownRepository.deleteAll()
        }
    }
}