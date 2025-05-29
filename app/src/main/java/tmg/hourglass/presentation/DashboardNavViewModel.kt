package tmg.hourglass.presentation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import tmg.hourglass.presentation.navigation.NavigationController
import tmg.hourglass.presentation.navigation.NavigationDestination
import javax.inject.Inject

data class UiState(
    val tab: Tab = Tab.DASHBOARD
)

@HiltViewModel
class DashboardNavViewModel @Inject constructor(
    private val navigationController: NavigationController
): ViewModel() {

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    fun selectTab(tab: Tab) {
        if (uiState.value.tab == tab) {
            return
        }
        navigationController.navigate(when (tab) {
            Tab.DASHBOARD -> NavigationDestination.Home
            Tab.SETTINGS -> NavigationDestination.Settings
        })
        _uiState.update { it.copy(tab = tab) }
    }
}