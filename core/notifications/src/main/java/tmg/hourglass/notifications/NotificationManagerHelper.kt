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
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationManagerHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notifications for HourGlass countdown milestones"
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
        message: String?
    ) {
        createNotificationChannel()

        val titleText = countdownName.ifBlank { "Countdown Notification" }
        val bodyText = message ?: when (notificationType) {
            "VALUE" -> "$titleText reached $notificationValue"
            else -> "$titleText milestone reached"
        }

        val launchIntent = context.packageManager.getLaunchIntentForPackage(context.packageName) ?: Intent()
        val pendingIntent = PendingIntent.getActivity(
            context,
            countdownId.hashCode(),
            launchIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_popup_reminder)
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
