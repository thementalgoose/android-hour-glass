package tmg.hourglass.widgets.di

import android.content.Context
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import tmg.hourglass.domain.connectors.CountdownConnector
import tmg.hourglass.domain.connectors.WidgetConnector

@EntryPoint
@InstallIn(SingletonComponent::class)
internal interface WidgetsEntryPoints {

    fun widgetConnector(): WidgetConnector
    fun countdownConnector(): CountdownConnector
    fun navigator(): WidgetNavigator

    companion object {
        fun get(context: Context): WidgetsEntryPoints = EntryPointAccessors.fromApplication(context, WidgetsEntryPoints::class.java)
    }
}