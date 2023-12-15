package tmg.hourglass.widget.circle.dark

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.util.Log
import tmg.hourglass.R
import tmg.hourglass.strings.R.string
import tmg.hourglass.widget.bar.onUpdateBar
import tmg.hourglass.widget.circle.onUpdateCircle

class WidgetCircleDarkProvider : AppWidgetProvider() {

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
    }

    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {
        Log.i("HourGlass", "Processing circle dark widget updates for ids ${appWidgetIds?.joinToString(",")}")
        onUpdateCircle<WidgetCircleDarkProvider>(
            context = context,
            layoutId = R.layout.widget_circle_dark,
            appWidgetManager = appWidgetManager,
            appWidgetIds = appWidgetIds
        )
    }
}
