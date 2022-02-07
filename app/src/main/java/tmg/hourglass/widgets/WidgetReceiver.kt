package tmg.hourglass.widgets

import android.appwidget.AppWidgetManager
import android.content.Context
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import tmg.hourglass.widgets.layout.CountdownWidget

class WidgetReceiver : GlanceAppWidgetReceiver() {

    override val glanceAppWidget = CountdownWidget(
        "test-id"
    )
}