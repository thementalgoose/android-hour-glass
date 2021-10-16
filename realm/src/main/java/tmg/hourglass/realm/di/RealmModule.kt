package tmg.hourglass.realm.di

import org.koin.dsl.module
import tmg.hourglass.domain.connectors.CountdownConnector
import tmg.hourglass.domain.connectors.WidgetConnector
import tmg.hourglass.realm.connectors.RealmCountdownConnector
import tmg.hourglass.realm.connectors.RealmWidgetConnector
import tmg.hourglass.realm.mappers.RealmCountdownMapper
import tmg.hourglass.realm.mappers.RealmWidgetMapper

val realmModule = module {

    single<CountdownConnector> { RealmCountdownConnector(get()) }
    single<WidgetConnector> { RealmWidgetConnector(get(), get()) }

    single { RealmWidgetMapper() }
    single { RealmCountdownMapper() }
}