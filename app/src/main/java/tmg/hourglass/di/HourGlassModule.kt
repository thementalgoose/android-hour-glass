package tmg.hourglass.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tmg.hourglass.analytics.AnalyticsManager
import tmg.hourglass.analytics.FirebaseAnalyticsManager
import tmg.hourglass.crash.CrashReporter
import tmg.hourglass.crash.FirebaseCrashReporter
import tmg.hourglass.data.connectors.CountdownConnector
import tmg.hourglass.data.connectors.WidgetConnector
import tmg.hourglass.home.HomeViewModel
import tmg.hourglass.modify.ModifyViewModel
import tmg.hourglass.prefs.PreferencesManager
import tmg.hourglass.prefs.AppPreferencesManager
import tmg.hourglass.realm.connectors.RealmCountdownConnector
import tmg.hourglass.realm.connectors.RealmWidgetConnector
import tmg.hourglass.settings.SettingsViewModel
import tmg.hourglass.settings.privacy.PrivacyPolicyViewModel
import tmg.hourglass.settings.release.ReleaseViewModel
import tmg.hourglass.widget.ItemWidgetPickerViewModel

val hourGlassModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { ModifyViewModel(get(), get()) }
    viewModel { SettingsViewModel(get(), get(), get()) }
    viewModel { ReleaseViewModel() }
    viewModel { PrivacyPolicyViewModel() }

    viewModel { ItemWidgetPickerViewModel(get(), get()) }

    single<CountdownConnector> { RealmCountdownConnector() }
    single<WidgetConnector> { RealmWidgetConnector(get()) }

    single<PreferencesManager> { AppPreferencesManager(get()) }
    single<AnalyticsManager> { FirebaseAnalyticsManager(get()) }
    single<CrashReporter> { FirebaseCrashReporter() }
}