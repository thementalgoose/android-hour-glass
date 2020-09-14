package tmg.hourglass.settings.privacy

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.hourglass.testutils.BaseTest
import tmg.hourglass.testutils.assertEventFired

@FlowPreview
@ExperimentalCoroutinesApi
class PrivacyPolicyViewModelTest: BaseTest() {

    lateinit var sut: PrivacyPolicyViewModel

    @BeforeEach
    internal fun setUp() {

        sut = PrivacyPolicyViewModel()
    }

    @Test
    fun `PrivacyPolicyViewModel click back fires go back event`() {

        sut.inputs.clickBack()

        assertEventFired(sut.outputs.goBack)
    }

    @AfterEach
    internal fun tearDown() {

    }
}