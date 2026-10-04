package tmg.hourglass.wearos.presentation

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import tmg.hourglass.domain.model.Countdown
import tmg.hourglass.wearos.data.WearosCountdownRepository
import javax.inject.Inject

sealed interface ComplicationConfigUiState {
    data object Loading : ComplicationConfigUiState
    data class Content(val countdowns: List<Countdown>) : ComplicationConfigUiState
    data object UpdateRequired : ComplicationConfigUiState
}

@HiltViewModel
class ComplicationConfigViewModel @Inject constructor(
    private val repository: WearosCountdownRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ComplicationConfigUiState>(ComplicationConfigUiState.Loading)
    val uiState: StateFlow<ComplicationConfigUiState> = _uiState.asStateFlow()

    val countdowns: StateFlow<List<Countdown>>
        get() = _countdowns
    private val _countdowns = MutableStateFlow<List<Countdown>>(emptyList())

    init {
        loadCountdowns()
    }

    fun loadCountdowns() {
        if (!repository.isSchemaSupported()) {
            _uiState.value = ComplicationConfigUiState.UpdateRequired
            return
        }

        val cached = repository.getCountdowns()
        _countdowns.value = cached
        _uiState.value = ComplicationConfigUiState.Content(cached)

        viewModelScope.launch {
            val fetched = repository.fetchCountdownsFromDataClient()
            if (!repository.isSchemaSupported()) {
                _uiState.value = ComplicationConfigUiState.UpdateRequired
            } else {
                _countdowns.value = fetched
                _uiState.value = ComplicationConfigUiState.Content(fetched)
            }
        }
    }

    fun refresh() {
        loadCountdowns()
    }

    fun saveComplicationBinding(complicationId: Int, countdownId: String) {
        repository.saveComplicationBinding(complicationId, countdownId)
    }

    fun openPlayStoreOnWatch(context: Context) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=tmg.hourglass")).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to open Play Store on watch", e)
        }
    }

    fun openPlayStoreOnPhone(context: Context) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=tmg.hourglass")).apply {
                addCategory(Intent.CATEGORY_BROWSABLE)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to open Play Store link for phone", e)
        }
    }

    companion object {
        private const val TAG = "ComplicationConfigVM"
    }
}
