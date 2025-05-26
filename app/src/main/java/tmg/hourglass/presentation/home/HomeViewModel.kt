package tmg.hourglass.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import tmg.hourglass.domain.repositories.CountdownRepository
import tmg.hourglass.domain.model.Countdown
import tmg.hourglass.presentation.navigation.NavigationController
import tmg.hourglass.presentation.navigation.NavigationDestination
import javax.inject.Inject

data class UiState(
    val upcoming: List<Countdown>,
    val expired: List<Countdown>,
) {
    constructor(): this(
        upcoming = emptyList(),
        expired = emptyList(),
    )

    val isEmpty: Boolean
        get() = upcoming.isEmpty() && expired.isEmpty()

    companion object
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val countdownRepository: CountdownRepository,
    private val navigationController: NavigationController,
): ViewModel() {

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    init {
        loadDashboard()
    }

    fun navigateToSettings() {
        navigationController.navigate(NavigationDestination.Settings)
    }

    fun createNew() {
        navigationController.navigate(NavigationDestination.AddCountdown)
    }

    fun refresh() {
        loadDashboard()
    }

    fun edit(countdown: Countdown) {
        navigationController.navigate(NavigationDestination.ModifyCountdown(countdown.id))
    }

    fun delete(countdown: Countdown) {
        countdownRepository.delete(countdown.id)
        loadDashboard()
    }

    private fun loadDashboard() {
        viewModelScope.launch {
            val expired = countdownRepository.allDone().first()
            val upcoming = countdownRepository.allCurrent().first()

            update {
                copy(
                    upcoming = upcoming,
                    expired = expired,
                )
            }
        }
    }

    private fun update(callback: UiState.() -> UiState) {
        _uiState.value = callback(_uiState.value)
    }
}