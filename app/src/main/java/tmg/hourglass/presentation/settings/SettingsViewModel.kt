package tmg.hourglass.presentation.settings

import android.content.Context
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import tmg.hourglass.core.crashlytics.AnalyticsManager
import tmg.hourglass.domain.model.ThemeSelection
import tmg.hourglass.domain.repositories.CountdownRepository
import tmg.hourglass.domain.repositories.PreferencesManager
import tmg.hourglass.presentation.ThemePref
import tmg.hourglass.presentation.usecases.ChangeThemeUseCase
import javax.inject.Inject

data class UiState(
    val screen: SettingsType?,
    val theme: ThemePref,
    val crashReporting: Boolean,
    val anonymousAnalytics: Boolean,
    val notificationsEnabled: Boolean = false,
) {
    constructor(): this(
        screen = null,
        theme = ThemePref.AUTO,
        crashReporting = false,
        anonymousAnalytics = false,
        notificationsEnabled = false,
    )
}

enum class SettingsType {
    PRIVACY_POLICY,
    BACKUP,
}

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val prefManager: PreferencesManager,
    private val countdownRepository: CountdownRepository,
    private val changeThemeUseCase: ChangeThemeUseCase,
    private val analyticsManager: AnalyticsManager,
    @param:ApplicationContext private val context: Context
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
        prefManager.theme = theme.toSelection()
        changeThemeUseCase.update(theme)
        refresh()
    }

    fun deleteAll() {
        analyticsManager.event("countdown_removeall")
        countdownRepository.deleteAll()
    }

    fun refresh() {
        val notificationsEnabled = NotificationManagerCompat.from(context).areNotificationsEnabled()
        update { copy(
            crashReporting = prefManager.crashReporting,
            anonymousAnalytics = prefManager.analyticsEnabled,
            theme = prefManager.theme.toPref(),
            notificationsEnabled = notificationsEnabled
        )}
    }

    private fun ThemeSelection.toPref(): ThemePref {
        return when (this) {
            ThemeSelection.FollowSystem -> ThemePref.AUTO
            ThemeSelection.Light -> ThemePref.LIGHT
            ThemeSelection.Dark -> ThemePref.DARK
        }
    }

    private fun ThemePref.toSelection(): ThemeSelection {
        return when (this) {
            ThemePref.AUTO -> ThemeSelection.FollowSystem
            ThemePref.LIGHT -> ThemeSelection.Light
            ThemePref.DARK -> ThemeSelection.Dark
        }
    }

    private fun update(callback: UiState.() -> UiState) {
        _uiState.value = callback(_uiState.value)
    }
}