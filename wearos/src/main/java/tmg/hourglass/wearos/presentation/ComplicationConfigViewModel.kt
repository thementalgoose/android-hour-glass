package tmg.hourglass.wearos.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import tmg.hourglass.domain.model.Countdown
import tmg.hourglass.wearos.data.WearosCountdownRepository
import javax.inject.Inject

@HiltViewModel
class ComplicationConfigViewModel @Inject constructor(
    private val repository: WearosCountdownRepository
) : ViewModel() {

    private val _countdowns = MutableStateFlow<List<Countdown>>(emptyList())
    val countdowns: StateFlow<List<Countdown>> = _countdowns.asStateFlow()

    init {
        loadCountdowns()
    }

    private fun loadCountdowns() {
        _countdowns.value = repository.getCountdowns()
        viewModelScope.launch {
            val fetched = repository.fetchCountdownsFromDataClient()
            if (fetched.isNotEmpty()) {
                _countdowns.value = fetched
            }
        }
    }

    fun saveComplicationBinding(complicationId: Int, countdownId: String) {
        repository.saveComplicationBinding(complicationId, countdownId)
    }
}
