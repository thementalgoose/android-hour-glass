package tmg.hourglass.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import tmg.hourglass.domain.connectors.CountdownConnector
import tmg.hourglass.domain.model.Countdown
import tmg.hourglass.presentation.HomeTab
import javax.inject.Inject

data class UiState(
    val upcoming: List<Countdown>,
    val expired: List<Countdown>,
    val action: DashboardAction?
) {
    constructor(): this(
        upcoming = emptyList(),
        expired = emptyList(),
        action = null
    )

    val isEmpty: Boolean
        get() = upcoming.isEmpty() && expired.isEmpty()

    companion object
}

sealed class DashboardAction {
    data class Modify(
        val countdown: Countdown
    ): DashboardAction()

    data object Add: DashboardAction()
}

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val countdownConnector: CountdownConnector
): ViewModel() {

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    init {
        loadDashboard()
    }

    fun closeAction() {
        _uiState.update { copy(action = null) }
    }

    fun createNew() {
        _uiState.update { copy(action = DashboardAction.Add) }
    }

    fun refresh() {
        loadDashboard()
    }

    fun edit(countdown: Countdown) {
        _uiState.update { copy(action = DashboardAction.Modify(countdown)) }
    }

    fun delete(countdown: Countdown) {
        countdownConnector.delete(countdown.id)
        loadDashboard()
    }

    private fun loadDashboard() {
        viewModelScope.launch {
            val expired = countdownConnector.allDone().first()
            val upcoming = countdownConnector.allCurrent().first()

            _uiState.update {
                copy(
                    upcoming = upcoming,
                    expired = expired,
                    action = null
                )
            }
        }
    }

    private fun MutableStateFlow<UiState>.update(callback: UiState.() -> UiState) {
        _uiState.value = callback(_uiState.value)
    }
}