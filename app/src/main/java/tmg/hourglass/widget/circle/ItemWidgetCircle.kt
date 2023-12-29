package tmg.hourglass.widget.circle

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.annotation.LayoutRes
import io.realm.exceptions.RealmMigrationNeededException
import tmg.hourglass.BuildConfig
import tmg.hourglass.R
import tmg.hourglass.strings.R.string
import tmg.hourglass.prefs.AppPreferencesManager
import tmg.hourglass.prefs.PreferencesManager
import tmg.hourglass.realm.connectors.RealmCountdownConnector
import tmg.hourglass.realm.connectors.RealmWidgetConnector
import tmg.hourglass.realm.mappers.RealmCountdownMapper
import tmg.hourglass.realm.mappers.RealmCountdownNotificationMapper
import tmg.hourglass.realm.mappers.RealmWidgetMapper
import tmg.hourglass.widget.WidgetCircleProgress
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
            val connector = RealmWidgetConnector(
                countdownConnector = RealmCountdownConnector(
                    countdownMapper = RealmCountdownMapper(
                        realmCountdownNotificationMapper = RealmCountdownNotificationMapper()
                    ),
                    countdownNotificationMapper = RealmCountdownNotificationMapper()
                ),
                widgetMapper = RealmWidgetMapper()
            )
            val countdownModel = connector.getCountdownModelSync(widgetId)
            if (countdownModel != null) {

                val start = countdownModel.initial.toIntOrNull() ?: 0
                val end = countdownModel.finishing.toIntOrNull() ?: 100
                val progress = ProgressUtils.getProgress(countdownModel.startByType, countdownModel.endByType, interpolator = countdownModel.interpolator)

                val label =
                    countdownModel.countdownType.converter(floor((start + (progress * (end - start)))).toInt().toString())

                remoteView.setTextViewText(R.id.value, label)

                val circleImage = WidgetCircleProgress.getImageFor(progress)
                remoteView.setImageViewResource(R.id.progress, circleImage.res)

                when {
                    progress >= 1.0f -> {
                        remoteView.setImageViewResource(R.id.progress, R.drawable.widget_circle_check)
                    }
                    progress <= 0.0f -> {
                        remoteView.setTextViewText(R.id.value, context.getString(string.widget_not_started_short))
                    }
                }
            }
            else {
                remoteView.setProgressBar(R.id.progress, 100, 0, false)
                remoteView.setTextViewText(R.id.value, context.getString(string.widget_migration_needed_label_short))
            }
        } catch (e: RealmMigrationNeededException) {
            remoteView.setProgressBar(R.id.progress, 100, 0, false)
            remoteView.setTextViewText(R.id.value, context.getString(string.widget_migration_needed_label_short))
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