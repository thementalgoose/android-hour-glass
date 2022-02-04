package tmg.hourglass.widgets

import androidx.glance.appwidget.GlanceAppWidgetReceiver
import tmg.hourglass.widgets.layout.CountdownWidget

class WidgetReceiver : GlanceAppWidgetReceiver() {

    override val glanceAppWidget = CountdownWidget(
        "test-id"
    )
}