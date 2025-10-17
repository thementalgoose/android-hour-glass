package tmg.hourglass.presentation.settings

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import tmg.hourglass.domain.repositories.CountdownRepository
import tmg.hourglass.prefs.PreferencesManager
import tmg.hourglass.presentation.ThemePref
import tmg.hourglass.presentation.usecases.ChangeThemeUseCase
import javax.inject.Inject

data class UiState(
    val screen: SettingsType?,
    val theme: ThemePref,
    val crashReporting: Boolean,
    val anonymousAnalytics: Boolean,
) {
    constructor(): this(
        screen = null,
        theme = ThemePref.AUTO,
        crashReporting = false,
        anonymousAnalytics = false,
    )
}

enum class SettingsType {
    PRIVACY_POLICY,
}

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val prefManager: PreferencesManager,
    private val countdownRepository: CountdownRepository,
    private val changeThemeUseCase: ChangeThemeUseCase,
): ViewModel() {

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    init {
        refresh()
    }

    fun closeDetails() {
        update { copy(screen = null) }
    }

    fun clickScreen(screenType: SettingsType) {
        update { copy(screen = screenType) }
    }

    fun setAnalytics(enabled: Boolean) {
        prefManager.analyticsEnabled = enabled
        refresh()
    }

    fun setCrash(enabled: Boolean) {
        prefManager.crashReporting = enabled
        refresh()
    }

    fun setTheme(theme: ThemePref) {
        prefManager.theme = theme
        changeThemeUseCase.update(theme)
        refresh()
    }

    fun deleteAll() {
        countdownRepository.deleteAll()
    }

    private fun refresh() {
        update { copy(
            crashReporting = prefManager.crashReporting,
            anonymousAnalytics = prefManager.analyticsEnabled,
            theme = prefManager.theme
        )}
    }
    private fun update(callback: UiState.() -> UiState) {
        _uiState.value = callback(_uiState.value)
    }
}