package tmg.hourglass.presentation

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import tmg.hourglass.navigation.Screen
import tmg.hourglass.presentation.navigation.NavigationController
import javax.inject.Inject

data class UiState(
    val tab: Tab = Tab.DASHBOARD
)

@HiltViewModel
class DashboardNavViewModel @Inject constructor(
    private val navigationController: NavigationController
): ViewModel(), NavController.OnDestinationChangedListener {

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    fun selectTab(tab: Tab) {
        navigationController.navigate(when (tab) {
            Tab.DASHBOARD -> Screen.Home
            Tab.SETTINGS -> Screen.Settings
        })
    }

    private fun update(callback: UiState.() -> UiState) {
        _uiState.value = callback(_uiState.value)
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        when (destination.route) {
            Screen.Home.route -> update { copy(tab = Tab.DASHBOARD) }
            Screen.Settings.route -> update { copy(tab = Tab.SETTINGS) }
        }
    }
}