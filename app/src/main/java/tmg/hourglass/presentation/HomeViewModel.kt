package tmg.hourglass.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import tmg.hourglass.domain.connectors.CountdownConnector
import tmg.hourglass.domain.model.Countdown
import javax.inject.Inject

sealed class UiState(
    val tab: HomeTab
) {
    data class Dashboard(
        val upcoming: List<Countdown>,
        val expired: List<Countdown>,
        val action: DashboardAction?
    ): UiState(tab = HomeTab.DASHBOARD) {
        constructor(): this(
            upcoming = emptyList(),
            expired = emptyList(),
            action = null
        )
    }

    data class Settings(
        val screen: SettingsType?
    ): UiState(tab = HomeTab.SETTINGS) {
        constructor(): this(null)
    }
}

sealed class DashboardAction {
    data class Modify(
        val countdown: Countdown
    ): DashboardAction()

    data object Add: DashboardAction()
}

enum class SettingsType {
    PRIVACY_POLICY,
    RELEASE,
    THEME
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val countdownConnector: CountdownConnector
): ViewModel() {

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Dashboard())
    val uiState: StateFlow<UiState> = _uiState

    init {
        loadDashboard()
    }

    fun selectTab(tab: HomeTab) {
        when (tab) {
            HomeTab.DASHBOARD -> loadDashboard()
            HomeTab.SETTINGS -> _uiState.value = UiState.Settings()
        }
    }

    private fun loadDashboard() {
        viewModelScope.launch {
            val expired = countdownConnector.allDone().first()
            val upcoming = countdownConnector.allCurrent().first()

            _uiState.value = UiState.Dashboard(
                upcoming = upcoming,
                expired = expired,
                action = null
            )
        }
    }
}