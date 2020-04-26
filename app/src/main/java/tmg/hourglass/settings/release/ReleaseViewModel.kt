package tmg.hourglass.settings.release

import androidx.lifecycle.MutableLiveData
import tmg.hourglass.base.BaseViewModel
import tmg.hourglass.prefs.IPrefs
import tmg.hourglass.releaseNotes

//region Inputs

interface ReleaseViewModelInputs {

}

//endregion

//region Outputs

interface ReleaseViewModelOutputs {
    val content: MutableLiveData<List<Int>>
}

//endregion

class ReleaseViewModel(
    val prefs: IPrefs
): BaseViewModel(), ReleaseViewModelInputs, ReleaseViewModelOutputs {

    var inputs: ReleaseViewModelInputs = this
    var outputs: ReleaseViewModelOutputs = this

    override val content: MutableLiveData<List<Int>> = MutableLiveData()

    init {
        content.value = releaseNotes.values.toList()
    }

    //region Inputs

    //endregion
}