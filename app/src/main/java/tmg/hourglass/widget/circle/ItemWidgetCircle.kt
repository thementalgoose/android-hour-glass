package tmg.hourglass.widget.circle

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.annotation.LayoutRes
import androidx.core.graphics.toColorInt
import io.realm.exceptions.RealmMigrationNeededException
import org.threeten.bp.LocalDateTime
import tmg.hourglass.BuildConfig
import tmg.hourglass.R
import tmg.hourglass.extensions.format
import tmg.hourglass.prefs.AppPreferencesManager
import tmg.hourglass.prefs.PreferencesManager
import tmg.hourglass.realm.connectors.RealmCountdownConnector
import tmg.hourglass.realm.connectors.RealmWidgetConnector
import tmg.hourglass.utils.ProgressUtils
import tmg.hourglass.widget.setProgressBarColor
import kotlin.math.floor

inline fun <reified T: AppWidgetProvider> AppWidgetProvider.onUpdateCircle(
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
            val countdownModel = RealmWidgetConnector(RealmCountdownConnector()).getCountdownModelSync(widgetId)
            if (countdownModel != null) {


                val start = countdownModel.initial.toIntOrNull() ?: 0
                val end = countdownModel.finishing.toIntOrNull() ?: 100
                val progress = ProgressUtils.getProgress(countdownModel.startByType, countdownModel.endByType, interpolator = countdownModel.interpolator)

                val label =
                    countdownModel.countdownType.converter(floor((start + (progress * (end - start)))).toInt().toString())

                remoteView.setTextViewText(R.id.value, label)

                remoteView.setProgressBar(R.id.progressBar, 100, (progress * 100.0f).toInt(), false)
                remoteView.setProgressBarColor(R.id.progressBar, countdownModel.colour.toColorInt())

                when {
                    progress >= 1.0f -> {
                        remoteView.setTextViewText(R.id.value, context.getString(R.string.widget_finished_short))
                    }
                    progress <= 0.0f -> {
                        remoteView.setTextViewText(R.id.value, context.getString(R.string.widget_not_started_short))
                    }
                }
            }
            else {
                remoteView.setProgressBar(R.id.progress, 100, 0, false)
                remoteView.setTextViewText(R.id.value, context.getString(R.string.widget_migration_needed_label_short))
            }
        } catch (e: RealmMigrationNeededException) {
            remoteView.setProgressBar(R.id.progress, 100, 0, false)
            remoteView.setTextViewText(R.id.value, context.getString(R.string.widget_migration_needed_label_short))
        }

        val intent = Intent(context, T::class.java)
        intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds)
        val pendingIntent =
            PendingIntent.getBroadcast(context, widgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        remoteView.setOnClickPendingIntent(R.id.container, pendingIntent)
        appWidgetManager?.updateAppWidget(widgetId, remoteView)
    }
}