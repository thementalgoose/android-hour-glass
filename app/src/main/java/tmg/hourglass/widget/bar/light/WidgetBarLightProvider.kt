package tmg.hourglass.widget.bar.light

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.util.Log
import tmg.hourglass.R
import tmg.hourglass.widget.bar.onUpdateBar

class WidgetBarLightProvider: AppWidgetProvider() {

    override fun onReceive(context: Context?, intent: Intent?) {

        super.onReceive(context, intent)
    }

    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {
        Log.i("HourGlass", "Processing light widget updates for ids ${appWidgetIds?.joinToString(",")}")
        onUpdateBar<WidgetBarLightProvider>(
            context = context,
            layoutId = R.layout.widget_bar_light,
            appWidgetManager = appWidgetManager,
            appWidgetIds = appWidgetIds
        )
    }
}
