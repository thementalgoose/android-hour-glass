package tmg.hourglass.settings.privacy

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.hourglass.testutils.BaseTest
import tmg.hourglass.testutils.assertEventFired
import tmg.hourglass.testutils.test

class PrivacyPolicyViewModelTest: BaseTest() {

    lateinit var sut: PrivacyPolicyViewModel

    @BeforeEach
    internal fun setUp() {

        sut = PrivacyPolicyViewModel()
    }

    @Test
    fun `PrivacyPolicyViewModel click back fires go back event`() {

        sut.inputs.clickBack()

        sut.outputs.goBack.test {
            assertEventFired()
        }
    }

    @AfterEach
    internal fun tearDown() {

    }
}