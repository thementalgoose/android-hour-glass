package tmg.hourglass.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tmg.hourglass.data.connectors.CountdownConnector
import tmg.hourglass.home.HomeViewModel
import tmg.hourglass.modify.ModifyViewModel
import tmg.hourglass.prefs.IPrefs
import tmg.hourglass.prefs.Prefs
import tmg.hourglass.realm.connectors.RealmCountdownConnector
import tmg.hourglass.settings.SettingsViewModel
import tmg.hourglass.settings.privacy.PrivacyPolicyViewModel
import tmg.hourglass.settings.release.ReleaseViewModel

val hourGlassModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { ModifyViewModel(get()) }
    viewModel { SettingsViewModel(get(), get()) }
    viewModel { ReleaseViewModel(get()) }
    viewModel { PrivacyPolicyViewModel(get()) }

    single<CountdownConnector> { RealmCountdownConnector() }
    single<IPrefs> { Prefs(get()) }
}