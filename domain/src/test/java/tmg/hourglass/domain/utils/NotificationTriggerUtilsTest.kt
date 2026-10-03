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

    @Test
    fun `sortChronologically sorts AtTime notifications in chronological order`() {
        val endDate = LocalDateTime.of(2026, 12, 31, 0, 0)
        val countdown = Countdown.Static.model(end = "2026-12-31")

        val n1 = CountdownNotifications.AtTime(id = "1", time = endDate.minusDays(2))
        val n2 = CountdownNotifications.AtTime(id = "2", time = endDate.minusDays(10))
        val n3 = CountdownNotifications.AtTime(id = "3", time = endDate.minusDays(5))

        val sorted = NotificationTriggerUtils.sortChronologically(countdown, listOf(n1, n2, n3))

        assertEquals(listOf(n2, n3, n1), sorted)
    }

    @Test
    fun `sortChronologically sorts AtValue notifications in chronological order for count up`() {
        val countdown = Countdown.Static.model(
            start = "2026-01-01",
            end = "2026-12-31",
            startValue = "0",
            endValue = "100",
            countdownType = CountdownType.NUMBER
        )

        val n1 = CountdownNotifications.AtValue(id = "1", value = "80")
        val n2 = CountdownNotifications.AtValue(id = "2", value = "20")
        val n3 = CountdownNotifications.AtValue(id = "3", value = "50")

        val sorted = NotificationTriggerUtils.sortChronologically(countdown, listOf(n1, n2, n3))

        assertEquals(listOf(n2, n3, n1), sorted)
    }

    @Test
    fun `sortChronologically sorts AtValue notifications in chronological order for count down`() {
        val countdown = Countdown.Static.model(
            start = "2026-01-01",
            end = "2026-12-31",
            startValue = "100",
            endValue = "0",
            countdownType = CountdownType.NUMBER
        )

        val n1 = CountdownNotifications.AtValue(id = "1", value = "20")
        val n2 = CountdownNotifications.AtValue(id = "2", value = "80")
        val n3 = CountdownNotifications.AtValue(id = "3", value = "50")

        val sorted = NotificationTriggerUtils.sortChronologically(countdown, listOf(n1, n2, n3))

        assertEquals(listOf(n2, n3, n1), sorted)
    }
}
