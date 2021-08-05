package tmg.hourglass.ui.home

import androidx.compose.runtime.MutableState
import androidx.lifecycle.*
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*
import tmg.hourglass.domain.data.connectors.CountdownConnector
import tmg.hourglass.home.HomeItemAction
import tmg.hourglass.home.HomeItemType
import tmg.hourglass.home.HomeTab
import tmg.hourglass.ui.base.BaseViewModel
import tmg.hourglass.ui.home.views.Tab
import tmg.utilities.lifecycle.DataEvent
import tmg.utilities.lifecycle.Event

//region Inputs

interface HomeViewModelInputs {
    fun clickAdd()
    fun editItem(id: String)
    fun deleteItem(id: String)
    fun switchList(tab: Tab)
}

//endregion

//region Outputs

interface HomeViewModelOutputs {
    val items: LiveData<List<HomeItemType.Item>>
    val currentTab: LiveData<Tab>
}

//endregion

@Suppress("EXPERIMENTAL_API_USAGE")
class HomeViewModel(
    private val countdownConnector: CountdownConnector
) : BaseViewModel(), HomeViewModelInputs, HomeViewModelOutputs {

    var inputs: HomeViewModelInputs = this
    var outputs: HomeViewModelOutputs = this

    private val viewTab: MutableStateFlow<Tab> = MutableStateFlow(Tab.NOW)
    override val currentTab: LiveData<Tab> = viewTab.asLiveData()

    override val items: LiveData<List<HomeItemType.Item>> = viewTab
        .asStateFlow()
        .flatMapLatest {
            when (it) {
                Tab.NOW -> countdownConnector.allCurrent()
                Tab.PREVIOUS -> countdownConnector.allDone()
            }
        }
        .map { list ->
            list.map {
                val action: HomeItemAction = when (viewTab.value) {
                    Tab.NOW -> HomeItemAction.EDIT
                    Tab.PREVIOUS -> HomeItemAction.DELETE
                }
                HomeItemType.Item(countdown = it, action = action, isEnabled = false)
            }
        }
        .asLiveData(viewModelScope.coroutineContext)

//    override val editItemEvent: MutableLiveData<DataEvent<String>> = MutableLiveData()
//    override val addItemEvent: MutableLiveData<Event> = MutableLiveData()
//    override val deleteItemEvent: MutableLiveData<Event> = MutableLiveData()

    //region Inputs

    override fun clickAdd() {
//        addItemEvent.value = Event()
    }

    override fun editItem(id: String) {
//        editItemEvent.value = DataEvent(id)
    }

    override fun deleteItem(id: String) {
        countdownConnector.delete(id)
//        deleteItemEvent.value = Event()
    }

    override fun switchList(tab: Tab) {
        viewTab.tryEmit(tab)
    }

    //endregion
}