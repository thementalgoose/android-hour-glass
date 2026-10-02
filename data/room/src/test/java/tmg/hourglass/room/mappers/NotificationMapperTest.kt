package tmg.hourglass.room.mappers

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import tmg.hourglass.domain.model.Countdown
import tmg.hourglass.domain.model.CountdownNotifications
import tmg.hourglass.room.models.Notification
import java.time.LocalDateTime

class NotificationMapperTest {

    private val mapper = NotificationMapper()

    @Test
    fun `serialize AtTime correctly formats date to yyyy-MM-dd`() {
        val time = LocalDateTime.of(2026, 10, 2, 14, 30)
        val domain = CountdownNotifications.AtTime(id = "notif-1", time = time)

        val result = mapper.serialize(countdownId = "cd-1", model = domain)

        assertEquals("notif-1", result.id)
        assertEquals("cd-1", result.countdownId)
        assertEquals(Notification.TYPE_TIME, result.type)
        assertEquals("2026-10-02", result.time)
        assertNull(result.value)
    }

    @Test
    fun `serialize AtValue correctly populates value`() {
        val domain = CountdownNotifications.AtValue(id = "notif-2", value = "350")

        val result = mapper.serialize(countdownId = "cd-1", model = domain)

        assertEquals("notif-2", result.id)
        assertEquals("cd-1", result.countdownId)
        assertEquals(Notification.TYPE_VALUE, result.type)
        assertNull(result.time)
        assertEquals("350", result.value)
    }

    @Test
    fun `deserialize Notification TYPE_TIME parses yyyy-MM-dd date`() {
        val entity = Notification(
            id = "notif-1",
            countdownId = "cd-1",
            type = Notification.TYPE_TIME,
            time = "2026-10-02",
            value = null
        )

        val result = mapper.deserialize(entity)

        assertTrue(result is CountdownNotifications.AtTime)
        val atTime = result as CountdownNotifications.AtTime
        assertEquals("notif-1", atTime.id)
        assertEquals(LocalDateTime.of(2026, 10, 2, 0, 0), atTime.time)
    }

    @Test
    fun `deserialize Notification TYPE_VALUE parses value`() {
        val entity = Notification(
            id = "notif-2",
            countdownId = "cd-1",
            type = Notification.TYPE_VALUE,
            time = null,
            value = "350"
        )

        val result = mapper.deserialize(entity)

        assertTrue(result is CountdownNotifications.AtValue)
        val atValue = result as CountdownNotifications.AtValue
        assertEquals("notif-2", atValue.id)
        assertEquals("350", atValue.value)
    }
}
