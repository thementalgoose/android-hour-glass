package tmg.hourglass.settings

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.hourglass.BuildConfig
import tmg.hourglass.R
import tmg.hourglass.domain.connectors.CountdownConnector
import tmg.hourglass.prefs.PreferencesManager
import tmg.hourglass.prefs.ThemePref
import tmg.hourglass.prefs.ThemePref.DARK
import tmg.hourglass.prefs.ThemePref.LIGHT
import tmg.hourglass.testutils.*
import tmg.testutils.BaseTest
import tmg.testutils.livedata.assertEventFired
import tmg.testutils.livedata.test

internal class SettingsViewModelTest: BaseTest() {

    lateinit var sut: SettingsViewModel

    private val mockCountdownConnector: CountdownConnector = mockk(relaxed = true)
    private val mockPreferenceManager: PreferencesManager = mockk(relaxed = true)

    private val expectedList = listOf(
        Pair(R.string.settings_theme, null),
        Pair(R.string.settings_theme_theme_title, R.string.settings_theme_theme_description),
        Pair(R.string.settings_widgets, null),
        Pair(R.string.settings_widgets_refresh_title, R.string.settings_widgets_refresh_description),
        Pair(R.string.settings_widgets_updated_title, R.string.settings_widgets_updated_description),
        Pair(R.string.settings_reset, null),
        Pair(R.string.settings_reset_all_title, R.string.settings_reset_all_description),
        Pair(R.string.settings_help, null),
        Pair(R.string.settings_help_about_title, R.string.settings_help_about_description),
        Pair(R.string.settings_help_review_title, R.string.settings_help_review_description),
        Pair(R.string.settings_help_release_notes_title, R.string.settings_help_release_notes_description),
        Pair(R.string.settings_feedback, null),
        Pair(R.string.settings_help_suggestions_title, R.string.settings_help_suggestions_description),
        Pair(R.string.settings_help_crash_reporting_title, R.string.settings_help_crash_reporting_description),
        Pair(R.string.settings_help_analytics_title, R.string.settings_help_analytics_description),
        Pair(R.string.settings_help_shake_to_report_title, R.string.settings_help_shake_to_report_description),
        Pair(R.string.settings_privacy, null),
        Pair(R.string.settings_help_privacy_policy_title, R.string.settings_help_privacy_policy_description)
    )

    private fun initSUT() {
        sut = SettingsViewModel(mockCountdownConnector, mockPreferenceManager)
    }

    @BeforeEach
    internal fun setUp() {
        every { mockPreferenceManager.theme } returns ThemePref.DARK
        every { mockPreferenceManager.widgetShowUpdate } returns false
        every { mockPreferenceManager.analyticsEnabled } returns false
        every { mockPreferenceManager.crashReporting } returns false
        every { mockPreferenceManager.shakeToReport } returns false
    }

    @Test
    fun `initialising the VM sets default theme to pref value`() {
        every { mockPreferenceManager.theme } returns ThemePref.DARK
        initSUT()
        sut.outputs.currentThemePref.test {
            assertValue(DARK)
        }
    }

    @Test
    fun `initial settings list is expected`() {
        initSUT()
        sut.outputs.list.test {
            latestValue!!.assertExpectedOrder(expectedList)
        }
    }

    @Test
    fun `clicking setting for pref model invokes onclick`() {
        var invoker = 0
        val mockSetting = SettingsModel.Pref(
            title = 0,
            description = 0,
            onClick = {
                invoker += 1
            }
        )

        initSUT()

        sut.clickSetting(mockSetting)

        assertEquals(1, invoker)
    }

    @Test
    fun `clicking setting for switch pref model inverts state`() {
        var invoker = 0
        val mockSetting = SettingsModel.SwitchPref(
            title = 0,
            description = 0,
            getState = { false },
            saveState = { if (it) invoker += 1 }

        )

        initSUT()

        sut.clickSetting(mockSetting)

        assertEquals(1, invoker)
    }


    @Test
    fun `clicking refresh widgets updates widget event`() {
        initSUT()
        sut.clickSetting(sut.list.value!!.findPref(R.string.settings_widgets_refresh_title))
        sut.updateWidget.test {
            assertEventFired()
        }
    }

    @Test
    fun `clicking show date time in widgets updates widget event and updates pref value`() {
        initSUT()
        sut.clickSetting(sut.list.value!!.findSwitch(R.string.settings_widgets_updated_title))
        verify {
            mockPreferenceManager.widgetShowUpdate = true
        }
        sut.updateWidget.test {
            assertEventFired()
        }
    }

    @Test
    fun `clicking review fires open review event`() {
        initSUT()
        sut.clickSetting(sut.list.value!!.findPref(R.string.settings_help_review_title))
        sut.openReview.test {
            assertEventFired()
        }
    }

    @Test
    fun `clicking release notes fires open release notes event`() {
        initSUT()
        sut.clickSetting(sut.list.value!!.findPref(R.string.settings_help_release_notes_title))
        sut.openReleaseNotes.test {
            assertEventFired()
        }
    }

    @Test
    fun `clicking suggestions fires open suggestions event`() {
        initSUT()
        sut.clickSetting(sut.list.value!!.findPref(R.string.settings_help_suggestions_title))
        sut.openSuggestions.test {
            assertEventFired()
        }
    }

    @Test
    fun `clicking crash reporting fires open event`() {
        initSUT()
        sut.clickSetting(sut.list.value!!.findSwitch(R.string.settings_help_crash_reporting_title))
        verify {
            mockPreferenceManager.crashReporting = true
        }
        sut.crashReporting.test {
            assertValue(Pair(first = true, second = false))
        }
    }

    @Test
    fun `clicking analytics fires open event`() {
        initSUT()
        sut.clickSetting(sut.list.value!!.findSwitch(R.string.settings_help_analytics_title))
        verify {
            mockPreferenceManager.analyticsEnabled = true
        }
        sut.analyticsReporting.test {
            assertValue(Pair(first = true, second = false))
        }
    }

    @Test
    fun `clicking shake to report fires open event`() {
        initSUT()
        sut.clickSetting(sut.list.value!!.findSwitch(R.string.settings_help_shake_to_report_title))
        verify {
            mockPreferenceManager.shakeToReport = true
        }
        sut.shakeToReport.test {
            assertValue(Pair(first = true, second = false))
        }
    }

    @Test
    fun `clicking privacy policy fires open event`() {
        initSUT()
        sut.clickSetting(sut.list.value!!.findPref(R.string.settings_help_privacy_policy_title))
        sut.privacyPolicy.test {
            assertEventFired()
        }
    }


    @Test
    fun `clicking back fires go back event`() {
        initSUT()
        sut.inputs.clickBack()

        sut.outputs.goBack.test {
            assertEventFired()
        }
    }

    @Test
    fun `clicking theme updates pref`() {
        initSUT()
        sut.inputs.clickTheme(LIGHT)

        verify {
            mockPreferenceManager.theme = LIGHT
        }
        sut.outputs.currentThemePref.test {
            assertValue(LIGHT)
        }
    }


    @Test
    fun `clicking delete all deletes all items`() {
        initSUT()
        sut.inputs.clickDeleteAll()

        verify {
            mockCountdownConnector.deleteAll()
        }
    }


    @Test
    fun `clicking delete done deletes the done items`() {
        initSUT()
        sut.inputs.clickDeleteDone()

        verify {
            mockCountdownConnector.deleteDone()
        }
    }
}