package tmg.hourglass.settings

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.components.prefs.AppPreferencesItem
import tmg.hourglass.BuildConfig
import tmg.hourglass.R
import tmg.hourglass.data.connectors.CountdownConnector
import tmg.hourglass.prefs.PreferencesManager
import tmg.hourglass.prefs.ThemePref.DARK
import tmg.hourglass.prefs.ThemePref.LIGHT
import tmg.hourglass.testutils.*
import tmg.hourglass.testutils.assertEventFired
import tmg.hourglass.testutils.test
import tmg.hourglass.testutils.testObserve

internal class SettingsViewModelTest: BaseTest() {

    lateinit var sut: SettingsViewModel

    private val mockCountdownConnector: CountdownConnector = mockk(relaxed = true)
    private val mockPreferenceManager: PreferencesManager = mockk(relaxed = true)

    private val keyTheme: String = "theme_app"
    private val keyKeepInNow: String = "keep_in_now"
    private val keyDeleteAll: String = "delete_all"
    private val keyDeleteDone: String = "delete_done"
    private val keyWidgetsRefresh: String = "widget_refresh"
    private val keyWidgetsUpdated: String = "widget_updated"
    private val keyHelpAbout: String = "help_about"
    private val keyHelpReview: String = "help_review"
    private val keyHelpRelease: String = "help_release"
    private val keyFeedbackCrash: String = "feedback_crash_reporting"
    private val keyFeedbackAnalytics: String = "feedback_analytics"
    private val keyFeedbackSuggestions: String = "feedback_suggestions"
    private val keyFeedbackShake: String = "feedback_shake"
    private val keyPrivacyPolicy: String = "privacy_policy"


    private var expectedList: List<AppPreferencesItem> = listOf(
        AppPreferencesItem.Category(R.string.settings_theme),
        AppPreferencesItem.Preference(
            prefKey = keyTheme,
            title = R.string.settings_theme_theme_title,
            description = R.string.settings_theme_theme_description
        ),

        AppPreferencesItem.Category(R.string.settings_widgets),
        AppPreferencesItem.Preference(
            prefKey = keyWidgetsRefresh,
            title = R.string.settings_widgets_refresh_title,
            description = R.string.settings_widgets_refresh_description
        ),
        AppPreferencesItem.SwitchPreference(
            prefKey = keyWidgetsUpdated,
            title = R.string.settings_widgets_updated_title,
            description = R.string.settings_widgets_updated_description,
            isChecked = false
        ),

        AppPreferencesItem.Category(R.string.settings_reset),
        AppPreferencesItem.Preference(
            prefKey = keyDeleteAll,
            title = R.string.settings_reset_all_title,
            description = R.string.settings_reset_all_description
        ),
        AppPreferencesItem.Preference(
            prefKey = keyDeleteDone,
            title = R.string.settings_reset_done_title,
            description = R.string.settings_reset_done_description
        ),

        AppPreferencesItem.Category(R.string.settings_help),
        AppPreferencesItem.Preference(
            prefKey = keyHelpAbout,
            title = R.string.settings_help_about_title,
            description = R.string.settings_help_about_description
        ),
        AppPreferencesItem.Preference(
            prefKey = keyHelpReview,
            title = R.string.settings_help_review_title,
            description = R.string.settings_help_review_description
        ),
        AppPreferencesItem.Preference(
            prefKey = keyHelpRelease,
            title = R.string.settings_help_release_notes_title,
            description = R.string.settings_help_release_notes_description
        ),

        AppPreferencesItem.Category(R.string.settings_feedback),
        AppPreferencesItem.Preference(
            prefKey = keyFeedbackSuggestions,
            title = R.string.settings_help_suggestions_title,
            description = R.string.settings_help_suggestions_description
        ),
        AppPreferencesItem.SwitchPreference(
            prefKey = keyFeedbackCrash,
            title = R.string.settings_help_crash_reporting_title,
            description = R.string.settings_help_crash_reporting_description,
            isChecked = false
        ),
        AppPreferencesItem.SwitchPreference(
            prefKey = keyFeedbackAnalytics,
            title = R.string.settings_help_analytics_title,
            description = R.string.settings_help_analytics_description,
            isChecked = false
        ),
        AppPreferencesItem.SwitchPreference(
            prefKey = keyFeedbackShake,
            title = R.string.settings_help_shake_to_report_title,
            description = R.string.settings_help_shake_to_report_description,
            isChecked = false
        ),

        AppPreferencesItem.Category(R.string.settings_privacy),
        AppPreferencesItem.Preference(
            prefKey = keyPrivacyPolicy,
            title = R.string.settings_help_privacy_policy_title,
            description = R.string.settings_help_privacy_policy_description
        )
    )

    private fun initSUT() {
        sut = SettingsViewModel(mockCountdownConnector, mockPreferenceManager)
    }

    @Test
    fun `SettingsViewModel initial list of settings is emitted with all set to false`() {

        initSUT()

        sut.outputs.list.test {
            assertValue(expectedList)
        }
    }

    @Test
    fun `SettingsViewModel theme preference is set to the value set in shared prefs`() {

        every { mockPreferenceManager.theme } returns DARK

        initSUT()

        sut.outputs.themeSelected.test {
            assertValue(DARK)
        }
    }

    @Test
    fun `SettingsViewModel clicking back event emits back behavior`() {

        initSUT()

        sut.inputs.clickBack()

        sut.outputs.goBack.test {
            assertEventFired()
        }
    }

    @Test
    fun `SettingsViewModel delete all emits deletion event and calls delete all in connector`() {

        initSUT()

        sut.inputs.clickDeleteAll()

        verify { mockCountdownConnector.deleteAll() }
        sut.outputs.deletedAll.test {
            assertEventFired()
        }
    }

    @Test
    fun `SettingsViewModel delete done emits deletion event and calls delete done in connector`() {

        initSUT()

        sut.inputs.clickDeleteDone()

        verify { mockCountdownConnector.deleteDone() }
        sut.outputs.deletedDone.test {
            assertEventFired()
        }
    }

    @Test
    fun `SettingsViewModel clicking a theme triggers theme updates`() {

        every { mockPreferenceManager.theme } returns DARK

        initSUT()

        sut.inputs.clickTheme(LIGHT)

        sut.outputs.themeSelected.test {
            assertValue(LIGHT)
        }
        sut.outputs.themeUpdated.test {
            assertEventFired()
        }
        verify {
            mockPreferenceManager.theme = LIGHT
        }
    }

    @Test
    fun `SettingsViewModel clicking widget update interacts with pref`() {

        every { mockPreferenceManager.widgetShowUpdate } returns false

        initSUT()

        sut.inputs.clickWidgetUpdate(true)

        verify {
            mockPreferenceManager.widgetShowUpdate = true
        }
    }

    @Test
    fun `SettingsViewModel clicking about fires about event`() {

        initSUT()

        sut.inputs.clickAbout()

        sut.outputs.openAbout.test {
            assertEventFired()
        }
    }

    @Test
    fun `SettingsViewModel clicking review fires review event`() {

        initSUT()

        sut.inputs.clickReview()

        sut.outputs.openReview.test {
            assertDataEventValue("https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID)
        }
    }

    @Test
    fun `SettingsViewModel clicking release notes fires release event`() {

        initSUT()

        sut.inputs.clickReleaseNotes()

        sut.outputs.openReleaseNotes.test {
            assertEventFired()
        }
    }

    @Test
    fun `SettingsViewModel crash reporting initial value has show update to be false, clicking it shows crash reportings banner`() {

        every { mockPreferenceManager.crashReporting } returns false

        initSUT()

        val crashReportingObserver = sut.outputs.crashReporting.testObserve()

        crashReportingObserver.assertValue(Pair(false, second = false))

        every { mockPreferenceManager.crashReporting } returns true
        sut.inputs.clickCrashReporting(true)
        verify { mockPreferenceManager.crashReporting = true }

        crashReportingObserver.assertValue(Pair(true, second = true))
    }

    @Test
    fun `SettingsViewModel analytics initial value has show update to be false, clicking it shows analytics banner`() {

        every { mockPreferenceManager.analyticsEnabled } returns false

        initSUT()

        val crashReportingObserver = sut.outputs.analyticsReporting.testObserve()

        crashReportingObserver.assertValue(Pair(false, second = false))

        every { mockPreferenceManager.analyticsEnabled } returns true
        sut.inputs.clickAnalytics(true)
        verify { mockPreferenceManager.analyticsEnabled = true }

        crashReportingObserver.assertValue(Pair(true, second = true))
    }

    @Test
    fun `SettingsViewModel click suggestions fires open suggestions event`() {

        initSUT()

        sut.inputs.clickSuggestions()

        sut.outputs.openSuggestions.test {
            assertEventFired()
        }
    }

    @Test
    fun `SettingsViewModel shake to report initial value has show update to be false, clicking it shows shake to report banner`() {

        every { mockPreferenceManager.shakeToReport } returns false

        initSUT()

        val shakeToReportObserver = sut.outputs.shakeToReport.testObserve()

        shakeToReportObserver.assertValue(Pair(false, second = false))

        every { mockPreferenceManager.shakeToReport } returns true
        sut.inputs.clickShakeToReport(true)
        verify { mockPreferenceManager.shakeToReport = true }

        shakeToReportObserver.assertValue(Pair(true, second = true))
    }

    @Test
    fun `SettingsViewModel click privacy policy fires open privacy policy event`() {

        initSUT()

        sut.inputs.clickPrivacyPolicy()

        sut.outputs.privacyPolicy.test {
            assertEventFired()
        }
    }
}