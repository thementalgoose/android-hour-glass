package tmg.hourglass.domain.utils

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tmg.hourglass.domain.enums.CountdownType
import tmg.hourglass.domain.model
import tmg.hourglass.domain.model.Countdown
import tmg.hourglass.domain.model.CountdownNotifications
import java.time.LocalDateTime

class NotificationTriggerUtilsTest {

    @Test
    fun `calculateTriggerTime for AtTime returns exact notification time`() {
        val time = LocalDateTime.of(2026, 11, 1, 10, 0)
        val countdown = Countdown.Static.model()
        val notification = CountdownNotifications.AtTime(id = "n-1", time = time)

        val triggerTime = NotificationTriggerUtils.calculateTriggerTime(countdown, notification)

        assertEquals(time, triggerTime)
    }

    @Test
    fun `calculateTriggerTime for AtValue interpolates midpoint date for 50 percent progress`() {
        val startDateTime = LocalDateTime.of(2026, 1, 1, 0, 0)
        val endDateTime = LocalDateTime.of(2026, 1, 11, 0, 0)

        val countdown = Countdown.Static.model(
            start = "2026-01-01",
            end = "2026-01-11",
            startValue = "200",
            endValue = "500",
            countdownType = CountdownType.NUMBER
        )
        val notification = CountdownNotifications.AtValue(id = "n-2", value = "350")

        val triggerTime = NotificationTriggerUtils.calculateTriggerTime(countdown, notification)

        val expectedMidpoint = LocalDateTime.of(2026, 1, 6, 0, 0)
        assertEquals(expectedMidpoint, triggerTime)
    }

    @Test
    fun `calculateTriggerTime for AtValue below startValue returns startDate`() {
        val countdown = Countdown.Static.model(
            start = "2026-01-01",
            end = "2026-01-11",
            startValue = "200",
            endValue = "500"
        )
        val notification = CountdownNotifications.AtValue(id = "n-3", value = "100")

        val triggerTime = NotificationTriggerUtils.calculateTriggerTime(countdown, notification)

        assertEquals(countdown.startDate, triggerTime)
    }

    @Test
    fun `calculateTriggerTime for AtValue above endValue returns endDate`() {
        val countdown = Countdown.Static.model(
            start = "2026-01-01",
            end = "2026-01-11",
            startValue = "200",
            endValue = "500"
        )
        val notification = CountdownNotifications.AtValue(id = "n-4", value = "600")

        val triggerTime = NotificationTriggerUtils.calculateTriggerTime(countdown, notification)

        assertEquals(countdown.endDate, triggerTime)
    }
}
