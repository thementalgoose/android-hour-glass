package tmg.hourglass.settings.release

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import tmg.hourglass.ReleaseNotes
import tmg.utilities.lifecycle.Event
import javax.inject.Inject

//region Inputs

interface ReleaseViewModelInputs {
    fun clickBack()
}

//endregion

//region Outputs

interface ReleaseViewModelOutputs {
    val content: LiveData<List<ReleaseNotes>>
    val goBack: LiveData<Event>
}

//endregion

@HiltViewModel
class ReleaseViewModel @Inject constructor(): ViewModel(), ReleaseViewModelInputs, ReleaseViewModelOutputs {

    var inputs: ReleaseViewModelInputs = this
    var outputs: ReleaseViewModelOutputs = this

    override val content: MutableLiveData<List<ReleaseNotes>> = MutableLiveData()

    override val goBack: MutableLiveData<Event> = MutableLiveData()

    init {
        content.value = ReleaseNotes
            .values()
            .reversed()
            .toList()
    }

    //region Inputs

    override fun clickBack() {
        goBack.value = Event()
    }

    //endregion
}