package tmg.hourglass.presentation.tag

import app.cash.turbine.test
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tmg.hourglass.core.crashlytics.AnalyticsManager
import tmg.hourglass.domain.model.Tag
import tmg.hourglass.domain.model.TagOrdering
import tmg.hourglass.domain.model.preview
import tmg.hourglass.domain.repositories.TagRepository

internal class TagViewModelTest {

    private val mockTagRepository: TagRepository = mockk(relaxed = true)
    private val mockAnalyticsManager: AnalyticsManager = mockk(relaxed = true)

    private lateinit var underTest: TagViewModel

    private fun initUnderTest() {
        underTest = TagViewModel(
            tagRepository = mockTagRepository,
            analyticsManager = mockAnalyticsManager
        )
    }

    @Test
    fun `initial ui state has empty tags and empty input`() = runTest {
        every { mockTagRepository.getAll() } returns flowOf(emptyList())

        initUnderTest()

        underTest.uiState.test {
            val state = awaitItem()
            assertEquals(emptyList<Tag>(), state.tags)
            assertEquals("", state.tagInput)
        }
    }

    @Test
    fun `tags from repository are emitted into ui state`() = runTest {
        val tag = Tag.preview()
        every { mockTagRepository.getAll() } returns flowOf(listOf(tag))

        initUnderTest()

        underTest.uiState.test {
            awaitItem()

            val state = awaitItem()
            assertEquals(1, state.tags.size)
            assertEquals(tag.tagId, state.tags.first().tagId)
        }
    }

    @Test
    fun `tags are sorted alphabetically by name`() = runTest {
        val tagB = Tag.preview(tagId = "1", name = "Banana")
        val tagA = Tag.preview(tagId = "2", name = "Apple")
        val tagC = Tag.preview(tagId = "3", name = "Cherry")
        every { mockTagRepository.getAll() } returns flowOf(listOf(tagB, tagA, tagC))

        initUnderTest()

        underTest.uiState.test {
            awaitItem()

            val state = awaitItem()
            assertEquals(listOf(tagA, tagB, tagC), state.tags)
        }
    }

    @Test
    fun `tags are sorted case-insensitively`() = runTest {
        val tagLower = Tag.preview(tagId = "1", name = "apple")
        val tagUpper = Tag.preview(tagId = "2", name = "Banana")
        every { mockTagRepository.getAll() } returns flowOf(listOf(tagUpper, tagLower))

        initUnderTest()

        underTest.uiState.test {
            awaitItem()

            val state = awaitItem()
            assertEquals(listOf(tagLower, tagUpper), state.tags)
        }
    }

    @Test
    fun `inputTag updates tagInput in ui state`() = runTest {
        every { mockTagRepository.getAll() } returns flowOf(emptyList())

        initUnderTest()

        underTest.uiState.test {
            awaitItem()

            underTest.inputTag("my tag")

            val state = awaitItem()
            assertEquals("my tag", state.tagInput)
        }
    }

    @Test
    fun `insertTag calls repository insertTag with trimmed name`() = runTest {
        every { mockTagRepository.getAll() } returns flowOf(emptyList())

        initUnderTest()

        underTest.insertTag("  my tag  ")

        verify { mockTagRepository.insertTag(match { it.name == "my tag" }) }
    }

    @Test
    fun `insertTag creates tag with expected default properties`() = runTest {
        every { mockTagRepository.getAll() } returns flowOf(emptyList())

        initUnderTest()

        underTest.insertTag("my tag")

        verify {
            mockTagRepository.insertTag(match { tag ->
                tag.name == "my tag" &&
                tag.colour == "#AD1457" &&
                tag.sort == TagOrdering.FINISHING_SOONEST &&
                tag.expanded == true
            })
        }
    }

    @Test
    fun `insertTag emits tag_add analytics event`() = runTest {
        every { mockTagRepository.getAll() } returns flowOf(emptyList())

        initUnderTest()

        underTest.insertTag("my tag")

        verify { mockAnalyticsManager.event("tag_add") }
    }

    @Test
    fun `deleteTag calls repository deleteTag`() = runTest {
        val tag = Tag.preview()
        every { mockTagRepository.getAll() } returns flowOf(emptyList())

        initUnderTest()

        underTest.deleteTag(tag)

        verify { mockTagRepository.deleteTag(tag) }
    }

    @Test
    fun `deleteTag emits tag_remove analytics event`() = runTest {
        val tag = Tag.preview()
        every { mockTagRepository.getAll() } returns flowOf(emptyList())

        initUnderTest()

        underTest.deleteTag(tag)

        verify { mockAnalyticsManager.event("tag_remove") }
    }
}
