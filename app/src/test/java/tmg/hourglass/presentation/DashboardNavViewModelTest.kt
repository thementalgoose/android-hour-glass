package tmg.hourglass.presentation

import app.cash.turbine.test
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tmg.hourglass.navigation.Screen
import tmg.hourglass.presentation.navigation.NavigationController
import tmg.hourglass.presentation.navigation.NavigationDestination

internal class DashboardNavViewModelTest {

    private val mockNavigationController: NavigationController = mockk(relaxed = true)

    private lateinit var underTest: DashboardNavViewModel

    private fun initUnderTest() {
        underTest = DashboardNavViewModel(
            navigationController = mockNavigationController
        )
    }

    private val Tab.expectedNavigationDestination: NavigationDestination
        get() = when (this) {
            Tab.DASHBOARD -> Screen.Home
            Tab.SETTINGS -> Screen.Settings
        }


    @Test
    fun `selecting tab dispatches navigation event`() {
        initUnderTest()
        underTest.selectTab(Tab.SETTINGS)

        verify {
            mockNavigationController.navigate(Tab.SETTINGS.expectedNavigationDestination)
        }
    }

    @Test
    fun `destination changing updates state`() = runTest {
        initUnderTest()
        underTest.uiState.test {
            underTest.onDestinationChanged(
                controller = mockk(),
                destination = mockk {
                    every { route } returns Screen.Settings.route
                },
                arguments = null
            )

            assertEquals(Tab.DASHBOARD, awaitItem().tab)
            assertEquals(Tab.SETTINGS, awaitItem().tab)
        }
    }
}