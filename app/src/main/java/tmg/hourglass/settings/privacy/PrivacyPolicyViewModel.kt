package tmg.hourglass.settings.privacy

import androidx.lifecycle.MutableLiveData
import tmg.hourglass.base.BaseViewModel
import tmg.hourglass.prefs.IPrefs
import tmg.hourglass.releaseNotes
import tmg.utilities.lifecycle.Event

//region Inputs

interface PrivacyPolicyViewModelInputs {
    fun clickBack()
}

//endregion

//region Outputs

interface PrivacyPolicyViewModelOutputs {
    val goBack: MutableLiveData<Event>
}

//endregion

class PrivacyPolicyViewModel(
    val prefs: IPrefs
): BaseViewModel(), PrivacyPolicyViewModelInputs, PrivacyPolicyViewModelOutputs {

    var inputs: PrivacyPolicyViewModelInputs = this
    var outputs: PrivacyPolicyViewModelOutputs = this

    override val goBack: MutableLiveData<Event> = MutableLiveData()

    //region Inputs

    override fun clickBack() {
        goBack.value = Event()
    }

    //endregion
}