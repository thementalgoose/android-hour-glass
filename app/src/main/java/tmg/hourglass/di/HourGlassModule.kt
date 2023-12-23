package tmg.hourglass.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import tmg.hourglass.prefs.AppPreferencesManager
import tmg.hourglass.prefs.PreferencesManager

@Module
@InstallIn(SingletonComponent::class)
class HourGlassModule {

    @Provides
    fun providesPreferencesManager(impl: AppPreferencesManager): PreferencesManager = impl

    @Provides
    fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO
}