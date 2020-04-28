package tmg.hourglass.settings.release

import androidx.lifecycle.MutableLiveData
import tmg.hourglass.base.BaseViewModel
import tmg.hourglass.prefs.PreferencesManager
import tmg.hourglass.releaseNotes
import tmg.utilities.lifecycle.Event

//region Inputs

interface ReleaseViewModelInputs {
    fun clickBack()
}

//endregion

//region Outputs

interface ReleaseViewModelOutputs {
    val content: MutableLiveData<List<Int>>
    val goBack: MutableLiveData<Event>
}

//endregion

class ReleaseViewModel(
    val prefs: PreferencesManager
): BaseViewModel(), ReleaseViewModelInputs, ReleaseViewModelOutputs {

    var inputs: ReleaseViewModelInputs = this
    var outputs: ReleaseViewModelOutputs = this

    override val content: MutableLiveData<List<Int>> = MutableLiveData()

    override val goBack: MutableLiveData<Event> = MutableLiveData()

    init {
        content.value = releaseNotes.values.toList()
    }

    //region Inputs

    override fun clickBack() {
        goBack.value = Event()
    }

    //endregion
}