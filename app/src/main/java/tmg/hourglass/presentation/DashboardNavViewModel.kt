package tmg.hourglass.presentation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

data class UiState(
    val tab: Tab = Tab.DASHBOARD
)

@HiltViewModel
class DashboardNavViewModel @Inject constructor(): ViewModel() {

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    fun selectTab(tab: Tab) {
        _uiState.value = _uiState.value.copy(tab = tab)
    }
}