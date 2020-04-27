package tmg.hourglass.settings

import androidx.lifecycle.MutableLiveData
import tmg.hourglass.base.BaseViewModel
import tmg.hourglass.data.connectors.CountdownConnector
import tmg.hourglass.prefs.IPrefs
import tmg.utilities.lifecycle.Event

//region Inputs

interface SettingsViewModelInputs {
    fun clickBack()

    fun clickDeleteAll()
    fun clickDeleteDone()

    fun clickAbout()
    fun clickReleaseNotes()
    fun clickCrashReporting()
    fun clickSuggestions()
    fun clickShakeToReport()
    fun clickPrivacyPolicy()
}

//endregion

//region Outputs

interface SettingsViewModelOutputs {
    val goBack: MutableLiveData<Event>

    val deletedAll: MutableLiveData<Event>
    val deletedDone: MutableLiveData<Event>

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
    private val prefs: IPrefs
) : BaseViewModel(), SettingsViewModelInputs, SettingsViewModelOutputs {

    override val goBack: MutableLiveData<Event> = MutableLiveData()
    override val deletedAll: MutableLiveData<Event> = MutableLiveData()
    override val deletedDone: MutableLiveData<Event> = MutableLiveData()
    override val openAbout: MutableLiveData<Event> = MutableLiveData()
    override val openReleaseNotes: MutableLiveData<Event> = MutableLiveData()
    override val crashReporting: MutableLiveData<Pair<Boolean, Boolean>> = MutableLiveData(Pair(false, prefs.crashReporting))
    override val openSuggestions: MutableLiveData<Event> = MutableLiveData()
    override val shakeToReport: MutableLiveData<Pair<Boolean, Boolean>> = MutableLiveData(Pair(false, prefs.shakeToReport))

    override val privacyPolicy: MutableLiveData<Event> = MutableLiveData()

    var inputs: SettingsViewModelInputs = this
    var outputs: SettingsViewModelOutputs = this

    init {

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

    override fun clickAbout() {
        openAbout.value = Event()
    }

    override fun clickReleaseNotes() {
        openReleaseNotes.value = Event()
    }

    override fun clickCrashReporting() {
        prefs.crashReporting = !prefs.crashReporting
        crashReporting.value = Pair(true, prefs.crashReporting)
    }

    override fun clickSuggestions() {
        openSuggestions.value = Event()
    }

    override fun clickShakeToReport() {
        prefs.shakeToReport = !prefs.shakeToReport
        shakeToReport.value = Pair(true, prefs.shakeToReport)
    }

    override fun clickPrivacyPolicy() {
        privacyPolicy.value = Event()
    }

    //endregion
}