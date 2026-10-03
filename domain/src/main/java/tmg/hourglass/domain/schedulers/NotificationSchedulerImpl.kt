package tmg.hourglass.domain.schedulers

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import dagger.hilt.android.qualifiers.ApplicationContext
import tmg.hourglass.domain.model.Countdown
import tmg.hourglass.domain.model.CountdownNotifications
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import javax.inject.Inject

class NotificationSchedulerImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
) : NotificationScheduler {

    override fun scheduleNotification(
        countdown: Countdown,
        notification: CountdownNotifications,
        triggerAt: LocalDateTime
    ) {
        val triggerAtMillis = triggerAt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        if (triggerAtMillis <= System.currentTimeMillis()) {
            return
        }

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return

        val intent = Intent(ACTION_TRIGGER_NOTIFICATION).apply {
            setPackage(context.packageName)
            putExtra(EXTRA_COUNTDOWN_ID, countdown.id)
            putExtra(EXTRA_COUNTDOWN_NAME, countdown.name)
            putExtra(EXTRA_NOTIFICATION_ID, notification.id)
            when (notification) {
                is CountdownNotifications.AtTime -> {
                    val daysRemaining = ChronoUnit.DAYS.between(
                        triggerAt.toLocalDate(),
                        countdown.endDate.toLocalDate()
                    ).toInt().coerceAtLeast(0)
                    putExtra(EXTRA_NOTIFICATION_TYPE, TYPE_TIME)
                    putExtra(EXTRA_DAYS_REMAINING, daysRemaining)
                    putExtra(EXTRA_MESSAGE, "${countdown.name} notification")
                }
                is CountdownNotifications.AtValue -> {
                    putExtra(EXTRA_NOTIFICATION_TYPE, TYPE_VALUE)
                    putExtra(EXTRA_NOTIFICATION_VALUE, notification.value)
                    putExtra(EXTRA_MESSAGE, "${countdown.name} reached ${notification.value}")
                }
            }
        }

        val requestCode = (countdown.id + "_" + notification.id).hashCode()
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
                alarmManager.setAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerAtMillis,
                    pendingIntent
                )
            } else {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerAtMillis,
                    pendingIntent
                )
            }
        } catch (_: SecurityException) {
            alarmManager.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerAtMillis,
                pendingIntent
            )
        }
    }

    override fun cancelNotification(countdownId: String, notificationId: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        val intent = Intent(ACTION_TRIGGER_NOTIFICATION).apply {
            setPackage(context.packageName)
        }
        val requestCode = (countdownId + "_" + notificationId).hashCode()
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
            pendingIntent.cancel()
        }
    }

    override fun cancelAll() {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            alarmManager.cancelAll()
        }
    }

    companion object {
        const val ACTION_TRIGGER_NOTIFICATION = "tmg.hourglass.notifications.TRIGGER_NOTIFICATION"
        const val EXTRA_COUNTDOWN_ID = "extra_countdown_id"
        const val EXTRA_COUNTDOWN_NAME = "extra_countdown_name"
        const val EXTRA_NOTIFICATION_ID = "extra_notification_id"
        const val EXTRA_NOTIFICATION_TYPE = "extra_notification_type"
        const val EXTRA_NOTIFICATION_VALUE = "extra_notification_value"
        const val EXTRA_DAYS_REMAINING = "extra_days_remaining"
        const val EXTRA_MESSAGE = "extra_message"

        const val TYPE_TIME = "TIME"
        const val TYPE_VALUE = "VALUE"
    }
}
