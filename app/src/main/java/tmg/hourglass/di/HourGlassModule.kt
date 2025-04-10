package tmg.hourglass.di

import android.content.Context
import android.content.Intent
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import tmg.hourglass.navigation.AppNavigationController
import tmg.hourglass.prefs.AppPreferencesManager
import tmg.hourglass.prefs.PreferencesManager
import tmg.hourglass.presentation.DashboardActivity
import tmg.hourglass.presentation.navigation.NavigationController
import tmg.hourglass.widgets.di.WidgetNavigator

@Module
@InstallIn(SingletonComponent::class)
class HourGlassModule {

    @Provides
    fun providesPreferencesManager(impl: AppPreferencesManager): PreferencesManager = impl

    @Provides
    fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    fun provideWidgetNavigator(): WidgetNavigator = object : WidgetNavigator {
        override fun getIntent(context: Context): Intent {
            return Intent(context, DashboardActivity::class.java)
        }
    }

    @Provides
    fun provideAppNavigationController(impl: AppNavigationController): NavigationController = impl
}