package tmg.hourglass.realm.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tmg.hourglass.domain.connectors.CountdownConnector
import tmg.hourglass.domain.connectors.WidgetConnector
import tmg.hourglass.realm.connectors.RealmCountdownConnector
import tmg.hourglass.realm.connectors.RealmWidgetConnector

@Module
@InstallIn(SingletonComponent::class)
class RealmModule {

    @Provides
    fun providesCountdownConnector(impl: RealmCountdownConnector): CountdownConnector = impl

    @Provides
    fun providesWidgetConnector(impl: RealmWidgetConnector): WidgetConnector = impl
}