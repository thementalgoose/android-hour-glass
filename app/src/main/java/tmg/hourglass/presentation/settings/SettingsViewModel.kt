package tmg.hourglass.presentation.settings

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import tmg.hourglass.domain.connectors.CountdownConnector
import tmg.hourglass.prefs.PreferencesManager
import tmg.hourglass.presentation.ThemePref
import tmg.hourglass.presentation.usecases.ChangeThemeUseCase
import javax.inject.Inject

data class UiState(
    val screen: SettingsType?,
    val theme: ThemePref,
    val showWidgetUpdatedDate: Boolean,
    val crashReporting: Boolean,
    val anonymousAnalytics: Boolean,
    val shakeToReport: Boolean,
) {
    constructor(): this(
        screen = null,
        theme = ThemePref.AUTO,
        showWidgetUpdatedDate = false,
        crashReporting = false,
        anonymousAnalytics = false,
        shakeToReport = false,
    )
}

enum class SettingsType {
    PRIVACY_POLICY,
    RELEASE
}

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val prefManager: PreferencesManager,
    private val countdownConnector: CountdownConnector,
    private val changeThemeUseCase: ChangeThemeUseCase
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

    fun setShakeToReport(enabled: Boolean) {
        prefManager.shakeToReport = enabled
        refresh()
    }

    fun setWidgetDate(enabled: Boolean) {
        prefManager.widgetShowUpdate = enabled
        refresh()
    }

    fun setTheme(theme: ThemePref) {
        prefManager.theme = theme
        changeThemeUseCase.update(theme)
        refresh()
    }

    fun deleteAll() {
        countdownConnector.deleteAll()
    }

    private fun refresh() {
        update { copy(
            showWidgetUpdatedDate = prefManager.widgetShowUpdate,
            crashReporting = prefManager.crashReporting,
            anonymousAnalytics = prefManager.analyticsEnabled,
            shakeToReport = prefManager.shakeToReport,
            theme = prefManager.theme
        )}
    }
    private fun update(callback: UiState.() -> UiState) {
        _uiState.value = callback(_uiState.value)
    }
}