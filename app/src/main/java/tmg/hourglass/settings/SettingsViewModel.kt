package tmg.hourglass.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import tmg.hourglass.BuildConfig
import tmg.hourglass.R
import tmg.hourglass.base.BaseViewModel
import tmg.hourglass.domain.connectors.CountdownConnector
import tmg.hourglass.prefs.PreferencesManager
import tmg.hourglass.prefs.ThemePref
import tmg.utilities.lifecycle.DataEvent
import tmg.utilities.lifecycle.Event

//region Inputs

interface SettingsViewModelInputs {

    fun clickBack()

    fun clickSetting(model: SettingsModel)

    fun clickTheme(themePref: ThemePref)

    fun clickDeleteAll()
    fun clickDeleteDone()
}

//endregion

//region Outputs

interface SettingsViewModelOutputs {
    val list: LiveData<List<SettingsModel>>
    val goBack: LiveData<Event>

    val deletedAll: LiveData<Event>
    val deletedDone: LiveData<Event>

    val updateWidget: LiveData<Event>

    val currentThemePref: LiveData<ThemePref>
    val openTheme: LiveData<Event>

    val openAbout: LiveData<Event>
    val openReview: LiveData<DataEvent<String>>
    val openReleaseNotes: LiveData<Event>
    val crashReporting: LiveData<Pair<Boolean, Boolean>> // Show update, updated too
    val analyticsReporting: LiveData<Pair<Boolean, Boolean>> // Show update, updated too
    val openSuggestions: LiveData<Event>
    val shakeToReport: LiveData<Pair<Boolean, Boolean>> // Show update, updated too
    val privacyPolicy: LiveData<Event>
}

//endregion

class SettingsViewModel(
    private val countdownConnector: CountdownConnector,
    private val prefs: PreferencesManager
) : BaseViewModel(), SettingsViewModelInputs, SettingsViewModelOutputs {

    override val goBack: MutableLiveData<Event> = MutableLiveData()

    override val deletedAll: MutableLiveData<Event> = MutableLiveData()
    override val deletedDone: MutableLiveData<Event> = MutableLiveData()

    override val updateWidget: MutableLiveData<Event> = MutableLiveData()

    override val currentThemePref: MutableLiveData<ThemePref> = MutableLiveData()
    override val openTheme: MutableLiveData<Event> = MutableLiveData()

    override val openAbout: MutableLiveData<Event> = MutableLiveData()
    override val openReview: MutableLiveData<DataEvent<String>> = MutableLiveData()
    override val openReleaseNotes: MutableLiveData<Event> = MutableLiveData()
    override val crashReporting: MutableLiveData<Pair<Boolean, Boolean>> = MutableLiveData()
    override val analyticsReporting: MutableLiveData<Pair<Boolean, Boolean>> = MutableLiveData()
    override val openSuggestions: MutableLiveData<Event> = MutableLiveData()
    override val shakeToReport: MutableLiveData<Pair<Boolean, Boolean>> = MutableLiveData()

    override val privacyPolicy: MutableLiveData<Event> = MutableLiveData()

    var inputs: SettingsViewModelInputs = this
    var outputs: SettingsViewModelOutputs = this

    override val list: MutableLiveData<List<SettingsModel>> = MutableLiveData(getModels())

    var modelList: List<SettingsModel> = getModels()
    private fun getModels() = mutableListOf<SettingsModel>().apply {
        add(SettingsModel.Header(R.string.settings_theme))
        add(SettingsModel.Pref(
            title = R.string.settings_theme_theme_title,
            description = R.string.settings_theme_theme_description,
            onClick = {
                openTheme.value = Event()
            }
        ))
        add(SettingsModel.Header(R.string.settings_widgets))
        add(SettingsModel.Pref(
            title = R.string.settings_widgets_refresh_title,
            description = R.string.settings_widgets_refresh_description,
            onClick = {
                updateWidget.value = Event()
            }
        ))
        add(SettingsModel.SwitchPref(
            title = R.string.settings_widgets_updated_title,
            description = R.string.settings_widgets_updated_description,
            getState = { prefs.widgetShowUpdate },
            saveState = { value ->
                prefs.widgetShowUpdate = value
                updateWidget.value = Event()
            }
        ))
        add(SettingsModel.Header(R.string.settings_reset))
        add(SettingsModel.Pref(
            title = R.string.settings_reset_all_title,
            description = R.string.settings_reset_all_description,
            onClick = {
                deletedAll.value = Event()
            }
        ))
        add(SettingsModel.Pref(
            title = R.string.settings_reset_done_title,
            description = R.string.settings_reset_done_description,
            onClick = {
                deletedDone.value = Event()
            }
        ))
        add(SettingsModel.Header(R.string.settings_help))
        add(SettingsModel.Pref(
            title = R.string.settings_help_about_title,
            description = R.string.settings_help_about_description,
            onClick = {
                openAbout.value = Event()
            }
        ))
        add(SettingsModel.Pref(
            title = R.string.settings_help_review_title,
            description = R.string.settings_help_review_description,
            onClick = {
                openReview.value = DataEvent("https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID)
            }
        ))
        add(SettingsModel.Pref(
            title = R.string.settings_help_release_notes_title,
            description = R.string.settings_help_release_notes_description,
            onClick = {
                openReleaseNotes.value = Event()
            }
        ))
        add(SettingsModel.Header(R.string.settings_feedback))
        add(SettingsModel.Pref(
            title = R.string.settings_help_suggestions_title,
            description = R.string.settings_help_suggestions_description,
            onClick = {
                openSuggestions.value = Event()
            }
        ))
        add(SettingsModel.SwitchPref(
            title = R.string.settings_help_crash_reporting_title,
            description = R.string.settings_help_crash_reporting_description,
            getState = { prefs.crashReporting },
            saveState = { value ->
                prefs.crashReporting = value
                crashReporting.value = Pair(true, prefs.crashReporting)
            }
        ))
        add(SettingsModel.SwitchPref(
            title = R.string.settings_help_analytics_title,
            description = R.string.settings_help_analytics_description,
            getState = { prefs.analyticsEnabled },
            saveState = { value ->
                prefs.analyticsEnabled = value
                analyticsReporting.value = Pair(true, prefs.analyticsEnabled)
            }
        ))
        add(SettingsModel.SwitchPref(
            title = R.string.settings_help_shake_to_report_title,
            description = R.string.settings_help_shake_to_report_description,
            getState = { prefs.shakeToReport },
            saveState = { value ->
                prefs.shakeToReport = value
                shakeToReport.value = Pair(true, prefs.shakeToReport)
            }
        ))
        add(SettingsModel.Header(R.string.settings_privacy))
        add(SettingsModel.Pref(
            title = R.string.settings_help_privacy_policy_title,
            description = R.string.settings_help_privacy_policy_description,
            onClick = {
                privacyPolicy.value = Event()
            }
        ))
    }

    init {
        currentThemePref.value = prefs.theme
    }

    //region Inputs

    override fun clickSetting(model: SettingsModel) {
        val shouldRefresh = when (model) {
            is SettingsModel.Pref -> {
                model.onClick?.invoke()
                true
            }
            is SettingsModel.SwitchPref -> {
                val currentState = model.getState.invoke()
                model.saveState(!currentState)
                true
            }
            else -> false
        }

        if (shouldRefresh) {
            list.postValue(getModels())
        }
    }

    override fun clickBack() {
        goBack.value = Event()
    }

    override fun clickTheme(themePref: ThemePref) {
        prefs.theme = themePref
        currentThemePref.value = themePref
    }

    override fun clickDeleteAll() {
        countdownConnector.deleteAll()
    }

    override fun clickDeleteDone() {
        countdownConnector.deleteDone()
    }
}