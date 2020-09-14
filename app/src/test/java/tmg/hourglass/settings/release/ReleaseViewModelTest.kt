package tmg.hourglass.settings.release

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.hourglass.releaseNotes
import tmg.hourglass.testutils.BaseTest
import tmg.hourglass.testutils.assertEventFired
import tmg.hourglass.testutils.assertValue

@FlowPreview
@ExperimentalCoroutinesApi
class ReleaseViewModelTest: BaseTest() {

    lateinit var sut: ReleaseViewModel

    @BeforeEach
    internal fun setUp() {
        sut = ReleaseViewModel()
    }

    @Test
    fun `ReleaseViewModel content is set to all release notes when vm is initialised`() {

        val expected = releaseNotes.values.toList()
        assertValue(expected, sut.outputs.content)
    }

    @Test
    fun `ReleaseViewModel clicking back fires back event`() {

        sut.inputs.clickBack()

        assertEventFired(sut.outputs.goBack)
    }

    @AfterEach
    internal fun tearDown() {

    }
}