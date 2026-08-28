package tmg.hourglass.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import tmg.hourglass.core.crashlytics.AnalyticsManager
import tmg.hourglass.domain.model.Countdown
import tmg.hourglass.domain.model.Tag
import tmg.hourglass.domain.model.TagOrdering
import tmg.hourglass.domain.model.TaggedCountdowns
import tmg.hourglass.domain.repositories.CountdownRepository
import tmg.hourglass.domain.repositories.PreferencesManager
import tmg.hourglass.domain.repositories.TagRepository
import tmg.hourglass.domain.usecases.GetTaggedCountdownsUseCase
import tmg.hourglass.domain.usecases.sortBy
import java.time.LocalDateTime
import java.time.Month
import javax.inject.Inject

data class UiState(
    val items: List<ListItem>,
    val showSnow: Boolean = false,
) {
    constructor(): this(
        items = emptyList()
    )
    val isEmpty: Boolean
        get() = items.isEmpty()

    companion object
}

sealed interface ListItem {
    val id: String

    data class TagHeader(
        val tag: Tag,
        val expand: Boolean?,
        val sort: TagOrdering
    ): ListItem {
        override val id: String
            get() = tag.tagId
    }

    data class UntaggedHeader(
        val sort: TagOrdering
    ): ListItem {
        override val id: String
            get() = "untagged"
    }

    data class CountdownItem(
        val countdown: Countdown
    ): ListItem {
        override val id: String
            get() = countdown.id
    }
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    getTaggedCountdownsUseCase: GetTaggedCountdownsUseCase,
    private val countdownRepository: CountdownRepository,
    private val tagRepository: TagRepository,
    private val preferencesManager: PreferencesManager,
    private val analyticsManager: AnalyticsManager
): ViewModel() {

    private val untaggedSort = MutableStateFlow(preferencesManager.sortOrder)
    private val shouldShowSnow: Boolean by lazy {
        val now = LocalDateTime.now()
        val day = now.dayOfMonth
        val month = now.month
        return@lazy when (month) {
            Month.JANUARY if day <= 4 -> true
            Month.DECEMBER if day >= 15 -> true
            else -> false
        }
    }

    val uiState: StateFlow<UiState> =
        combine(
            flow = getTaggedCountdownsUseCase(),
            flow2 = untaggedSort,
            transform = { list, untaggedSort ->
                UiState(
                    items = buildList(list,  untaggedSort),
                    showSnow = shouldShowSnow
                )
            }
        )
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = UiState(
                items = emptyList(),
                showSnow = shouldShowSnow,
            )
        )

    private fun buildList(
        list: List<TaggedCountdowns>,
        untaggedSort: TagOrdering
    ): List<ListItem> {
        val now = LocalDateTime.now()
        if (list.size == 1 && list.first() is TaggedCountdowns.Untagged) {
            return listOf(ListItem.UntaggedHeader(untaggedSort)) + list.first()
                .countdowns
                .sortBy(now, untaggedSort)
                .map { ListItem.CountdownItem(it) }

        }
        return buildList {
            for (item in list) {
                when (item) {
                    is TaggedCountdowns.Tagged -> {
                        add(ListItem.TagHeader(item.tag, item.tag.expanded, item.sort))
                        if (item.tag.expanded) {
                            addAll(item.countdowns.map { countdown -> ListItem.CountdownItem(countdown) })
                        }
                    }
                    is TaggedCountdowns.Untagged -> {
                        add(ListItem.UntaggedHeader(untaggedSort))
                        addAll(item.countdowns
                            .sortBy(now, untaggedSort)
                            .map { countdown -> ListItem.CountdownItem(countdown) })
                    }
                }
            }
        }
    }

    fun tagExpanded(tag: Tag, expanded: Boolean) {
        tagRepository.insertTag(tag.copy(expanded = expanded))
    }

    fun untaggedSort(tagOrdering: TagOrdering) {
        preferencesManager.sortOrder = tagOrdering
        untaggedSort.value = tagOrdering
    }

    fun tagSortUpdated(tag: Tag, tagOrdering: TagOrdering) {
        tagRepository.insertTag(tag.copy(sort = tagOrdering))
    }

    fun delete(countdown: Countdown) {
        analyticsManager.event("countdown_remove")
        countdownRepository.delete(countdown.id)
    }
}