package tmg.hourglass.widgets.presentation.multiple.configure

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import tmg.hourglass.domain.model.Tag
import tmg.hourglass.domain.model.WidgetReference
import tmg.hourglass.domain.repositories.TagRepository
import tmg.hourglass.domain.repositories.WidgetRepository
import javax.inject.Inject

data class CountdownsUiState(
    val tags: List<Tag>,
    val selectedTagId: String?,
    val openAppOnClick: Boolean,
    val appWidgetId: Int
) {
    constructor() : this(
        tags = emptyList(),
        selectedTagId = null,
        openAppOnClick = false,
        appWidgetId = -1
    )
}

@HiltViewModel
class CountdownsConfigurationViewModel @Inject constructor(
    private val tagRepository: TagRepository,
    private val widgetRepository: WidgetRepository,
) : ViewModel() {

    private val _uiState: MutableStateFlow<CountdownsUiState> = MutableStateFlow(CountdownsUiState())
    val uiState: StateFlow<CountdownsUiState> = _uiState

    init {
        refresh()
    }

    fun load(appWidgetId: Int) {
        Log.i("CountdownsConfig", "Loading widget id $appWidgetId")
        val widgetReference = widgetRepository.getSync(appWidgetId)
        if (widgetReference is WidgetReference.Multiple) {
            _uiState.value = _uiState.value.copy(
                appWidgetId = appWidgetId,
                selectedTagId = widgetReference.tagId,
                openAppOnClick = widgetReference.openAppOnClick
            )
        } else {
            _uiState.value = _uiState.value.copy(
                appWidgetId = appWidgetId
            )
        }
    }

    fun openAppOnClick(openAppOnClick: Boolean) {
        _uiState.value = _uiState.value.copy(
            openAppOnClick = openAppOnClick
        )
    }

    fun selectTag(tagId: String?) {
        _uiState.value = _uiState.value.copy(
            selectedTagId = tagId
        )
    }

    private fun refresh() {
        viewModelScope.launch {
            val currentTags = tagRepository.getAll().first()
            _uiState.value = _uiState.value.copy(
                tags = currentTags,
            )
        }
    }

    fun save() {
        val state = uiState.value
        Log.i("CountdownsConfig", "Saving widget configuration ${state.appWidgetId}")
        if (state.appWidgetId == -1) {
            return
        }
        val ref = WidgetReference.Multiple(
            appWidgetId = state.appWidgetId,
            tagId = state.selectedTagId,
            openAppOnClick = state.openAppOnClick
        )

        widgetRepository.saveSync(ref)
    }
}
