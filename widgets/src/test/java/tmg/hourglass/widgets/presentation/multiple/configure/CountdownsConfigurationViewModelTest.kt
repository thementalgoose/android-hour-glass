package tmg.hourglass.widgets.presentation.multiple.configure

import app.cash.turbine.test
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tmg.hourglass.domain.model.Tag
import tmg.hourglass.domain.model.WidgetReference
import tmg.hourglass.domain.model.preview
import tmg.hourglass.domain.modelMultiple
import tmg.hourglass.domain.repositories.TagRepository
import tmg.hourglass.domain.repositories.WidgetRepository
import tmg.testutils.BaseTest

internal class CountdownsConfigurationViewModelTest : BaseTest() {

    private val mockTagRepository: TagRepository = mockk(relaxed = true)
    private val mockWidgetRepository: WidgetRepository = mockk(relaxed = true)

    private lateinit var underTest: CountdownsConfigurationViewModel

    private fun initUnderTest() {
        underTest = CountdownsConfigurationViewModel(
            tagRepository = mockTagRepository,
            widgetRepository = mockWidgetRepository
        )
    }

    @Test
    fun `view model initialise loads all tags`() = runTest {
        val tags = listOf(Tag.preview(tagId = "tag1", name = "Tag 1"))
        every { mockTagRepository.getAll() } returns flow { emit(tags) }
        initUnderTest()

        underTest.uiState.test {
            assertEquals(tags, awaitItem().tags)
        }
    }

    @Test
    fun `loading app widget updates state with tag selection`() = runTest {
        val tags = listOf(Tag.preview(tagId = "tag1", name = "Tag 1"))
        every { mockTagRepository.getAll() } returns flow { emit(tags) }

        val appWidgetId = 2
        every { mockWidgetRepository.getSync(appWidgetId) } returns WidgetReference.modelMultiple(
            appWidgetId = appWidgetId,
            tagId = "tag1",
            openAppOnClick = true
        )

        initUnderTest()
        underTest.uiState.test {
            assertEquals(tags, awaitItem().tags)

            underTest.load(appWidgetId)

            val model = awaitItem()
            assertEquals(appWidgetId, model.appWidgetId)
            assertEquals("tag1", model.selectedTagId)
            assertEquals(true, model.openAppOnClick)
        }
    }

    @Test
    fun `selecting tag updates selectedTagId`() = runTest {
        val tags = listOf(Tag.preview(tagId = "tag1"), Tag.preview(tagId = "tag2"))
        every { mockTagRepository.getAll() } returns flow { emit(tags) }

        initUnderTest()
        underTest.uiState.test {
            assertEquals(tags, awaitItem().tags)

            underTest.selectTag("tag2")

            val model = awaitItem()
            assertEquals("tag2", model.selectedTagId)

            underTest.selectTag(null) // Selecting All

            val allSelectedModel = awaitItem()
            assertEquals(null, allSelectedModel.selectedTagId)
        }
    }

    @Test
    fun `saving item saves multiple widget ref`() = runTest {
        val tags = listOf(Tag.preview(tagId = "tag1"))
        every { mockTagRepository.getAll() } returns flow { emit(tags) }

        val appWidgetId = 5
        every { mockWidgetRepository.getSync(appWidgetId) } returns WidgetReference.modelMultiple(
            appWidgetId = appWidgetId,
            tagId = null,
            openAppOnClick = false
        )

        initUnderTest()
        underTest.uiState.test {
            assertEquals(tags, awaitItem().tags)

            underTest.load(appWidgetId)

            val loaded = awaitItem()
            assertEquals(null, loaded.selectedTagId)

            underTest.selectTag("tag1")
            assertEquals("tag1", awaitItem().selectedTagId)

            underTest.openAppOnClick(true)
            assertEquals(true, awaitItem().openAppOnClick)

            underTest.save()

            val expectedWidgetRef = WidgetReference.Multiple(
                appWidgetId = appWidgetId,
                tagId = "tag1",
                openAppOnClick = true
            )
            verify {
                mockWidgetRepository.saveSync(expectedWidgetRef)
            }
        }
    }
}
