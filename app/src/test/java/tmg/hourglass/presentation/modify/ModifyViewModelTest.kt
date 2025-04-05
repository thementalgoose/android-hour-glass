package tmg.hourglass.presentation.modify

import app.cash.turbine.test
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import tmg.hourglass.core.googleanalytics.CrashReporter
import tmg.hourglass.domain.connectors.CountdownConnector
import tmg.hourglass.domain.enums.CountdownType
import tmg.hourglass.presentation.modify.ModifyData.countdownDays
import tmg.hourglass.presentation.modify.ModifyData.countdownNumber
import tmg.hourglass.presentation.modify.ModifyData.tomorrow
import tmg.hourglass.presentation.modify.ModifyData.uiStateDays
import tmg.hourglass.presentation.modify.ModifyData.uiStateDaysEmpty
import tmg.hourglass.presentation.modify.ModifyData.uiStateNumber
import tmg.hourglass.presentation.modify.ModifyData.uiStateNumberEmpty

internal class ModifyViewModelTest {

    private val mockCountdownConnector: CountdownConnector = mockk(relaxed = true)
    private val mockCrashReporter: CrashReporter = mockk(relaxed = true)

    private lateinit var underTest: ModifyViewModel

    private fun initUnderTest() {
        underTest = ModifyViewModel(
            countdownConnector = mockCountdownConnector,
            crashReporter = mockCrashReporter
        )
        every { mockCountdownConnector.getSync(countdownDays.id) } returns countdownDays
        every { mockCountdownConnector.getSync(countdownNumber.id) } returns countdownNumber
    }

    @Test
    fun `initialise sets ui state with null countdown model`() = runTest {
        initUnderTest()
        underTest.initialise(null)

        underTest.uiState.test {
            assertEquals(uiStateDaysEmpty, awaitItem())
        }
    }

    @Test
    fun `initialise sets ui state with days countdown model`() = runTest {
        initUnderTest()
        underTest.initialise(countdownDays.id)

        underTest.uiState.test {
            assertEquals(uiStateDays, awaitItem())
        }
    }

    @Test
    fun `initialise sets ui state with number countdown model`() = runTest {
        initUnderTest()
        underTest.initialise(countdownNumber.id)

        underTest.uiState.test {
            assertEquals(uiStateNumber, awaitItem())
        }
    }

    @Test
    fun `set title updates title`() = runTest {
        initUnderTest()
        underTest.initialise(countdownDays.id)

        underTest.setTitle("input")
        underTest.uiState.test {
            assertEquals("input", awaitItem().title)
        }
    }

    @Test
    fun `set description updates description`() = runTest {
        initUnderTest()
        underTest.initialise(countdownDays.id)

        underTest.setDescription("input")
        underTest.uiState.test {
            assertEquals("input", awaitItem().description)
        }
    }

    @Test
    fun `set color updates color`() = runTest {
        initUnderTest()
        underTest.initialise(countdownDays.id)

        underTest.setColor("input")
        underTest.uiState.test {
            assertEquals("input", awaitItem().colorHex)
        }
    }

    @Test
    fun `set type from day to day keeps data`() = runTest {
        initUnderTest()
        underTest.initialise(countdownDays.id)

        underTest.uiState.test {
            assertEquals(uiStateDays.inputTypes, awaitItem().inputTypes)

            underTest.setType(CountdownType.DAYS)
            underTest.setTitle("makeModelDifferent")

            assertEquals(uiStateDays.inputTypes, awaitItem().inputTypes)
        }
    }

    @Test
    fun `set type from number to money keeps data`() = runTest {
        initUnderTest()
        underTest.initialise(countdownNumber.id)

        underTest.uiState.test {
            assertEquals(uiStateNumber.inputTypes, awaitItem().inputTypes)

            underTest.setType(CountdownType.MONEY_EUR)

            assertEquals(uiStateNumber.inputTypes, awaitItem().inputTypes)
        }
    }

    @Test
    fun `set type from number to day wipes data`() = runTest {
        initUnderTest()
        underTest.initialise(countdownNumber.id)

        underTest.uiState.test {
            assertEquals(uiStateNumber.inputTypes, awaitItem().inputTypes)

            underTest.setType(CountdownType.DAYS)

            assertEquals(uiStateDaysEmpty.inputTypes, awaitItem().inputTypes)
        }
    }

    @Test
    fun `set type from day to number wipes data`() = runTest {
        initUnderTest()
        underTest.initialise(countdownDays.id)

        underTest.uiState.test {
            assertEquals(uiStateDays.inputTypes, awaitItem().inputTypes)

            underTest.setType(CountdownType.NUMBER)

            assertEquals(uiStateNumberEmpty.inputTypes, awaitItem().inputTypes)
        }
    }

    @Test
    fun `set start date updates start date`() = runTest {
        val date = LocalDateTime.now()
        initUnderTest()
        underTest.initialise(countdownNumber.id)

        underTest.setStartDate(date)
        underTest.uiState.test {
            assertEquals(date, (awaitItem().inputTypes as UiState.Types.Values).startDate)
        }
    }

    @Test
    fun `set end date updates end date`() = runTest {
        val date = LocalDateTime.now()
        initUnderTest()
        underTest.initialise(countdownNumber.id)

        underTest.setEndDate(date)
        underTest.uiState.test {
            assertEquals(date, (awaitItem().inputTypes as UiState.Types.Values).endDate)
        }
    }

    @Test
    fun `set start value updates start value`() = runTest {
        initUnderTest()
        underTest.initialise(countdownNumber.id)

        underTest.setStartValue("1")
        underTest.uiState.test {
            assertEquals("1", (awaitItem().inputTypes as UiState.Types.Values).startValue)
        }
    }

    @Test
    fun `set end value updates end value`() = runTest {
        initUnderTest()
        underTest.initialise(countdownNumber.id)

        underTest.setEndValue("1")
        underTest.uiState.test {
            assertEquals("1", (awaitItem().inputTypes as UiState.Types.Values).endValue)
        }
    }

    @Test
    fun `save will save data if valid`() = runTest {
        initUnderTest()
        underTest.initialise(countdownDays.id)

        underTest.save()
        verify {
            mockCountdownConnector.saveSync(countdownDays)
        }
    }

    @Test
    fun `save when data is not valid will log exception`() = runTest {
        initUnderTest()
        underTest.initialise(countdownDays.id)

        underTest.setTitle("")

        underTest.save()
        verify(exactly = 0) {
            mockCountdownConnector.saveSync(countdownDays)
        }
        verify {
            mockCrashReporter.logException(any())
        }
    }

    @Test
    fun `save with no id will generate new one`() = runTest {
        initUnderTest()
        underTest.initialise(null)

        underTest.setTitle("Test")
        underTest.setEndDate(tomorrow)

        underTest.save()
        verify {
            mockCountdownConnector.saveSync(any())
        }
    }

    @Test
    fun `delete on existing countdown deletes item`() = runTest {
        initUnderTest()
        underTest.initialise(countdownDays.id)

        underTest.delete()
        verify {
            mockCountdownConnector.delete(countdownDays.id)
        }
    }
}