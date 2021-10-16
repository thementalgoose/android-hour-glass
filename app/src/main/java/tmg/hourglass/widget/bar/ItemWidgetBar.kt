package tmg.hourglass.widget.bar

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.RemoteViews
import androidx.annotation.LayoutRes
import io.realm.exceptions.RealmMigrationNeededException
import org.threeten.bp.LocalDateTime
import tmg.hourglass.BuildConfig
import tmg.hourglass.R
import tmg.hourglass.prefs.AppPreferencesManager
import tmg.hourglass.prefs.PreferencesManager
import tmg.hourglass.realm.connectors.RealmCountdownConnector
import tmg.hourglass.realm.connectors.RealmWidgetConnector
import tmg.hourglass.realm.mappers.RealmCountdownMapper
import tmg.hourglass.realm.mappers.RealmWidgetMapper
import tmg.hourglass.utils.ProgressUtils
import tmg.hourglass.widget.WidgetBarColours
import tmg.utilities.extensions.format
import kotlin.math.floor

inline fun <reified T : AppWidgetProvider> AppWidgetProvider.onUpdateBar(
    context: Context?,
    @LayoutRes layoutId: Int,
    appWidgetManager: AppWidgetManager?,
    appWidgetIds: IntArray?
) {

    for (x in 0 until (appWidgetIds?.size ?: 0)) {
        val widgetId: Int = appWidgetIds!![x]
        val prefs: PreferencesManager = AppPreferencesManager(context!!)
        val remoteView = RemoteViews(BuildConfig.APPLICATION_ID, layoutId)

        try {
            val countdownModel = RealmWidgetConnector(RealmCountdownConnector(RealmCountdownMapper()), RealmWidgetMapper()).getCountdownModelSync(widgetId)
            if (countdownModel != null) {
                remoteView.setTextViewText(R.id.title, countdownModel.name)

                val start = countdownModel.initial.toIntOrNull() ?: 0
                val end = countdownModel.finishing.toIntOrNull() ?: 100
                val progress = ProgressUtils.getProgress(countdownModel.startByType, countdownModel.endByType, interpolator = countdownModel.interpolator)

                val label =
                    countdownModel.countdownType.converter(floor((start + (progress * (end - start)))).toInt().toString())

                // Text
                remoteView.setTextViewText(R.id.value, label)

                // Progress Bar
                val selectedProgressBar = WidgetBarColours.values().firstOrNull { it.colour == countdownModel.colour } ?: WidgetBarColours.COLOUR_DEFAULT
                if (BuildConfig.DEBUG) {
                    Log.i("Hourglass", "Colour is ${countdownModel.colour} $selectedProgressBar")
                }
                remoteView.setProgressBar(selectedProgressBar.viewId, 100, (progress * 100.0f).toInt(), false)
                remoteView.setViewVisibility(selectedProgressBar.viewId, View.VISIBLE)
                WidgetBarColours
                    .values()
                    .filter { it != selectedProgressBar }
                    .forEach {
                        remoteView.setViewVisibility(it.viewId, View.GONE)
                    }

                // Updated at
                remoteView.setViewVisibility(R.id.updatedAt, if (prefs.widgetShowUpdate) View.VISIBLE else View.GONE)
                when {
                    progress >= 1.0f -> {
                        remoteView.setTextViewText(R.id.updatedAt, context.getString(R.string.widget_finished))
                    }
                    progress <= 0.0f -> {
                        remoteView.setTextViewText(R.id.updatedAt, context.getString(R.string.widget_not_started))
                    }
                    else -> {
                        remoteView.setTextViewText(R.id.updatedAt, context.getString(R.string.widget_progress, LocalDateTime.now().format("HH:mm dd MMM yyyy")))
                    }
                }
            } else {
                remoteView.setTextViewText(R.id.title, context.getString(R.string.widget_error))
                remoteView.setTextViewText(R.id.value, context.getString(R.string.widget_value))
                remoteView.setTextViewText(R.id.updatedAt, "")
            }
        } catch (e: RealmMigrationNeededException) {
            remoteView.setTextViewText(R.id.title, context.getString(R.string.widget_migration_needed))
            remoteView.setTextViewText(R.id.value, context.getString(R.string.widget_migration_needed_value))
            remoteView.setTextViewText(R.id.updatedAt, context.getString(R.string.widget_migration_needed_label))
        }

        val intent = Intent(context, T::class.java)
        intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds)
        val pendingIntent =
            PendingIntent.getBroadcast(context, widgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        remoteView.setOnClickPendingIntent(R.id.container, pendingIntent)
        appWidgetManager?.updateAppWidget(widgetId, remoteView)
    }
}