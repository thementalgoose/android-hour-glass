package tmg.hourglass.realm.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tmg.hourglass.domain.repositories.CountdownRepository
import tmg.hourglass.domain.repositories.WidgetRepository
import tmg.hourglass.realm.repositories.CountdownRealmRepository
import tmg.hourglass.realm.repositories.WidgetRealmRepository

@Module
@InstallIn(SingletonComponent::class)
class RealmModule {

    @Provides
    fun providesCountdownConnector(impl: CountdownRealmRepository): CountdownRepository = impl

    @Provides
    fun providesWidgetConnector(impl: WidgetRealmRepository): WidgetRepository = impl
}