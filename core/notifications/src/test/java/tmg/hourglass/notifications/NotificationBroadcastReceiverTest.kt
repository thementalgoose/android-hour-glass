package tmg.hourglass.notifications

import android.content.Context
import android.content.Intent
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import tmg.hourglass.domain.schedulers.NotificationSchedulerImpl

class NotificationBroadcastReceiverTest {

    private val notificationManagerHelper: NotificationManagerHelper = mockk(relaxed = true)
    private val receiver = NotificationBroadcastReceiver().apply {
        this.notificationManagerHelper = this@NotificationBroadcastReceiverTest.notificationManagerHelper
    }

    private val context: Context = mockk(relaxed = true)

    @Test
    fun `processIntent with matching action triggers notification display`() {
        val intent: Intent = mockk(relaxed = true) {
            every { action } returns NotificationSchedulerImpl.ACTION_TRIGGER_NOTIFICATION
            every { getStringExtra(NotificationSchedulerImpl.EXTRA_COUNTDOWN_ID) } returns "cd-123"
            every { getStringExtra(NotificationSchedulerImpl.EXTRA_COUNTDOWN_NAME) } returns "My Countdown"
            every { getStringExtra(NotificationSchedulerImpl.EXTRA_NOTIFICATION_ID) } returns "n-456"
            every { getStringExtra(NotificationSchedulerImpl.EXTRA_NOTIFICATION_TYPE) } returns "TIME"
            every { getStringExtra(NotificationSchedulerImpl.EXTRA_NOTIFICATION_VALUE) } returns null
            every { hasExtra(NotificationSchedulerImpl.EXTRA_DAYS_REMAINING) } returns true
            every { getIntExtra(NotificationSchedulerImpl.EXTRA_DAYS_REMAINING, 0) } returns 7
            every { getStringExtra(NotificationSchedulerImpl.EXTRA_MESSAGE) } returns "My Countdown notification"
        }

        receiver.processIntent(context, intent)

        verify(exactly = 1) {
            notificationManagerHelper.showNotification(
                countdownId = "cd-123",
                countdownName = "My Countdown",
                notificationId = "n-456",
                notificationType = "TIME",
                notificationValue = null,
                daysRemaining = 7,
                message = "My Countdown notification"
            )
        }
    }

    @Test
    fun `processIntent with mismatched action does not trigger notification`() {
        val intent: Intent = mockk(relaxed = true) {
            every { action } returns "SOME_OTHER_ACTION"
        }

        receiver.processIntent(context, intent)

        verify(exactly = 0) {
            notificationManagerHelper.showNotification(any(), any(), any(), any(), any(), any(), any())
        }
    }
}
