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
import tmg.hourglass.domain.repositories.RealmRepo
import tmg.hourglass.navigation.Screen
import tmg.hourglass.presentation.navigation.NavigationController
import javax.inject.Inject

data class UiState(
    val upcoming: List<Countdown>,
    val expired: List<Countdown>,
    val action: HomeAction?
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

sealed class HomeAction {
    data class Modify(
        val countdown: Countdown
    ): HomeAction()

    data object Add: HomeAction()
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    @param:RealmRepo
    private val countdownRepository: CountdownRepository,
    private val navigationController: NavigationController,
): ViewModel() {

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    init {
        loadDashboard()
    }

    fun navigateToSettings() {
        navigationController.navigate(Screen.Settings)
    }

    fun closeAction() {
        update { copy(action = null) }
    }

    fun createNew() {
        update { copy(action = HomeAction.Add) }
    }

    fun refresh() {
        loadDashboard()
    }

    fun edit(countdown: Countdown) {
        update { copy(action = HomeAction.Modify(countdown)) }
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
                    action = null
                )
            }
        }
    }

    private fun update(callback: UiState.() -> UiState) {
        _uiState.value = callback(_uiState.value)
    }
}