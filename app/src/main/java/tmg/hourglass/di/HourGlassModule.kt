package tmg.hourglass.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tmg.hourglass.domain.analytics.AnalyticsManager
import tmg.hourglass.domain.analytics.FirebaseAnalyticsManager
import tmg.hourglass.domain.crash.CrashReporter
import tmg.hourglass.domain.crash.FirebaseCrashReporter
import tmg.hourglass.domain.data.connectors.CountdownConnector
import tmg.hourglass.domain.data.connectors.WidgetConnector
import tmg.hourglass.home.HomeViewModel
import tmg.hourglass.modify.ModifyViewModel
import tmg.hourglass.prefs.PreferencesManager
import tmg.hourglass.prefs.AppPreferencesManager
import tmg.hourglass.domain.realm.connectors.RealmCountdownConnector
import tmg.hourglass.domain.realm.connectors.RealmWidgetConnector
import tmg.hourglass.settings.SettingsViewModel
import tmg.hourglass.settings.privacy.PrivacyPolicyViewModel
import tmg.hourglass.settings.release.ReleaseViewModel
import tmg.hourglass.widget.ItemWidgetPickerViewModel

val hourGlassModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { ModifyViewModel(get(), get()) }
    viewModel { SettingsViewModel(get(), get()) }
    viewModel { ReleaseViewModel() }
    viewModel { PrivacyPolicyViewModel() }

    viewModel { tmg.hourglass.ui.home.HomeViewModel(get()) }
    viewModel { tmg.hourglass.ui.modify.ModifyViewModel() }

    viewModel { ItemWidgetPickerViewModel(get(), get()) }

    single<CountdownConnector> { RealmCountdownConnector() }
    single<WidgetConnector> { RealmWidgetConnector(get()) }

    single<PreferencesManager> { AppPreferencesManager(get()) }
    single<AnalyticsManager> { FirebaseAnalyticsManager(get()) }
    single<CrashReporter> { FirebaseCrashReporter() }
}