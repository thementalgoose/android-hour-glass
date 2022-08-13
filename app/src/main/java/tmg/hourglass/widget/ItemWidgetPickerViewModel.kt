
package tmg.hourglass.widget

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import tmg.hourglass.base.BaseViewModel
import tmg.hourglass.domain.connectors.CountdownConnector
import tmg.hourglass.domain.connectors.WidgetConnector
import tmg.utilities.extensions.combinePair

//region Inputs

interface ItemWidgetPickerViewModelInputs {
    fun supplyAppWidgetId(id: Int)
    fun checkedItem(itemId: String)
    fun clickSave()
}

//endregion

//region Outputs

interface ItemWidgetPickerViewModelOutputs {
    val list: LiveData<List<ItemWidgetPickerItem>>
    val isSavedEnabled: LiveData<Boolean>
}

//endregion

@OptIn(ExperimentalCoroutinesApi::class, ObsoleteCoroutinesApi::class)
@Suppress("EXPERIMENTAL_API_USAGE")
class ItemWidgetPickerViewModel(
    private val widgetReferenceConnector: WidgetConnector,
    countdownConnector: CountdownConnector,
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

    override val list: LiveData<List<ItemWidgetPickerItem>> = countdownConnector
        .all()
        .combinePair(checkedId.asFlow())
        .map { (list, checkedId) ->
            mutableListOf<ItemWidgetPickerItem>()
                .apply {
                    if (list.isEmpty()) {
                        add(ItemWidgetPickerItem.Placeholder)
                    }
                    else {
                        addAll(list.map {
                            ItemWidgetPickerItem.Item(
                                countdown = it,
                                isEnabled = checkedId == it.id,
                            )
                        })
                    }
                }
        }
        .asLiveData(viewModelScope.coroutineContext)

    //region Inputs

    override fun supplyAppWidgetId(id: Int) {
        appWidgetId.trySend(id)
    }

    override fun checkedItem(itemId: String) {
        checkedId.trySend(itemId)
    }

    override fun clickSave() {
        val aId = appWidgetId.valueOrNull
        val cId = checkedId.valueOrNull
        if (aId != null && cId != null) {
            widgetReferenceConnector.saveSync(aId, cId)
        }
    }

    //endregion
}