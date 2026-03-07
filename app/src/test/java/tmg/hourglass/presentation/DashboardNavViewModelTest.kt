package tmg.hourglass.presentation

import app.cash.turbine.test
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class DashboardNavViewModelTest {

    private lateinit var underTest: DashboardNavViewModel

    private fun initUnderTest() {
        underTest = DashboardNavViewModel()
    }

    @Test
    fun `destination changing updates state`() = runTest {
        initUnderTest()
        underTest.uiState.test {

            underTest.selectTab(Tab.DASHBOARD)
            assertEquals(Tab.DASHBOARD, awaitItem().tab)

            underTest.selectTab(Tab.SETTINGS)
            assertEquals(Tab.SETTINGS, awaitItem().tab)
        }
    }
}