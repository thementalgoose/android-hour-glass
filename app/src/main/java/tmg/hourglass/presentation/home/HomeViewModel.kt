package tmg.hourglass.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import tmg.hourglass.domain.repositories.CountdownRepository
import tmg.hourglass.domain.model.Countdown
import tmg.hourglass.domain.model.Tag
import tmg.hourglass.domain.model.TagOrdering
import tmg.hourglass.domain.model.TaggedCountdowns
import tmg.hourglass.domain.repositories.PreferencesManager
import tmg.hourglass.domain.repositories.TagRepository
import tmg.hourglass.domain.usecases.GetTaggedCountdownsUseCase
import tmg.hourglass.navigation.Screen
import tmg.hourglass.presentation.navigation.NavigationController
import java.time.LocalDateTime
import javax.inject.Inject

data class UiState(
    val items: List<TaggedCountdowns>,
    val action: HomeAction?
) {
    constructor(): this(
        items = emptyList(),
        action = null
    )

    val isEmpty: Boolean
        get() = items.isEmpty()

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
    private val navigationController: NavigationController,
    getTaggedCountdownsUseCase: GetTaggedCountdownsUseCase,
    private val countdownRepository: CountdownRepository,
    private val tagRepository: TagRepository
): ViewModel() {

    private val action: MutableStateFlow<HomeAction?> = MutableStateFlow(null)
    val uiState: StateFlow<UiState> =
        combine(
            getTaggedCountdownsUseCase(),
            action
        ) { list, action -> UiState(list, action) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = UiState()
            )

    fun navigateToSettings() {
        navigationController.navigate(Screen.Settings)
    }

    fun tagSortUpdated(tag: Tag, tagOrdering: TagOrdering) {
        tagRepository.insertTag(tag.copy(sort = tagOrdering))
    }

    fun closeAction() {
        action.value = null
    }

    fun createNew() {
        action.value = HomeAction.Add
    }

    fun edit(countdown: Countdown) {
        action.value = HomeAction.Modify(countdown)
    }

    fun delete(countdown: Countdown) {
        countdownRepository.delete(countdown.id)
    }
}