package tmg.hourglass.widget.bar.dark

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.util.Log
import tmg.hourglass.R
import tmg.hourglass.widget.bar.onUpdateBar


class WidgetBarDarkProvider : AppWidgetProvider() {

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
    }

    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {
        Log.i("HourGlass", "Processing dark widget updates for ids ${appWidgetIds?.joinToString(",")}")
        onUpdateBar<WidgetBarDarkProvider>(
            context = context,
            layoutId = R.layout.widget_bar_dark,
            appWidgetManager = appWidgetManager,
            appWidgetIds = appWidgetIds
        )
    }
}

