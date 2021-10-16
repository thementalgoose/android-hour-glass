package tmg.hourglass.settings.privacy

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.testutils.BaseTest
import tmg.testutils.livedata.assertEventFired
import tmg.testutils.livedata.test

internal class PrivacyPolicyViewModelTest: BaseTest() {

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
}