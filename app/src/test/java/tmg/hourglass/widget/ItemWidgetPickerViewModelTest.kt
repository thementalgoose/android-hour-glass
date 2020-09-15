package tmg.hourglass.widget

import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flow
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.threeten.bp.LocalDateTime
import tmg.hourglass.data.CountdownInterpolator
import tmg.hourglass.data.CountdownType
import tmg.hourglass.data.connectors.CountdownConnector
import tmg.hourglass.data.connectors.WidgetConnector
import tmg.hourglass.data.models.Countdown
import tmg.hourglass.home.HomeItemAction
import tmg.hourglass.home.HomeItemType
import tmg.hourglass.testutils.*

@FlowPreview
@ExperimentalCoroutinesApi
class ItemWidgetPickerViewModelTest: BaseTest() {

    lateinit var sut: ItemWidgetPickerViewModel

    private var mockWidgetReferenceConnector: WidgetConnector = mock()
    private var mockCountdownConnector: CountdownConnector = mock()

    private val mockWidgetId: Int = 1
    private val mockCountdownPrimaryId: String = "1"
    private val mockCountdownPrimary: Countdown = Countdown(
        id = mockCountdownPrimaryId,
        name = "Test1", description = "Test1", colour = "#000001",
        start = LocalDateTime.now(), end = LocalDateTime.now(),
        initial = "0", finishing = "100", countdownType = CountdownType.DAYS,
        interpolator = CountdownInterpolator.LINEAR
    )
    private val mockCountdownSecondaryId: String = "2"
    private val mockCountdownSecondary: Countdown = Countdown(
        id = mockCountdownSecondaryId,
        name = "Test2", description = "Test2", colour = "#000002",
        start = LocalDateTime.now(), end = LocalDateTime.now(),
        initial = "100", finishing = "0", countdownType = CountdownType.DAYS,
        interpolator = CountdownInterpolator.LINEAR
    )

    private val expectedItemPrimary: HomeItemType.Item = HomeItemType.Item(
        countdown = mockCountdownPrimary,
        action = HomeItemAction.CHECK,
        isEnabled = false,
        showDescription = false,
        clickBackground = true,
        animateBar = false
    )
    private val expectedItemSecondary: HomeItemType.Item = HomeItemType.Item(
        countdown = mockCountdownSecondary,
        action = HomeItemAction.CHECK,
        isEnabled = false,
        showDescription = false,
        clickBackground = true,
        animateBar = false
    )

    private val mockCountdownItems = listOf(mockCountdownPrimary, mockCountdownSecondary)

    @BeforeEach
    internal fun setUp() {

    }

    private fun initSUT() {
        sut = ItemWidgetPickerViewModel(mockWidgetReferenceConnector, mockCountdownConnector, testScopeProvider)
        sut.inputs.supplyAppWidgetId(mockWidgetId)
    }

    @Test
    fun `ItemWidgetPickerViewModel items are initially loaded with no items checked`() = coroutineTest {

        whenever(mockCountdownConnector.all()).thenReturn(flow { emit(mockCountdownItems) })
        val expected = listOf(
            HomeItemType.Header,
            expectedItemPrimary,
            expectedItemSecondary
        )

        initSUT()
        advanceUntilIdle()

        assertValue(expected, sut.outputs.list)
    }

    @Test
    fun `ItemWidgetPickerViewModel placeholder item is loaded when there are no items in the database`() = coroutineTest {

        whenever(mockCountdownConnector.all()).thenReturn(flow { emit(emptyList()) })
        val expected = listOf(
            HomeItemType.Header,
            HomeItemType.Placeholder
        )

        initSUT()
        advanceUntilIdle()

        assertValue(expected, sut.outputs.list)
    }

    @Test
    fun `ItemWidgetPickerViewModel check item marks the item checked in`() = coroutineTest {

        whenever(mockCountdownConnector.all()).thenReturn(flow { emit(mockCountdownItems) })

        initSUT()
        advanceUntilIdle()

        assertValue(listOf(HomeItemType.Header, expectedItemPrimary, expectedItemSecondary), sut.outputs.list)
        assertValue(false, sut.outputs.isSavedEnabled)

        sut.inputs.checkedItem(mockCountdownPrimaryId)

        assertValue(listOf(HomeItemType.Header, expectedItemPrimary.copy(isEnabled = true), expectedItemSecondary), sut.outputs.list)
        assertValue(true, sut.outputs.isSavedEnabled)
    }

    @Test
    fun `ItemWidgetPickerViewModel with item checked, clicking save saves to widget reference connector and emits save event`() = coroutineTest {

        whenever(mockCountdownConnector.all()).thenReturn(flow { emit(mockCountdownItems)})

        initSUT()
        advanceUntilIdle()

        sut.inputs.checkedItem(mockCountdownSecondaryId)

        assertValue(true, sut.outputs.isSavedEnabled)

        sut.inputs.clickSave()

        verify(mockWidgetReferenceConnector).saveSync(any(), any())
        assertDataEventValue(mockCountdownSecondaryId, sut.outputs.save)
    }

    @AfterEach
    internal fun tearDown() {

        reset(mockWidgetReferenceConnector, mockCountdownConnector)
    }
}