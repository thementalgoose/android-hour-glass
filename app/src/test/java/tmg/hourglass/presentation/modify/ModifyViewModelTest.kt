package tmg.hourglass.presentation.modify

import app.cash.turbine.test
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tmg.hourglass.domain.model.Tag
import tmg.hourglass.domain.repositories.CountdownRepository
import tmg.hourglass.domain.repositories.TagRepository
import tmg.hourglass.core.googleanalytics.CrashReporter
import tmg.hourglass.domain.model.preview
import java.time.LocalDateTime
import tmg.hourglass.presentation.modify.ModifyData.countdownDays
import tmg.hourglass.presentation.modify.ModifyData.countdownNumber
import tmg.hourglass.presentation.modify.ModifyData.today
import tmg.hourglass.presentation.modify.ModifyData.uiStateDays

internal class ModifyViewModelTest {

    private val mockCountdownRepository: CountdownRepository = mockk(relaxed = true)
    private val mockTagRepository: TagRepository = mockk(relaxed = true)
    private val mockCrashReporter: CrashReporter = mockk(relaxed = true)

    private lateinit var underTest: ModifyViewModel

    private fun initUnderTest() {
        underTest = ModifyViewModel(
            countdownRepository = mockCountdownRepository,
            tagRepository = mockTagRepository,
            crashReporter = mockCrashReporter
        )
    }

    @Test
    fun `initialise with id loads countdown into state`() = runTest {
        every { mockTagRepository.getAll() } returns flow { emit(emptyList<Tag>()) }
        every { mockCountdownRepository.getSync("1") } returns countdownDays

        initUnderTest()

        underTest.uiState.test {
            awaitItem()

            underTest.initialise("1")

            val loaded = awaitItem()
            assertEquals(uiStateDays.title, loaded.title)
            assertEquals(uiStateDays.colorHex, loaded.colorHex)
        }
    }

    @Test
    fun `setters update ui state values`() = runTest {
        every { mockTagRepository.getAll() } returns flow { emit(emptyList<Tag>()) }

        initUnderTest()

        underTest.uiState.test {
            awaitItem()

            underTest.setTitle("My title")
            assertEquals("My title", awaitItem().title)

            underTest.setDescription("desc")
            assertEquals("desc", awaitItem().description)

            underTest.setColor("#fff")
            assertEquals("#fff", awaitItem().colorHex)

            // switch to NUMBER type then set start date
            underTest.setType(tmg.hourglass.domain.enums.CountdownType.NUMBER)
            val afterType = awaitItem()
            assertEquals(tmg.hourglass.domain.enums.CountdownType.NUMBER, afterType.type)

            val dt: LocalDateTime = today
            underTest.setStartDate(dt)
            val afterStart = awaitItem()
            val startDate = (afterStart.inputTypes as UiState.Types.Values).startDate
            assertEquals(dt, startDate)
        }
    }

    @Test
    fun `setTag toggles selection when same tag is set twice`() = runTest {
        val tag = Tag.preview()
        every { mockTagRepository.getAll() } returns flow { emit(listOf(tag)) }

        initUnderTest()

        underTest.uiState.test {
            // consume initial emissions (initial + tags) to ensure combined state has applied tags
            awaitItem()
            awaitItem()

            underTest.setTag(tag)
            val withTag = awaitItem()
            assertEquals(tag, withTag.tag)

            underTest.setTag(tag)
            val toggled = awaitItem()
            assertEquals(null, toggled.tag)
        }
    }

    @Test
    fun `save when invalid logs exception and does not save`() = runTest {
        every { mockTagRepository.getAll() } returns flow { emit(emptyList<Tag>()) }

        initUnderTest()

        underTest.uiState.test {
            awaitItem()

            // initial state has empty title -> invalid
            underTest.save()

            verify { mockCrashReporter.logException(any()) }
            verify(exactly = 0) { mockCountdownRepository.saveSync(any()) }
        }
    }

    @Test
    fun `save when valid will call repository saveSync`() = runTest {
        every { mockTagRepository.getAll() } returns flow { emit(emptyList<Tag>()) }
        every { mockCountdownRepository.getSync("1") } returns countdownDays

        initUnderTest()

        underTest.uiState.test {
            awaitItem()

            underTest.initialise("1")

            val loaded = awaitItem()
            // loaded should match valid sample
            assertEquals(uiStateDays.title, loaded.title)
            assertEquals(true, loaded.isSaveEnabled)

            underTest.save()

            verify { mockCountdownRepository.saveSync(any()) }
        }
    }

    @Test
    fun `delete will call repository delete when id is set`() = runTest {
        every { mockTagRepository.getAll() } returns flow { emit(emptyList<Tag>()) }
        every { mockCountdownRepository.getSync("1") } returns countdownNumber

        initUnderTest()

        underTest.uiState.test {
            awaitItem()

            underTest.initialise("1")

            awaitItem()

            underTest.delete()

            verify { mockCountdownRepository.delete("1") }
        }
    }
}
