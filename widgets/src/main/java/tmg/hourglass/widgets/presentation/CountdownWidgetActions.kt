package tmg.hourglass.widgets.presentation

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.appwidget.updateAll
import tmg.hourglass.widgets.di.WidgetsEntryPoints
import tmg.hourglass.widgets.updateAllWidgets
import tmg.hourglass.widgets.utils.appWidgetId
import java.util.UUID

internal class RefreshWidget : ActionCallback {

    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        Log.i("Widget", "Refresh widget action for $glanceId (${glanceId.appWidgetId})")
        CountdownWidget().updateAll(context)
    }
}