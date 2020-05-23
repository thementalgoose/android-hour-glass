package tmg.hourglass.widget.dark

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.util.Log
import tmg.hourglass.R
import tmg.hourglass.widget.onUpdate


class ItemWidgetDarkProvider : AppWidgetProvider() {

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
    }

    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {
        Log.i("HourGlass", "Processing dark widget updates for ids ${appWidgetIds?.joinToString(",")}")
        onUpdate<ItemWidgetDarkProvider>(
            context = context,
            layoutId = R.layout.widget_item_dark,
            appWidgetManager = appWidgetManager,
            appWidgetIds = appWidgetIds
        )
    }
}

