package tmg.hourglass.domain.connectors

import junit.framework.Assert.assertEquals
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.junit.jupiter.api.Test
import tmg.hourglass.domain.model.Countdown
import tmg.hourglass.domain.model.WidgetReference

class WidgetConnectorTest {

    private var invokations = 0

    private val fakeInstance = object : WidgetConnector {
        override fun saveSync(widgetReference: WidgetReference) {
            invokations += 1
        }
        override fun getSync(appWidgetId: Int) = null
    }

    @Test
    fun `save sync with individual components builds model`() {
        fakeInstance.saveSync(1, "mockId")
        assertEquals(1, invokations)
    }
}