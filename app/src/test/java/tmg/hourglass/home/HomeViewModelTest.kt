package tmg.hourglass.home

import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flow
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.hourglass.*
import tmg.hourglass.data.connectors.CountdownConnector
import tmg.hourglass.data.models.Countdown
import tmg.hourglass.home.HomeTab.NOW
import tmg.hourglass.home.HomeTab.PREVIOUS
import tmg.hourglass.testutils.BaseTest
import tmg.hourglass.testutils.assertDataEventValue
import tmg.hourglass.testutils.assertEventFired
import tmg.hourglass.testutils.assertValue

@FlowPreview
@ExperimentalCoroutinesApi
class HomeViewModelTest: BaseTest() {

    lateinit var sut: HomeViewModel

    private var mockCountdownConnector: CountdownConnector = mock()

    private val mockListNow: List<Countdown> = listOf(mockCountdownPrimary, mockCountdownSecondary)
    private val mockListPrevious: List<Countdown> = listOf(mockCountdownTertiary)

    private val expectedListNow: List<HomeItemType> = listOf(
        expectedItemPrimary.copy(action = HomeItemAction.EDIT),
        expectedItemSecondary.copy(action = HomeItemAction.EDIT)
    )
    private val expectedListPrevious: List<HomeItemType> = listOf(
        expectedItemTertiary.copy(action = HomeItemAction.DELETE)
    )

    @BeforeEach
    internal fun setUp() {
        whenever(mockCountdownConnector.allCurrent()).thenReturn(flow { emit(mockListNow)} )
        whenever(mockCountdownConnector.allDone()).thenReturn(flow { emit(mockListPrevious)} )
    }

    private fun initSUT() {
        sut = HomeViewModel(mockCountdownConnector, testScopeProvider)
    }

    @Test
    fun `HomeViewModel initial setup loads empty list if no items are stored`() = coroutineTest {

        whenever(mockCountdownConnector.allCurrent()).thenReturn(flow { emit(emptyList<Countdown>()) })

        val expected = listOf(
            HomeItemType.Header,
            HomeItemType.Placeholder
        )

        initSUT()
        advanceUntilIdle()

        assertValue(expected, sut.outputs.items)
    }

    @Test
    fun `HomeViewModel initial setup loads the NOW tab and all items by default`() = coroutineTest {

        val expected = mutableListOf<HomeItemType>()
        expected.add(HomeItemType.Header)
        expected.addAll(expectedListNow)

        initSUT()
        advanceUntilIdle()

        assertValue(expected, sut.outputs.items)
    }

    @Test
    fun `HomeViewModel changing tabs changes list content`() = coroutineTest {

        val expectedPrevious = mutableListOf<HomeItemType>()
        expectedPrevious.add(HomeItemType.Header)
        expectedPrevious.addAll(expectedListPrevious)

        val expectedNow = mutableListOf<HomeItemType>()
        expectedNow.add(HomeItemType.Header)
        expectedNow.addAll(expectedListNow)

        initSUT()
        advanceUntilIdle()

        assertValue(expectedNow, sut.outputs.items)
        verify(mockCountdownConnector).allCurrent()

        sut.inputs.switchList(PREVIOUS)

        assertValue(expectedPrevious, sut.outputs.items)
        verify(mockCountdownConnector).allDone()

        sut.inputs.switchList(NOW)

        assertValue(expectedNow, sut.outputs.items)
    }

    @Test
    fun `HomeViewModel clicking add button launches add item event`() = coroutineTest {

        initSUT()
        advanceUntilIdle()

        sut.inputs.clickAdd()

        assertEventFired(sut.outputs.addItemEvent)
    }

    @Test
    fun `HomeViewModel clicking edit button on tab launches edit event`() = coroutineTest {

        val sampleId = "sampleId"

        initSUT()
        advanceUntilIdle()

        sut.inputs.editItem(sampleId)

        assertDataEventValue(sampleId, sut.outputs.editItemEvent)
    }

    @Test
    fun `HomeViewModel clicking delete button on previous tab launches deletion event`() = coroutineTest {

        val sampleId = "sampleId"

        initSUT()
        advanceUntilIdle()

        sut.inputs.deleteItem(sampleId)

        assertEventFired(sut.outputs.deleteItemEvent)
        verify(mockCountdownConnector).delete(any())
    }

    @AfterEach
    internal fun tearDown() {

        reset(mockCountdownConnector)
    }
}