package tmg.hourglass.widget

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class WidgetUtilsTest {

    @Test
    fun `WidgetSquareProgress getImageFor returns correct image for given progress`() {

        val first = WidgetSquareProgress.values().first()
        assertEquals(first, WidgetSquareProgress.getImageFor(0.0f))

        val middle = WidgetSquareProgress.values()[(WidgetSquareProgress.values().size / 2).toInt()]
        assertEquals(middle, WidgetSquareProgress.getImageFor(0.5f))

        val last = WidgetSquareProgress.values().last()
        assertEquals(last, WidgetSquareProgress.getImageFor(1.0f))
    }

    @Test
    fun `WidgetCircleProgress getImageFor returns correct image for given progress`() {

        val first = WidgetCircleProgress.values().first()
        assertEquals(first, WidgetCircleProgress.getImageFor(0.0f))

        val middle = WidgetCircleProgress.values()[(WidgetCircleProgress.values().size / 2).toInt()]
        assertEquals(middle, WidgetCircleProgress.getImageFor(0.5f))

        val last = WidgetCircleProgress.values().last()
        assertEquals(last, WidgetCircleProgress.getImageFor(1.0f))
    }
}