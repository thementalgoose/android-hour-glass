package tmg.hourglass.widgets.presentation.multiple

import android.content.Context
import android.content.Intent
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.updateAll
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
open class CountdownsWidgetReceiver : GlanceAppWidgetReceiver() {

    override val glanceAppWidget: GlanceAppWidget
        get() = CountdownsWidget()

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        runBlocking(Dispatchers.IO) {
            glanceAppWidget.updateAll(context)
        }
    }
}
