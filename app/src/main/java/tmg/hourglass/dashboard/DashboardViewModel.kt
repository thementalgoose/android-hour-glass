package tmg.hourglass.dashboard

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import tmg.hourglass.domain.connectors.CountdownConnector
import tmg.hourglass.domain.model.Countdown
import tmg.utilities.lifecycle.DataEvent
import tmg.utilities.lifecycle.Event
import javax.inject.Inject

//region Inputs

interface DashboardViewModelInputs {
    fun clickSettings()
    fun clickAdd()

    fun clickEdit(id: String)
    fun clickDelete(id: String)
}

//endregion

//region Outputs

interface DashboardViewModelOutputs {
    val goToSettings: LiveData<Event>
    val goToAdd: LiveData<Event>
    val goToEdit: LiveData<DataEvent<String>>

    val upcoming: LiveData<List<Countdown>>
}

//endregion

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val countdownConnector: CountdownConnector
): ViewModel(), DashboardViewModelInputs, DashboardViewModelOutputs {

    val inputs: DashboardViewModelInputs = this
    val outputs: DashboardViewModelOutputs = this

    override val goToSettings: MutableLiveData<Event> = MutableLiveData()
    override val goToAdd: MutableLiveData<Event> = MutableLiveData()
    override val goToEdit: MutableLiveData<DataEvent<String>> = MutableLiveData()

    override val upcoming: LiveData<List<Countdown>> = countdownConnector
        .all()
        .asLiveData(viewModelScope.coroutineContext)


    override fun clickSettings() {
        goToSettings.value = Event()
    }

    override fun clickAdd() {
        goToAdd.value = Event()
    }

    override fun clickEdit(id: String) {
        goToEdit.value = DataEvent(id)
    }

    override fun clickDelete(id: String) {
        countdownConnector.delete(id)
    }
}