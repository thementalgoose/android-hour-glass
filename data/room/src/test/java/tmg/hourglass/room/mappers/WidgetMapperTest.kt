package tmg.hourglass.room.mappers

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import tmg.hourglass.domain.model.WidgetReference

class WidgetMapperTest {

    private val underTest = WidgetMapper()

    @Test
    fun `serialize Single maps to room model with countdownId and null tagId`() {
        val domainModel = WidgetReference.Single(
            appWidgetId = 42,
            countdownId = "countdown-1",
            openAppOnClick = true
        )

        val result = underTest.serialize(domainModel)

        assertEquals(42, result.appWidgetId)
        assertEquals("countdown-1", result.countdownId)
        assertEquals(null, result.tagId)
        assertEquals(true, result.openAppOnClick)
    }

    @Test
    fun `serialize Multiple maps to room model with null countdownId and tagId`() {
        val domainModel = WidgetReference.Multiple(
            appWidgetId = 43,
            tagId = "tag-1",
            openAppOnClick = false
        )

        val result = underTest.serialize(domainModel)

        assertEquals(43, result.appWidgetId)
        assertEquals(null, result.countdownId)
        assertEquals("tag-1", result.tagId)
        assertEquals(false, result.openAppOnClick)
    }

    @Test
    fun `deserialize with countdownId returns WidgetReference Single`() {
        val roomModel = tmg.hourglass.room.models.WidgetReference(
            appWidgetId = 42,
            countdownId = "countdown-1",
            tagId = null,
            openAppOnClick = true
        )

        val result = underTest.deserialize(roomModel)

        assertTrue(result is WidgetReference.Single)
        val single = result as WidgetReference.Single
        assertEquals(42, single.appWidgetId)
        assertEquals("countdown-1", single.countdownId)
        assertEquals(true, single.openAppOnClick)
    }

    @Test
    fun `deserialize with null countdownId returns WidgetReference Multiple`() {
        val roomModel = tmg.hourglass.room.models.WidgetReference(
            appWidgetId = 43,
            countdownId = null,
            tagId = "tag-1",
            openAppOnClick = false
        )

        val result = underTest.deserialize(roomModel)

        assertTrue(result is WidgetReference.Multiple)
        val multiple = result as WidgetReference.Multiple
        assertEquals(43, multiple.appWidgetId)
        assertEquals("tag-1", multiple.tagId)
        assertEquals(false, multiple.openAppOnClick)
    }
}
