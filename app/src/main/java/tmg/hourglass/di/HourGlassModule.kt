package tmg.hourglass.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tmg.hourglass.analytics.AnalyticsManager
import tmg.hourglass.analytics.FirebaseAnalyticsManager
import tmg.hourglass.crash.CrashReporter
import tmg.hourglass.crash.FirebaseCrashReporter
import tmg.hourglass.dashboard.DashboardViewModel
import tmg.hourglass.modify.ModifyViewModel
import tmg.hourglass.prefs.PreferencesManager
import tmg.hourglass.prefs.AppPreferencesManager
import tmg.hourglass.settings.SettingsViewModel
import tmg.hourglass.settings.privacy.PrivacyPolicyViewModel
import tmg.hourglass.settings.release.ReleaseViewModel

val hourGlassModule = module {
    viewModel { DashboardViewModel(get()) }
    viewModel { ModifyViewModel(get(), get()) }

    viewModel { SettingsViewModel(get(), get()) }
    viewModel { ReleaseViewModel() }
    viewModel { PrivacyPolicyViewModel() }

    single<PreferencesManager> { AppPreferencesManager(get()) }
    single<AnalyticsManager> { FirebaseAnalyticsManager(get()) }
    single<CrashReporter> { FirebaseCrashReporter() }
}