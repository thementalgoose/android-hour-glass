package tmg.hourglass.core.googleanalytics.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tmg.hourglass.core.googleanalytics.CrashReporter
import tmg.hourglass.core.googleanalytics.FirebaseCrashReporter


@Module
@InstallIn(SingletonComponent::class)
abstract class CrashModule {

    @Binds
    abstract fun bindCrashReporter(impl: FirebaseCrashReporter): CrashReporter
}