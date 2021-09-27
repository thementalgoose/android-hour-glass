package tmg.hourglass.settings.release

import androidx.lifecycle.MutableLiveData
import tmg.hourglass.ReleaseNotes
import tmg.hourglass.base.BaseViewModel
import tmg.utilities.lifecycle.Event

//region Inputs

interface ReleaseViewModelInputs {
    fun clickBack()
}

//endregion

//region Outputs

interface ReleaseViewModelOutputs {
    val content: MutableLiveData<List<ReleaseNotes>>
    val goBack: MutableLiveData<Event>
}

//endregion

class ReleaseViewModel: BaseViewModel(), ReleaseViewModelInputs, ReleaseViewModelOutputs {

    var inputs: ReleaseViewModelInputs = this
    var outputs: ReleaseViewModelOutputs = this

    override val content: MutableLiveData<List<ReleaseNotes>> = MutableLiveData()

    override val goBack: MutableLiveData<Event> = MutableLiveData()

    init {
        content.value = ReleaseNotes.values().toList()
    }

    //region Inputs

    override fun clickBack() {
        goBack.value = Event()
    }

    //endregion
}