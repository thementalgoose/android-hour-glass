package tmg.hourglass.navigation

import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import tmg.hourglass.core.googleanalytics.CrashReporter

internal class AppNavigationControllerTest {

    private val mockCrashReporter: CrashReporter = mockk(relaxed = true)
    private val mockNavHostController: NavHostController = mockk(relaxed = true)

    private lateinit var underTest: AppNavigationController

    private fun initUnderTest() {
        underTest = AppNavigationController(
            crashReporter = mockCrashReporter
        )
        underTest.navHostController = mockNavHostController
    }

    @Test
    fun `navigate to a destination calls navigate and logs`() {
        initUnderTest()
        underTest.navigate(Screen.Home)

        verify {
            mockCrashReporter.log("Navigating to home")
            mockNavHostController.navigate("home", anyNullable<NavOptionsBuilder.() -> Unit>())
        }
    }
}