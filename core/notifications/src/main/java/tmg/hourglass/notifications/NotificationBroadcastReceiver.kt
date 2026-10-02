package tmg.hourglass.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import tmg.hourglass.domain.schedulers.NotificationSchedulerImpl
import javax.inject.Inject

@AndroidEntryPoint
class NotificationBroadcastReceiver : BroadcastReceiver() {

    @Inject
    lateinit var notificationManagerHelper: NotificationManagerHelper

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return
        processIntent(context, intent)
    }

    fun processIntent(context: Context, intent: Intent) {
        val action = intent.action
        if (action == NotificationSchedulerImpl.ACTION_TRIGGER_NOTIFICATION || action == ACTION_TRIGGER_NOTIFICATION) {
            val countdownId = intent.getStringExtra(NotificationSchedulerImpl.EXTRA_COUNTDOWN_ID) ?: ""
            val countdownName = intent.getStringExtra(NotificationSchedulerImpl.EXTRA_COUNTDOWN_NAME) ?: ""
            val notificationId = intent.getStringExtra(NotificationSchedulerImpl.EXTRA_NOTIFICATION_ID) ?: ""
            val notificationType = intent.getStringExtra(NotificationSchedulerImpl.EXTRA_NOTIFICATION_TYPE)
            val notificationValue = intent.getStringExtra(NotificationSchedulerImpl.EXTRA_NOTIFICATION_VALUE)
            val message = intent.getStringExtra(NotificationSchedulerImpl.EXTRA_MESSAGE)

            notificationManagerHelper.showNotification(
                countdownId = countdownId,
                countdownName = countdownName,
                notificationId = notificationId,
                notificationType = notificationType,
                notificationValue = notificationValue,
                message = message
            )
        }
    }

    companion object {
        const val ACTION_TRIGGER_NOTIFICATION = "tmg.hourglass.notifications.TRIGGER_NOTIFICATION"
    }
}
