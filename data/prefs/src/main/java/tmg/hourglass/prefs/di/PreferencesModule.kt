package tmg.hourglass.prefs.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tmg.hourglass.domain.repositories.PreferencesManager
import tmg.hourglass.prefs.AppPreferencesManager

@Module
@InstallIn(SingletonComponent::class)
abstract class PreferencesModule {
    
    @Binds
    abstract fun bindAppPreferenceManager(impl: AppPreferencesManager): PreferencesManager
}