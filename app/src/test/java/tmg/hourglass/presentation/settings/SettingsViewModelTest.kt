package tmg.hourglass.presentation.settings

import app.cash.turbine.test
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.hourglass.domain.connectors.CountdownConnector
import tmg.hourglass.prefs.PreferencesManager
import tmg.hourglass.presentation.ThemePref
import tmg.hourglass.presentation.usecases.ChangeThemeUseCase
import tmg.testutils.BaseTest

internal class SettingsViewModelTest: BaseTest() {

    private lateinit var underTest: SettingsViewModel

    private val mockCountdownConnector: CountdownConnector = mockk(relaxed = true)
    private val mockPreferenceManager: PreferencesManager = mockk(relaxed = true)
    private val mockChangeThemeUseCase: ChangeThemeUseCase = mockk(relaxed = true)

    private fun initUnderTest() {
        underTest = SettingsViewModel(
            prefManager = mockPreferenceManager,
            countdownConnector = mockCountdownConnector,
            changeThemeUseCase = mockChangeThemeUseCase
        )
    }

    @BeforeEach
    fun setUp() {
        every { mockPreferenceManager.theme } returns ThemePref.DARK
        every { mockPreferenceManager.crashReporting } returns true
        every { mockPreferenceManager.analyticsEnabled } returns true
        every { mockPreferenceManager.shakeToReport } returns true
        every { mockPreferenceManager.widgetShowUpdate } returns true
    }

    @Test
    fun `loads initial state`() = runTest {
        initUnderTest()
        underTest.uiState.test {
            val item = awaitItem()
            assertEquals(ThemePref.DARK, item.theme)
            assertEquals(true, item.crashReporting)
            assertEquals(true, item.anonymousAnalytics)
            assertEquals(true, item.shakeToReport)
            assertEquals(true, item.showWidgetUpdatedDate)
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

            underTest.clickScreen(SettingsType.RELEASE)
            val item2 = awaitItem()
            assertEquals(SettingsType.RELEASE, item2.screen)

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
    fun `shake to report updates value`() = runTest {
        initUnderTest()
        underTest.setShakeToReport(true)
        verify {
            mockPreferenceManager.shakeToReport = true
        }
    }

    @Test
    fun `set widget date updates value`() = runTest {
        initUnderTest()
        underTest.setWidgetDate(true)
        verify {
            mockPreferenceManager.widgetShowUpdate = true
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
            mockCountdownConnector.deleteAll()
        }
    }
}