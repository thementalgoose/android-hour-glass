package tmg.hourglass.room.mappers

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tmg.hourglass.domain.model.CountdownNotifications
import tmg.hourglass.room.models.CountdownWrapper
import tmg.hourglass.room.models.Notification
import java.time.LocalDateTime

class CountdownMapperTest {

    private val tagMapper = TagMapper()
    private val notificationMapper = NotificationMapper()
    private val countdownMapper = CountdownMapper(tagMapper, notificationMapper)

    @Test
    fun `deserialize maps notifications list correctly`() {
        val countdownEntity = tmg.hourglass.room.models.Countdown(
            id = "cd-1",
            name = "Test Countdown",
            description = "Description",
            colour = "#FFFFFF",
            start = "2026-01-01",
            end = "2026-12-31",
            initial = "0",
            finishing = "100",
            passageType = "DAYS",
            isRecurring = false,
            interpolator = "LINEAR",
            tagId = null
        )
        val notifEntity1 = Notification(
            id = "n-1",
            countdownId = "cd-1",
            type = Notification.TYPE_TIME,
            time = "2026-06-01",
            value = null
        )
        val notifEntity2 = Notification(
            id = "n-2",
            countdownId = "cd-1",
            type = Notification.TYPE_VALUE,
            time = null,
            value = "50"
        )
        val countdownWrapper = CountdownWrapper(
            countdown = countdownEntity,
            tag = null,
            notifications = listOf(notifEntity1, notifEntity2)
        )

        val result = countdownMapper.deserialize(countdownWrapper)

        assertEquals(2, result.notifications.size)
        val n1 = result.notifications[0] as CountdownNotifications.AtTime
        assertEquals("n-1", n1.id)
        assertEquals(LocalDateTime.of(2026, 6, 1, 0, 0), n1.time)

        val n2 = result.notifications[1] as CountdownNotifications.AtValue
        assertEquals("n-2", n2.id)
        assertEquals("50", n2.value)
    }

    @Test
    fun `serialize and deserialize maps emoji correctly`() {
        val domainModel = tmg.hourglass.domain.model.Countdown.Static(
            id = "cd-1",
            name = "Test",
            description = "Desc",
            colour = "#FFFFFF",
            emoji = "🎯",
            start = "2026-01-01",
            end = "2026-12-31",
            startValue = "0",
            endValue = "100",
            countdownType = tmg.hourglass.domain.enums.CountdownType.DAYS,
            tag = null
        )
        val entity = countdownMapper.serialize(domainModel)
        assertEquals("🎯", entity.emoji)

        val wrapper = CountdownWrapper(countdown = entity, tag = null, notifications = emptyList())
        val deserialized = countdownMapper.deserialize(wrapper)
        assertEquals("🎯", deserialized.emoji)
    }
}
