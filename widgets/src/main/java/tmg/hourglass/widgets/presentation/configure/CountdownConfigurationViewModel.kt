package tmg.hourglass.widgets.presentation.configure

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import tmg.hourglass.domain.connectors.CountdownConnector
import tmg.hourglass.domain.connectors.WidgetConnector
import tmg.hourglass.domain.model.Countdown
import javax.inject.Inject

data class UiState(
    val items: List<Countdown>,
    val selected: Countdown?,
    val appWidgetId: Int
) {
    constructor(): this(
        items = emptyList(),
        selected = null,
        appWidgetId = -1
    )
}

@HiltViewModel
class CountdownConfigurationViewModel @Inject constructor(
    private val countdownConnector: CountdownConnector,
    private val widgetConnector: WidgetConnector,
): ViewModel() {

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    init {
        refresh()
    }

    fun load(appWidgetId: Int) {
        val countdown = widgetConnector.getCountdownModelSync(appWidgetId)
        _uiState.value = _uiState.value.copy(
            appWidgetId = appWidgetId,
            selected = countdown
        )
    }

    fun select(countdown: Countdown) {
        _uiState.value = _uiState.value.copy(
            selected = countdown
        )
    }

    private fun refresh() {
        viewModelScope.launch {
            val current = countdownConnector.allCurrent().first()
            _uiState.value = _uiState.value.copy(
                items = current,
            )
        }
    }

    fun save() {
        val state = _uiState.value
        Log.i("Widgets", "Saving widget configuration $state")
        if (state.appWidgetId == -1) {
            return
        }
        if (state.selected == null) {
            return
        }

        widgetConnector.saveSync(state.appWidgetId, state.selected.id)
    }
}