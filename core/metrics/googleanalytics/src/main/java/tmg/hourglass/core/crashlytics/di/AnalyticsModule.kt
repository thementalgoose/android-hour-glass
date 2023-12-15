package tmg.hourglass.core.crashlytics.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tmg.hourglass.core.crashlytics.AnalyticsManager
import tmg.hourglass.core.crashlytics.GoogleAnalyticsManager

@Module
@InstallIn(SingletonComponent::class)
abstract class AnalyticsModule {

    @Binds
    abstract fun bindAnalyticsManager(impl: GoogleAnalyticsManager): AnalyticsManager
}