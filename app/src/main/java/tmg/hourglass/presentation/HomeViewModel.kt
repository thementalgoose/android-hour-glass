package tmg.hourglass.presentation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

data class UiState(
    val tab: HomeTab = HomeTab.DASHBOARD
)

@HiltViewModel
class HomeViewModel @Inject constructor(): ViewModel() {

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    fun selectTab(tab: HomeTab) {
        when (tab) {
            HomeTab.DASHBOARD -> _uiState.update { copy(tab = HomeTab.DASHBOARD) }
            HomeTab.SETTINGS -> _uiState.update { copy(tab = HomeTab.SETTINGS) }
        }
    }

    private fun MutableStateFlow<UiState>.update(callback: UiState.() -> UiState) {
        _uiState.value = callback(_uiState.value)
    }

}