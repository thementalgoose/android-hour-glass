package tmg.hourglass.settings.release

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.hourglass.releaseNotes
import tmg.hourglass.testutils.BaseTest
import tmg.hourglass.testutils.assertEventFired
import tmg.hourglass.testutils.test

internal class ReleaseViewModelTest: BaseTest() {

    lateinit var sut: ReleaseViewModel

    @BeforeEach
    internal fun setUp() {
        sut = ReleaseViewModel()
    }

    @Test
    fun `ReleaseViewModel content is set to all release notes when vm is initialised`() {

        val expected = releaseNotes.values.toList()

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