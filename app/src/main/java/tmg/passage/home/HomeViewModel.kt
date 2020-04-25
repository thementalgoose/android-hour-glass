package tmg.passage.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.threeten.bp.LocalDateTime
import tmg.passage.base.BaseViewModel
import tmg.passage.data.PassageType
import tmg.passage.data.models.Passage
import tmg.utilities.lifecycle.Event
import java.util.*

//region Inputs

interface HomeViewModelInputs {
    fun clickAdd()
}

//endregion

//region Outputs

interface HomeViewModelOutputs {
    val addItemEvent: MutableLiveData<Event>
    val items: LiveData<List<HomeItemType>>
}

//endregion

class HomeViewModel : BaseViewModel(), HomeViewModelInputs, HomeViewModelOutputs {

    var inputs: HomeViewModelInputs = this
    var outputs: HomeViewModelOutputs = this

    val listItems = MutableLiveData<List<HomeItemType>>()
    override val items: LiveData<List<HomeItemType>> = listItems

    override val addItemEvent: MutableLiveData<Event> = MutableLiveData<Event>()

    init {
        listItems.value = List(30) {
            HomeItemType.Item(
                passage = Passage(
                    id = UUID.randomUUID().toString(),
                    name = "Mileage",
                    description = "Testing description",
                    colour = "#123456",
                    start = LocalDateTime.MIN,
                    end = LocalDateTime.MAX,
                    initial = "16000",
                    final = "40000",
                    passageType = PassageType.NUMBER
                )
            )
        }
    }

    //region Inputs

    override fun clickAdd() {
        addItemEvent.value = Event()
    }

    //endregion
}