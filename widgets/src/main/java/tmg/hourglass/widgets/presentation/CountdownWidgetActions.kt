package tmg.hourglass.widgets.presentation

import android.content.Context
import android.util.Log
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.updateAll
import tmg.hourglass.widgets.utils.appWidgetId

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