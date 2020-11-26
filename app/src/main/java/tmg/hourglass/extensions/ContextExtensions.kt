package tmg.hourglass.extensions

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import tmg.hourglass.widget.bar.dark.WidgetBarDarkProvider
import tmg.hourglass.widget.bar.light.WidgetBarLightProvider
import tmg.hourglass.widget.circle.dark.WidgetCircleDarkProvider
import tmg.hourglass.widget.circle.light.WidgetCircleLightProvider

fun Context.updateAllWidgets() {
    updateWidgets(WidgetBarLightProvider::class.java)
    updateWidgets(WidgetBarDarkProvider::class.java)
    updateWidgets(WidgetCircleLightProvider::class.java)
    updateWidgets(WidgetCircleDarkProvider::class.java)
}

fun <T : AppWidgetProvider> Context.updateWidgets(zClass: Class<T>) {

    val manager = AppWidgetManager.getInstance(this)
    val ids = manager.getAppWidgetIds(ComponentName(this, zClass))

    val intent = Intent(this, zClass)
    intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)

    sendBroadcast(intent)
}

fun <T : AppWidgetProvider> Context.updateWidget(zClass: Class<T>, widgetId: Int) {
    val intent = Intent(this, zClass)
    intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, IntArray(1) { widgetId })

    sendBroadcast(intent)
}