package tmg.hourglass.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import tmg.hourglass.analytics.AnalyticsManager
import tmg.hourglass.analytics.FirebaseAnalyticsManager
import tmg.hourglass.base.BaseViewModel
import tmg.hourglass.data.connectors.CountdownConnector
import tmg.hourglass.di.async.ScopeProvider
import tmg.utilities.extensions.then
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
    val deleteItemEvent: MutableLiveData<Event>
}

//endregion

@Suppress("EXPERIMENTAL_API_USAGE")
class HomeViewModel(
    private val countdownConnector: CountdownConnector,
    scopeProvider: ScopeProvider
) : BaseViewModel(scopeProvider), HomeViewModelInputs, HomeViewModelOutputs {

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
                    if (list.isEmpty()) {
                        add(HomeItemType.Placeholder)
                    }
                    else {
                        addAll(list.map {
                            val action: HomeItemAction = if ((viewTab.valueOrNull
                                    ?: HomeTab.NOW) == HomeTab.NOW
                            ) HomeItemAction.EDIT else HomeItemAction.DELETE
                            HomeItemType.Item(countdown = it, action = action, isEnabled = false)
                        })
                    }
                }
        }
        .asLiveData(scope.coroutineContext)

    override val editItemEvent: MutableLiveData<DataEvent<String>> = MutableLiveData()
    override val addItemEvent: MutableLiveData<Event> = MutableLiveData()
    override val deleteItemEvent: MutableLiveData<Event> = MutableLiveData()

    //region Inputs

    override fun clickAdd() {
        addItemEvent.value = Event()
    }

    override fun editItem(id: String) {
        editItemEvent.value = DataEvent(id)
    }

    override fun deleteItem(id: String) {
        countdownConnector.delete(id)
        deleteItemEvent.value = Event()
    }

    override fun switchList(tab: HomeTab) {
        viewTab.offer(tab)
    }

    //endregion
}