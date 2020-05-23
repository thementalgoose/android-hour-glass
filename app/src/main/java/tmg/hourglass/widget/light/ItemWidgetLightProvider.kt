package tmg.hourglass.widget.light

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.util.Log
import tmg.hourglass.R
import tmg.hourglass.widget.dark.ItemWidgetDarkProvider
import tmg.hourglass.widget.onUpdate

class ItemWidgetLightProvider: AppWidgetProvider() {

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
    }

    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {
        onUpdate<ItemWidgetLightProvider>(
            context = context,
            layoutId = R.layout.widget_item_light,
            appWidgetManager = appWidgetManager,
            appWidgetIds = appWidgetIds
        )
    }
}
