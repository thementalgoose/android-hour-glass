package tmg.hourglass.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import tmg.hourglass.base.BaseViewModel
import tmg.hourglass.data.connectors.CountdownConnector
import tmg.utilities.lifecycle.DataEvent
import tmg.utilities.lifecycle.Event

//region Inputs

interface HomeViewModelInputs {
    fun clickAdd()
    fun editItem(id: String)
    fun deleteItem(id: String)
    fun switchList(tab: HomeTab)
}

//endregion

//region Outputs

interface HomeViewModelOutputs {
    val addItemEvent: MutableLiveData<Event>
    val editItemEvent: MutableLiveData<DataEvent<String>>
    val items: LiveData<List<HomeItemType>>
}

//endregion

class HomeViewModel(
    private val countdownConnector: CountdownConnector
) : BaseViewModel(), HomeViewModelInputs, HomeViewModelOutputs {

    var inputs: HomeViewModelInputs = this
    var outputs: HomeViewModelOutputs = this

    private val viewTab: ConflatedBroadcastChannel<HomeTab> = ConflatedBroadcastChannel(HomeTab.NOW)

    override val items: LiveData<List<HomeItemType>> = viewTab
        .asFlow()
        .flatMapLatest {
            when (it) {
                HomeTab.NOW -> countdownConnector.allCurrent()
                HomeTab.PREVIOUS -> countdownConnector.allDone()
            }
        }
        .map { list ->
            mutableListOf<HomeItemType>(HomeItemType.Header)
                .apply {
                    addAll(list.map {
                        val action: HomeItemAction = if ((viewTab.valueOrNull ?: HomeTab.NOW) == HomeTab.NOW) HomeItemAction.EDIT else HomeItemAction.DELETE
                        HomeItemType.Item(countdown = it, action = action)
                    })
                }
        }
        .asLiveData(viewModelScope.coroutineContext)

    override val editItemEvent: MutableLiveData<DataEvent<String>> = MutableLiveData()
    override val addItemEvent: MutableLiveData<Event> = MutableLiveData()

    //region Inputs

    override fun clickAdd() {
        addItemEvent.value = Event()
    }

    override fun editItem(id: String) {
        editItemEvent.value = DataEvent(id)
    }

    override fun deleteItem(id: String) {
        countdownConnector.delete(id)
    }

    override fun switchList(tab: HomeTab) {
        viewTab.offer(tab)
    }

    //endregion
}