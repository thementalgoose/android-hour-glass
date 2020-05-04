package tmg.hourglass.settings

import android.content.Context
import androidx.lifecycle.MutableLiveData
import tmg.components.prefs.*
import tmg.hourglass.R
import tmg.hourglass.base.BaseViewModel
import tmg.hourglass.data.connectors.CountdownConnector
import tmg.hourglass.prefs.PreferencesManager
import tmg.hourglass.prefs.ThemePref
import tmg.utilities.lifecycle.Event

//region Inputs

interface SettingsViewModelInputs {
    fun clickBack()

    fun clickDeleteAll()
    fun clickDeleteDone()

    fun clickTheme(themePref: ThemePref)

    fun clickAbout()
    fun clickReleaseNotes()
    fun clickCrashReporting(type: Boolean)
    fun clickSuggestions()
    fun clickShakeToReport(type: Boolean)
    fun clickPrivacyPolicy()
}

//endregion

//region Outputs

interface SettingsViewModelOutputs {
    val list: MutableLiveData<List<AppPreferencesItem>>
    val goBack: MutableLiveData<Event>

    val deletedAll: MutableLiveData<Event>
    val deletedDone: MutableLiveData<Event>

    val themeUpdated: MutableLiveData<Event>
    val themeSelected: MutableLiveData<ThemePref>

    val openAbout: MutableLiveData<Event>
    val openReleaseNotes: MutableLiveData<Event>
    val crashReporting: MutableLiveData<Pair<Boolean, Boolean>> // Show update, updated too
    val openSuggestions: MutableLiveData<Event>
    val shakeToReport: MutableLiveData<Pair<Boolean, Boolean>> // Show update, updated too
    val privacyPolicy: MutableLiveData<Event>
}

//endregion

class SettingsViewModel(
    private val countdownConnector: CountdownConnector,
    private val prefs: PreferencesManager,
    private val context: Context
) : BaseViewModel(), SettingsViewModelInputs, SettingsViewModelOutputs {

    override val goBack: MutableLiveData<Event> = MutableLiveData()

    override val deletedAll: MutableLiveData<Event> = MutableLiveData()
    override val deletedDone: MutableLiveData<Event> = MutableLiveData()

    override val themeUpdated: MutableLiveData<Event> = MutableLiveData()
    override val themeSelected: MutableLiveData<ThemePref> = MutableLiveData(prefs.theme)

    override val openAbout: MutableLiveData<Event> = MutableLiveData()
    override val openReleaseNotes: MutableLiveData<Event> = MutableLiveData()
    override val crashReporting: MutableLiveData<Pair<Boolean, Boolean>> = MutableLiveData(Pair(false, prefs.crashReporting))
    override val openSuggestions: MutableLiveData<Event> = MutableLiveData()
    override val shakeToReport: MutableLiveData<Pair<Boolean, Boolean>> = MutableLiveData(Pair(false, prefs.shakeToReport))

    override val privacyPolicy: MutableLiveData<Event> = MutableLiveData()

    var inputs: SettingsViewModelInputs = this
    var outputs: SettingsViewModelOutputs = this

    override val list: MutableLiveData<List<AppPreferencesItem>> = MutableLiveData()

    init {
        list.value = prefsList(context) {
            category(R.string.settings_theme) {
                preference(PrefType.THEME_APP.key,
                    title = R.string.settings_theme_theme_title,
                    description = R.string.settings_theme_theme_description
                )
            }
            category(R.string.settings_reset) {
                preference(PrefType.DELETE_ALL.key,
                    title = R.string.settings_reset_all_title,
                    description = R.string.settings_reset_all_description
                )
                preference(PrefType.DELETE_DONE.key,
                    title = R.string.settings_reset_done_title,
                    description = R.string.settings_reset_done_description
                )
            }
            category(R.string.settings_help) {
                preference(PrefType.HELP_ABOUT.key,
                    title = R.string.settings_help_about_title,
                    description = R.string.settings_help_about_description
                )
                preference(PrefType.HELP_RELEASE.key,
                    title = R.string.settings_help_release_notes_title,
                    description = R.string.settings_help_release_notes_description
                )
            }
            category(R.string.settings_feedback) {
                preference(PrefType.FEEDBACK_SUGGESTION.key,
                    title = R.string.settings_help_suggestions_title,
                    description = R.string.settings_help_suggestions_description
                )
                switch(PrefType.FEEDBACK_CRASH_REPORTING.key,
                    title = R.string.settings_help_crash_reporting_title,
                    description = R.string.settings_help_crash_reporting_description,
                    isChecked = prefs.crashReporting
                )
                switch(PrefType.FEEDBACK_SHAKE.key,
                    title = R.string.settings_help_shake_to_report_title,
                    description = R.string.settings_help_shake_to_report_description,
                    isChecked = prefs.shakeToReport
                )
            }
            category(R.string.settings_privacy) {
                preference(PrefType.PRIVACY_PRIVACY.key,
                    title = R.string.settings_help_privacy_policy_title,
                    description = R.string.settings_help_privacy_policy_description
                )
            }
        }
    }

    //region Inputs

    override fun clickBack() {
        goBack.value = Event()
    }

    override fun clickDeleteAll() {
        countdownConnector.deleteAll()
        deletedAll.value = Event()
    }

    override fun clickDeleteDone() {
        countdownConnector.deleteDone()
        deletedDone.value = Event()
    }

    override fun clickTheme(themePref: ThemePref) {
        themeSelected.value = themePref
        themeUpdated.value = Event()
        prefs.theme = themePref
    }

    override fun clickAbout() {
        openAbout.value = Event()
    }

    override fun clickReleaseNotes() {
        openReleaseNotes.value = Event()
    }

    override fun clickCrashReporting(type: Boolean) {
        prefs.crashReporting = type
        crashReporting.value = Pair(true, prefs.crashReporting)
    }

    override fun clickSuggestions() {
        openSuggestions.value = Event()
    }

    override fun clickShakeToReport(type: Boolean) {
        prefs.shakeToReport = type
        shakeToReport.value = Pair(true, prefs.shakeToReport)
    }

    override fun clickPrivacyPolicy() {
        privacyPolicy.value = Event()
    }

    //endregion

    enum class PrefType(
        val key: String
    ) {
        THEME_APP("theme_app"),
        DELETE_ALL("delete_all"),
        DELETE_DONE("delete_done"),
        HELP_ABOUT("help_about"),
        HELP_RELEASE("help_release"),
        FEEDBACK_CRASH_REPORTING("feedback_crash_reporting"),
        FEEDBACK_SUGGESTION("feedback_suggestions"),
        FEEDBACK_SHAKE("feedback_shake"),
        PRIVACY_PRIVACY("privacy_privacy")
    }
}