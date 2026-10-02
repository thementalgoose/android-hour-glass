package tmg.hourglass.domain.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class CountdownNotificationTest {

    @Test
    fun `AtTime creates correctly with id and time`() {
        val time = LocalDateTime.of(2026, 10, 2, 12, 0)
        val notification = CountdownNotifications.AtTime(id = "notif-1", time = time)

        assertEquals("notif-1", notification.id)
        assertEquals(time, notification.time)
        assertTrue(notification is CountdownNotification)
    }

    @Test
    fun `AtValue creates correctly with id and value`() {
        val notification = CountdownNotifications.AtValue(id = "notif-2", value = "350")

        assertEquals("notif-2", notification.id)
        assertEquals("350", notification.value)
        assertTrue(notification is CountdownNotification)
    }
}
