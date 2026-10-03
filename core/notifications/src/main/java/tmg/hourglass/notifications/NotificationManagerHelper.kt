package tmg.hourglass.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import tmg.hourglass.domain.schedulers.NotificationSchedulerImpl
import tmg.hourglass.strings.R as StringsR
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationManagerHelper @Inject constructor(
    @ApplicationContext private val context: Context,
    private val subtitleFormatter: AtTimeSubtitleFormatter
) {
    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = context.getString(StringsR.string.notification_channel_name)
            val channel = NotificationChannel(
                CHANNEL_ID,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = context.getString(StringsR.string.notification_channel_description)
            }
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
            notificationManager?.createNotificationChannel(channel)
        }
    }

    fun showNotification(
        countdownId: String,
        countdownName: String,
        notificationId: String,
        notificationType: String?,
        notificationValue: String?,
        daysRemaining: Int? = null,
        message: String? = null
    ) {
        createNotificationChannel()

        val titleText = countdownName.ifBlank { context.getString(StringsR.string.placeholder_title) }
        val bodyText = when (notificationType) {
            NotificationSchedulerImpl.TYPE_TIME -> {
                if (daysRemaining != null) {
                    subtitleFormatter.format(context.resources, daysRemaining)
                } else {
                    message ?: context.getString(StringsR.string.notification_subtitle_milestone_reached, titleText)
                }
            }
            NotificationSchedulerImpl.TYPE_VALUE -> {
                if (!notificationValue.isNullOrBlank()) {
                    context.getString(StringsR.string.notification_subtitle_at_value, titleText, notificationValue)
                } else {
                    message ?: context.getString(StringsR.string.notification_subtitle_milestone_reached, titleText)
                }
            }
            else -> message ?: context.getString(StringsR.string.notification_subtitle_milestone_reached, titleText)
        }

        val launchIntent = context.packageManager.getLaunchIntentForPackage(context.packageName) ?: Intent()
        val pendingIntent = PendingIntent.getActivity(
            context,
            countdownId.hashCode(),
            launchIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val appIcon = R.drawable.notification_icon

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(appIcon)
            .setContentTitle(titleText)
            .setContentText(bodyText)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        val notificationManager = NotificationManagerCompat.from(context)
        try {
            notificationManager.notify((countdownId + "_" + notificationId).hashCode(), notification)
        } catch (e: SecurityException) {
            // Permission POST_NOTIFICATIONS missing on Android 13+ if user revoked it
            e.printStackTrace()
        }
    }

    companion object {
        const val CHANNEL_ID = "countdown_notifications"
        const val CHANNEL_NAME = "Countdown Notifications"
    }
}
