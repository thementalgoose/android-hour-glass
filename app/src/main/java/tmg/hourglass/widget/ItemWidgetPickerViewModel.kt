
package tmg.hourglass.widget

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import tmg.hourglass.domain.connectors.CountdownConnector
import tmg.hourglass.domain.connectors.WidgetConnector
import tmg.utilities.extensions.combinePair
import javax.inject.Inject

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

@Suppress("EXPERIMENTAL_API_USAGE")
@HiltViewModel
class ItemWidgetPickerViewModel @Inject constructor(
    private val widgetReferenceConnector: WidgetConnector,
    countdownConnector: CountdownConnector,
): ViewModel(), ItemWidgetPickerViewModelInputs, ItemWidgetPickerViewModelOutputs {

    var inputs: ItemWidgetPickerViewModelInputs = this
    var outputs: ItemWidgetPickerViewModelOutputs = this

    private val appWidgetId: MutableStateFlow<Int?> = MutableStateFlow(null)
    private val checkedId: MutableStateFlow<String?> = MutableStateFlow(null)

    override val isSavedEnabled: LiveData<Boolean> = checkedId
        .combinePair(appWidgetId)
        .map { it.first != null && it.second != null }
        .asLiveData(viewModelScope.coroutineContext)

    override val list: LiveData<List<ItemWidgetPickerItem>> = countdownConnector
        .all()
        .combinePair(checkedId)
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
        appWidgetId.value = id
    }

    override fun checkedItem(itemId: String) {
        checkedId.value = itemId
    }

    override fun clickSave() {
        val aId = appWidgetId.value
        val cId = checkedId.value
        if (aId != null && cId != null) {
            widgetReferenceConnector.saveSync(aId, cId)
        }
    }

    //endregion
}