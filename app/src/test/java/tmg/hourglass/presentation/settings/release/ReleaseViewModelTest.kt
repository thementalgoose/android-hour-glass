package tmg.hourglass.presentation.settings.release

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.hourglass.ReleaseNotes
import tmg.testutils.BaseTest
import tmg.testutils.livedata.assertEventFired
import tmg.testutils.livedata.test

internal class ReleaseViewModelTest: BaseTest() {

    lateinit var sut: ReleaseViewModel

    @BeforeEach
    internal fun setUp() {
        sut = ReleaseViewModel()
    }

    @Test
    fun `ReleaseViewModel content is set to all release notes when vm is initialised`() {

        val expected = ReleaseNotes.values().reversed().toList()

        sut.outputs.content.test {
            assertValue(expected)
        }
    }

    @Test
    fun `ReleaseViewModel clicking back fires back event`() {

        sut.inputs.clickBack()

        sut.outputs.goBack.test {
            assertEventFired()
        }
    }
}