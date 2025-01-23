package tmg.hourglass.presentation

import androidx.navigation.NavDestination
import app.cash.turbine.test
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
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


    @ParameterizedTest
    @EnumSource(Tab::class)
    fun `selecting tab dispatches navigation event`(tab: Tab) {
        initUnderTest()
        underTest.selectTab(tab)

        verify {
            mockNavigationController.navigate(tab.expectedNavigationDestination)
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