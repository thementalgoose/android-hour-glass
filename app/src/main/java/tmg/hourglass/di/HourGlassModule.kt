package tmg.hourglass.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tmg.hourglass.analytics.AnalyticsManager
import tmg.hourglass.analytics.FirebaseAnalyticsManager
import tmg.hourglass.crash.CrashReporter
import tmg.hourglass.crash.FirebaseCrashReporter
import tmg.hourglass.prefs.AppPreferencesManager
import tmg.hourglass.prefs.PreferencesManager

@Module
@InstallIn(SingletonComponent::class)
class HourGlassModule {

    @Provides
    fun providesPreferencesManager(impl: AppPreferencesManager): PreferencesManager = impl

    @Provides
    fun providesAnalyticsManager(impl: FirebaseAnalyticsManager): AnalyticsManager = impl

    @Provides
    fun providesCrashReporter(impl: FirebaseCrashReporter): CrashReporter = impl
}