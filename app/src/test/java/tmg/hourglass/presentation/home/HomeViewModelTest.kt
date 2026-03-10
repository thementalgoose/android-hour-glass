package tmg.hourglass.presentation.home

import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import tmg.hourglass.core.crashlytics.AnalyticsManager
import tmg.hourglass.domain.model.Countdown
import tmg.hourglass.domain.model.Tag
import tmg.hourglass.domain.model.TagOrdering
import tmg.hourglass.domain.model.TaggedCountdowns
import tmg.hourglass.domain.model.preview
import tmg.hourglass.domain.usecases.GetTaggedCountdownsUseCase
import tmg.hourglass.domain.repositories.CountdownRepository
import tmg.hourglass.domain.repositories.PreferencesManager
import tmg.hourglass.domain.repositories.TagRepository

internal class HomeViewModelTest {

    private val mockGetTagged: GetTaggedCountdownsUseCase = mockk()
    private val mockCountdownRepository: CountdownRepository = mockk(relaxed = true)
    private val mockTagRepository: TagRepository = mockk(relaxed = true)
    private val mockPreferencesManager: PreferencesManager = mockk(relaxed = true)
    private val mockAnalyticsManager: AnalyticsManager = mockk(relaxed = true)

    private lateinit var underTest: HomeViewModel

    private fun initUnderTest() {
        every { mockPreferencesManager.sortOrder } returns TagOrdering.ALPHABETICAL

        underTest = HomeViewModel(
            getTaggedCountdownsUseCase = mockGetTagged,
            countdownRepository = mockCountdownRepository,
            tagRepository = mockTagRepository,
            preferencesManager = mockPreferencesManager,
            analyticsManager = mockAnalyticsManager
        )
    }

    @Test
    fun `initial ui state is empty when use case emits empty list`() = runTest {
        coEvery { mockGetTagged() } returns flowOf(emptyList())

        initUnderTest()

        underTest.uiState.test {
            assertEquals(0, awaitItem().items.size)
        }
    }

    @Test
    fun `untagged only list emits header then countdown item`() = runTest {
        val cd: Countdown = Countdown.preview()
        coEvery { mockGetTagged() } returns flowOf(
            listOf(TaggedCountdowns.Untagged(sort = TagOrdering.ALPHABETICAL, countdowns = listOf(cd)))
        )

        initUnderTest()

        underTest.uiState.test {
            // consume initial stateIn initial value
            awaitItem()

            val state = awaitItem()
            assertEquals(2, state.items.size)
            assertTrue(state.items.first() is ListItem.UntaggedHeader)
            val item = state.items[1]
            assertTrue(item is ListItem.CountdownItem)
            assertEquals(cd.id, (item as ListItem.CountdownItem).countdown.id)
        }
    }

    @Test
    fun `untaggedSort updates preferences sortOrder`() = runTest {
        val cd: Countdown = Countdown.preview()
        coEvery { mockGetTagged() } returns flow {
            emit(listOf(TaggedCountdowns.Untagged(sort = TagOrdering.ALPHABETICAL, countdowns = listOf(cd))))
            awaitCancellation()
        }

        initUnderTest()

        underTest.uiState.test {
            // consume initial stateIn initial value
            awaitItem()

            // change sort
            underTest.untaggedSort(TagOrdering.FINISHING_SOONEST)
            // verify preference setter was called
            verify { mockPreferencesManager.sortOrder = TagOrdering.FINISHING_SOONEST }
            // there should be at least one emission after change
            val state = awaitItem()
            assertEquals(true, state.items.isNotEmpty())
        }
    }

    @Test
    fun `tagSortUpdated calls tagRepository insertTag with updated sort`() = runTest {
        val tag = Tag(tagId = "tag1", name = "Tag One", colour = "#000", sort = TagOrdering.ALPHABETICAL, expanded = false)
        coEvery { mockGetTagged() } returns flowOf(emptyList())

        initUnderTest()

        underTest.tagSortUpdated(tag, TagOrdering.PROGRESS)

        verify { mockTagRepository.insertTag(tag.copy(sort = TagOrdering.PROGRESS)) }
    }

    @Test
    fun `delete will call countdownRepository delete with id`() = runTest {
        val cd: Countdown = Countdown.preview()
        coEvery { mockGetTagged() } returns flowOf(emptyList())

        initUnderTest()

        underTest.delete(cd)

        verify { mockAnalyticsManager.event(any()) }
        verify { mockCountdownRepository.delete(cd.id) }
    }
}
