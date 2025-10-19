package tmg.hourglass.domain.connectors

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tmg.hourglass.domain.model.WidgetReference
import tmg.hourglass.domain.repositories.WidgetRepository

class WidgetConnectorTest {

    private var invokations = 0

    private val fakeInstance = object : WidgetRepository {
        override fun saveSync(widgetReference: WidgetReference) {
            invokations += 1
        }
        override fun getSync(appWidgetId: Int) = null
        override fun get(appWidgetId: Int): Flow<WidgetReference?> {
            return flow { emit(null) }
        }
    }

    @Test
    fun `save sync with individual components builds model`() {
        fakeInstance.saveSync(1, "mockId")
        assertEquals(1, invokations)
    }
}