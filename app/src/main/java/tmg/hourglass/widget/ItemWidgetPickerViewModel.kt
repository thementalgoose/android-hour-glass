package tmg.hourglass.widget

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import tmg.hourglass.base.BaseViewModel
import tmg.hourglass.data.connectors.CountdownConnector
import tmg.hourglass.data.connectors.WidgetConnector
import tmg.hourglass.home.HomeItemAction
import tmg.hourglass.home.HomeItemType
import tmg.hourglass.home.HomeTab
import tmg.utilities.extensions.combinePair
import tmg.utilities.lifecycle.DataEvent

//region Inputs

interface ItemWidgetPickerViewModelInputs {
    fun supplyAppWidgetId(id: Int)
    fun checkedItem(itemId: String)
    fun clickSave()
}

//endregion

//region Outputs

interface ItemWidgetPickerViewModelOutputs {
    val list: LiveData<List<HomeItemType>>
    val isSavedEnabled: LiveData<Boolean>
    val save: MutableLiveData<DataEvent<String>>
}

//endregion

@Suppress("EXPERIMENTAL_API_USAGE")
class ItemWidgetPickerViewModel(
    private val widgetReferenceConnector: WidgetConnector,
    private val countdownConnector: CountdownConnector
): BaseViewModel(), ItemWidgetPickerViewModelInputs, ItemWidgetPickerViewModelOutputs {

    var inputs: ItemWidgetPickerViewModelInputs = this
    var outputs: ItemWidgetPickerViewModelOutputs = this

    private val appWidgetId: ConflatedBroadcastChannel<Int?> = ConflatedBroadcastChannel(null)
    private val checkedId: ConflatedBroadcastChannel<String?> = ConflatedBroadcastChannel(null)

    override val isSavedEnabled: LiveData<Boolean> = checkedId
        .asFlow()
        .combinePair(appWidgetId.asFlow())
        .map { it.first != null && it.second != null }
        .asLiveData(viewModelScope.coroutineContext)
    override val save: MutableLiveData<DataEvent<String>> = MutableLiveData()

    override val list: LiveData<List<HomeItemType>> = countdownConnector.all()
        .combinePair(checkedId.asFlow())
        .map { (list, checkedId) ->
            mutableListOf<HomeItemType>(HomeItemType.Header)
                .apply {
                    if (list.isEmpty()) {
                        add(HomeItemType.Placeholder)
                    }
                    else {
                        addAll(list.map {
                            HomeItemType.Item(
                                countdown = it,
                                action = HomeItemAction.CHECK,
                                isEnabled = checkedId == it.id,
                                showDescription = false,
                                clickBackground = true,
                                animateBar = false
                            )
                        })
                    }
                }
        }
        .asLiveData(viewModelScope.coroutineContext)

    //region Inputs

    override fun supplyAppWidgetId(id: Int) {
        appWidgetId.offer(id)
    }

    override fun checkedItem(itemId: String) {
        checkedId.offer(itemId)
    }

    override fun clickSave() {
        val aId = appWidgetId.valueOrNull
        val cId = checkedId.valueOrNull
        if (aId != null && cId != null) {
            widgetReferenceConnector.saveSync(aId, cId)
            save.value = DataEvent(cId)
        }
    }

    //endregion
}