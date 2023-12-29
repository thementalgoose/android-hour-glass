package tmg.hourglass.widgets.di

import android.content.Context
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import tmg.hourglass.realm.connectors.RealmWidgetConnector

@EntryPoint
@InstallIn(SingletonComponent::class)
internal interface WidgetsEntryPoints {

    fun realmWidgetConnector(): RealmWidgetConnector
    fun navigator(): WidgetNavigator

    companion object {
        fun get(context: Context): WidgetsEntryPoints = EntryPointAccessors.fromApplication(context, WidgetsEntryPoints::class.java)
    }
}