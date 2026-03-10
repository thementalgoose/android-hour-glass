package tmg.hourglass.presentation.tag

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import tmg.hourglass.core.crashlytics.AnalyticsManager
import tmg.hourglass.domain.model.Tag
import tmg.hourglass.domain.model.TagOrdering
import tmg.hourglass.domain.repositories.PreferencesManager
import tmg.hourglass.domain.repositories.TagRepository
import java.util.UUID
import javax.inject.Inject

data class TagUiState(
    val tags: List<Tag>,
    val tagInput: String
)

@HiltViewModel
class TagViewModel @Inject constructor(
    private val tagRepository: TagRepository,
    private val analyticsManager: AnalyticsManager
): ViewModel() {
    private val tagInput: MutableStateFlow<String> = MutableStateFlow("")
    val uiState: StateFlow<TagUiState> =
        combine(
            tagRepository.getAll(),
            tagInput
        ) { list, input ->
            TagUiState(
                tags = list.sortedBy { it.name.lowercase() },
                tagInput = input
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = TagUiState(emptyList(), "")
        )

    fun insertTag(name: String) {
        val tag = Tag(
            tagId = UUID.randomUUID().toString(),
            name = name.trim(),
            colour = "#AD1457",
            sort = TagOrdering.FINISHING_SOONEST,
            expanded = true
        )
        tagRepository.insertTag(tag)
        analyticsManager.event("tag_add")
    }

    fun inputTag(tag: String) {
        tagInput.update { tag }
    }

    fun deleteTag(tag: Tag) {
        analyticsManager.event("tag_remove")
        tagRepository.deleteTag(tag)
    }
}