package tmg.hourglass.widgets.presentation

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import tmg.hourglass.widgets.di.WidgetsEntryPoints

internal class CountdownWidgetActionOpen: ActionCallback {

    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        val intent = WidgetsEntryPoints.get(context).navigator().getIntent(context)
        context.startActivity(intent)
    }
}