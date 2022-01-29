package tmg.hourglass.dashboard

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.hourglass.domain.connectors.CountdownConnector
import tmg.hourglass.domain.model.Countdown
import tmg.hourglass.domain.model.preview
import tmg.testutils.BaseTest
import tmg.testutils.livedata.assertDataEventValue
import tmg.testutils.livedata.assertEventFired
import tmg.testutils.livedata.test

internal class DashboardViewModelTest: BaseTest() {

    private val mockCountdownConnector: CountdownConnector = mockk(relaxed = true)

    private lateinit var sut: DashboardViewModel

    private val countdownModel = Countdown.preview()

    private fun initSUT() {
        sut = DashboardViewModel(mockCountdownConnector)
    }

    @BeforeEach
    internal fun setUp() {
        every { mockCountdownConnector.all() } returns flow { emit(listOf(countdownModel)) }
    }

    @Test
    fun `all content on home screen returns all from connnector`() = coroutineTest {
        initSUT()

        sut.upcoming.test {
            assertValue(listOf(countdownModel))
        }
    }

    @Test
    fun `click settings fires go to settings event`() {
        initSUT()

        sut.inputs.clickSettings()

        sut.goToSettings.test {
            assertEventFired()
        }
    }

    @Test
    fun `click add fires go to add event`() {
        initSUT()

        sut.inputs.clickAdd()

        sut.goToAdd.test {
            assertEventFired()
        }
    }

    @Test
    fun `click edit fires go to edit event`() {
        initSUT()

        sut.inputs.clickEdit("my-id")

        sut.goToEdit.test {
            assertDataEventValue("my-id")
        }
    }

    @Test
    fun `click delete marks item deleted in database connector`() {
        initSUT()

        sut.clickDelete("my-id")

        verify {
            mockCountdownConnector.delete("my-id")
        }
    }


}