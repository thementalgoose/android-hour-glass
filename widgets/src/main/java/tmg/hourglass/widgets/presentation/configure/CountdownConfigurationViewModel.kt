package tmg.hourglass.widgets.presentation.configure

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import tmg.hourglass.domain.repositories.CountdownRepository
import tmg.hourglass.domain.repositories.WidgetRepository
import tmg.hourglass.domain.model.Countdown
import tmg.hourglass.domain.model.WidgetReference
import javax.inject.Inject

data class UiState(
    val items: List<Countdown>,
    val selected: Countdown?,
    val openAppOnClick: Boolean,
    val appWidgetId: Int
) {
    constructor(): this(
        items = emptyList(),
        selected = null,
        openAppOnClick = false,
        appWidgetId = -1
    )
}

@HiltViewModel
class CountdownConfigurationViewModel @Inject constructor(
    private val countdownRepository: CountdownRepository,
    private val widgetRepository: WidgetRepository,
): ViewModel() {

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    init {
        refresh()
    }

    fun load(appWidgetId: Int) {
        val widgetReference = widgetRepository.getSync(appWidgetId) ?: return
        val countdown = countdownRepository.getSync(widgetReference.countdownId)
        _uiState.value = _uiState.value.copy(
            appWidgetId = appWidgetId,
            selected = countdown,
            openAppOnClick = widgetReference.openAppOnClick
        )
    }

    fun openAppOnClick(openAppOnClick: Boolean) {
        _uiState.value = _uiState.value.copy(
            openAppOnClick = openAppOnClick
        )
    }

    fun select(countdown: Countdown) {
        _uiState.value = _uiState.value.copy(
            selected = countdown
        )
    }

    private fun refresh() {
        viewModelScope.launch {
            val current = countdownRepository.allCurrent().first()
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
        val ref = WidgetReference(
            appWidgetId = state.appWidgetId,
            countdownId = state.selected.id,
            openAppOnClick = state.openAppOnClick
        )

        widgetRepository.saveSync(ref)
    }
}