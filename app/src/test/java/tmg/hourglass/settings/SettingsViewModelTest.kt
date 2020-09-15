package tmg.hourglass.settings

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.components.prefs.AppPreferencesItem
import tmg.hourglass.R
import tmg.hourglass.data.connectors.CountdownConnector
import tmg.hourglass.prefs.PreferencesManager
import tmg.hourglass.prefs.ThemePref.DARK
import tmg.hourglass.prefs.ThemePref.LIGHT
import tmg.hourglass.testutils.BaseTest
import tmg.hourglass.testutils.assertEventFired
import tmg.hourglass.testutils.assertValue

@FlowPreview
@ExperimentalCoroutinesApi
class SettingsViewModelTest: BaseTest() {

    lateinit var sut: SettingsViewModel

    private val mockCountdownConnector: CountdownConnector = mock()
    private val mockPreferenceManager: PreferencesManager = mock()

    private val keyTheme: String = "theme_app"
    private val keyKeepInNow: String = "keep_in_now"
    private val keyDeleteAll: String = "delete_all"
    private val keyDeleteDone: String = "delete_done"
    private val keyWidgetsRefresh: String = "widget_refresh"
    private val keyWidgetsUpdated: String = "widget_updated"
    private val keyHelpAbout: String = "help_about"
    private val keyHelpRelease: String = "help_release"
    private val keyFeedbackCrash: String = "feedback_crash_reporting"
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

    @BeforeEach
    internal fun setUp() {


    }

    private fun initSUT() {
        sut = SettingsViewModel(mockCountdownConnector, mockPreferenceManager, testScopeProvider)
    }

    @Test
    fun `SettingsViewModel initial list of settings is emitted with all set to false`() {

        initSUT()

        assertValue(expectedList, sut.outputs.list)
    }

    @Test
    fun `SettingsViewModel theme preference is set to the value set in shared prefs`() {

        whenever(mockPreferenceManager.theme).thenReturn(DARK)

        initSUT()

        assertValue(DARK, sut.outputs.themeSelected)
    }

    @Test
    fun `SettingsViewModel clicking back event emits back behavior`() {

        initSUT()

        sut.inputs.clickBack()

        assertEventFired(sut.outputs.goBack)
    }

    @Test
    fun `SettingsViewModel delete all emits deletion event and calls delete all in connector`() {

        initSUT()

        sut.inputs.clickDeleteAll()

        assertEventFired(sut.outputs.deletedAll)
        verify(mockCountdownConnector).deleteAll()
    }

    @Test
    fun `SettingsViewModel delete done emits deletion event and calls delete done in connector`() {

        initSUT()

        sut.inputs.clickDeleteDone()

        assertEventFired(sut.outputs.deletedDone)
        verify(mockCountdownConnector).deleteDone()
    }

    @Test
    fun `SettingsViewModel clicking a theme triggeres theme updates`() {

        whenever(mockPreferenceManager.theme).thenReturn(DARK)

        initSUT()

        sut.inputs.clickTheme(LIGHT)

        assertValue(LIGHT, sut.outputs.themeSelected)
        assertEventFired(sut.outputs.themeUpdated)
        verify(mockPreferenceManager).theme = LIGHT
    }

    @Test
    fun `SettingsViewModel clicking widget update interacts with pref`() {

        whenever(mockPreferenceManager.widgetShowUpdate).thenReturn(false)

        initSUT()

        sut.inputs.clickWidgetUpdate(true)

        verify(mockPreferenceManager).widgetShowUpdate = true
    }

    @Test
    fun `SettingsViewModel clicking about fires about event`() {

        initSUT()

        sut.inputs.clickAbout()

        assertEventFired(sut.outputs.openAbout)
    }

    @Test
    fun `SettingsViewModel clicking release notes fires release event`() {

        initSUT()

        sut.inputs.clickReleaseNotes()

        assertEventFired(sut.outputs.openReleaseNotes)
    }

    @Test
    fun `SettingsViewModel crash reporting initial value has show update to be false, clicking it shows crash reportings banner`() {

        whenever(mockPreferenceManager.crashReporting).thenReturn(false)

        initSUT()

        assertValue(Pair(false, second = false), sut.outputs.crashReporting)

        whenever(mockPreferenceManager.crashReporting).thenReturn(true)
        sut.inputs.clickCrashReporting(true)
        verify(mockPreferenceManager).crashReporting = true

        assertValue(Pair(true, second = true), sut.outputs.crashReporting)
    }

    @Test
    fun `SettingsViewModel click suggestions fires open suggestions event`() {

        initSUT()

        sut.inputs.clickSuggestions()

        assertEventFired(sut.outputs.openSuggestions)
    }

    @Test
    fun `SettingsViewModel shake to report initial value has show update to be false, clicking it shows shake to report banner`() {

        whenever(mockPreferenceManager.shakeToReport).thenReturn(false)

        initSUT()

        assertValue(Pair(false, second = false), sut.outputs.shakeToReport)

        whenever(mockPreferenceManager.shakeToReport).thenReturn(true)
        sut.inputs.clickShakeToReport(true)
        verify(mockPreferenceManager).shakeToReport = true

        assertValue(Pair(true, second = true), sut.outputs.shakeToReport)
    }

    @Test
    fun `SettingsViewModel click privacy policy fires open privacy policy event`() {

        initSUT()

        sut.inputs.clickPrivacyPolicy()

        assertEventFired(sut.outputs.privacyPolicy)
    }


    @AfterEach
    internal fun tearDown() {


    }
}