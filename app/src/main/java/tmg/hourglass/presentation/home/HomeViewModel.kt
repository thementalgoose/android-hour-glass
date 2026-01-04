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
import tmg.hourglass.navigation.Screen
import tmg.hourglass.prefs.PreferencesManager
import tmg.hourglass.presentation.navigation.NavigationController
import java.time.LocalDateTime
import javax.inject.Inject

data class UiState(
    val items: List<Countdown>,
    val sortOrder: SortOrder,
    val action: HomeAction?
) {
    constructor(): this(
        items = emptyList(),
        sortOrder = SortOrder.ALPHABETICAL,
        action = null
    )

    val itemsOrdered: List<Countdown> by lazy {
        val now = LocalDateTime.now()
        return@lazy when (sortOrder) {
            SortOrder.ALPHABETICAL -> items.sortedBy { it.name.lowercase() }
            SortOrder.FINISHING_SOONEST -> items.sortedBy { it.endDate }
            SortOrder.FINISHING_LATEST -> items.sortedByDescending { it.endDate }
        }
    }

    val isEmpty: Boolean
        get() = items.isEmpty()

    companion object
}

enum class SortOrder {
    ALPHABETICAL,
    FINISHING_SOONEST,
    FINISHING_LATEST
}

sealed class HomeAction {
    data class Modify(
        val countdown: Countdown
    ): HomeAction()

    data object Add: HomeAction()
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val countdownRepository: CountdownRepository,
    private val navigationController: NavigationController,
    private val prefManager: PreferencesManager,
): ViewModel() {

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    init {
        loadDashboard()
    }

    fun updateSortOrder(order: SortOrder) {
        prefManager.sortOrder = order
        update { copy(sortOrder = order) }
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
            val all = countdownRepository.all().first()
            update {
                copy(
                    items = all,
                    sortOrder = prefManager.sortOrder,
                    action = null
                )
            }
        }
    }

    private fun update(callback: UiState.() -> UiState) {
        _uiState.value = callback(_uiState.value)
    }
}