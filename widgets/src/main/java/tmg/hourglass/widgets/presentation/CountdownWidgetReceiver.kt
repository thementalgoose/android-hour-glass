package tmg.hourglass.widgets.presentation

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.updateAll
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class CountdownWidgetReceiver: GlanceAppWidgetReceiver() {

    override val glanceAppWidget: GlanceAppWidget
        get() = CountdownWidget()

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        Log.i("CountdownWidget", "onReceive ${intent.extras?.getIntArray(AppWidgetManager.EXTRA_APPWIDGET_IDS)?.joinToString { it.toString() }}")
        runBlocking(Dispatchers.IO) {
            glanceAppWidget.updateAll(context)
        }
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        Log.i("CountdownWidget", "onUpdate ${appWidgetIds.joinToString { it.toString() }}")
    }
}