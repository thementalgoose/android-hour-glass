package tmg.hourglass

import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import tmg.hourglass.presentation.DashboardActivity

class BasicAppLaunchTest {

    @JvmField
    @RegisterExtension
    val composeTestRule = createAndroidComposeRule<DashboardActivity>()

    @Test
    fun appLaunch_dismissOnboarding_homepageVisible() {
        composeTestRule.waitUntil(timeoutMillis = 30_000) {
            composeTestRule
                .onAllNodesWithText("HourGlass", substring = true)
                .fetchSemanticsNodes()
                .isNotEmpty()
        }
    }
}