package tmg.hourglass.widgets.di

import android.content.Context
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import tmg.hourglass.domain.repositories.CountdownRepository
import tmg.hourglass.domain.repositories.RealmRepo
import tmg.hourglass.domain.repositories.WidgetRepository

@EntryPoint
@InstallIn(SingletonComponent::class)
internal interface WidgetsEntryPoints {

    @RealmRepo
    fun widgetConnector(): WidgetRepository
    @RealmRepo
    fun countdownConnector(): CountdownRepository
    fun navigator(): WidgetNavigator

    companion object {
        fun get(context: Context): WidgetsEntryPoints = EntryPointAccessors.fromApplication(context, WidgetsEntryPoints::class.java)
    }
}