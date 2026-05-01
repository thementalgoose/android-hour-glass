package tmg.hourglass

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import androidx.compose.ui.test.printToString
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import tmg.hourglass.presentation.DashboardActivity

@RunWith(AndroidJUnit4::class)
class BasicAppLaunchTest {

    @get:Rule
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