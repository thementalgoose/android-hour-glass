package tmg.passage.di

import org.koin.androidx.viewmodel.compat.ScopeCompat.viewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tmg.passage.data.connectors.PassageConnector
import tmg.passage.home.HomeViewModel
import tmg.passage.modify.ModifyViewModel
import tmg.passage.realm.connectors.RealmPassageConnector

val passageModule = module {
    viewModel { HomeViewModel() }
    viewModel { ModifyViewModel(get()) }

    single<PassageConnector> { RealmPassageConnector() }
}