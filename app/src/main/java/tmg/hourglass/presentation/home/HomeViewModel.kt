package tmg.hourglass.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import tmg.hourglass.core.crashlytics.AnalyticsManager
import tmg.hourglass.domain.enums.CountdownType
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
import javax.inject.Inject

data class UiState(
    val items: List<ListItem>,
) {
    constructor(): this(
        items = emptyList(),
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

    private val sectionExpanded = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    private val untaggedSort = MutableStateFlow(preferencesManager.sortOrder)

    val uiState: StateFlow<UiState> =
        combine(
            flow = getTaggedCountdownsUseCase(),
            flow2 = sectionExpanded,
            flow3 = untaggedSort,
            transform = { list, sections, untaggedSort ->
                UiState(buildList(list, sections, untaggedSort))
            }
        )
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = UiState()
        )

    private fun Map<String, Boolean>.isExpanded(tabId: String) = this[tabId] ?: true

    private fun buildList(
        list: List<TaggedCountdowns>,
        sections: Map<String, Boolean>,
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
                        val isExpand = sections.isExpanded(item.tag.tagId)
                        add(ListItem.TagHeader(item.tag, isExpand, item.sort))
                        if (isExpand) {
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
        val sections = sectionExpanded.value.toMutableMap()
        sections[tag.tagId] = expanded
        sectionExpanded.value = sections.toMap()
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