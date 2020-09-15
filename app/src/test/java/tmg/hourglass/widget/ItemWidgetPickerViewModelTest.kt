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
import tmg.hourglass.*
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
    private val mockCountdownItems = listOf(mockCountdownPrimary, mockCountdownSecondary)

    private val expectedWidgetPickerItemPrimary = expectedItemPrimary.copy(
        isEnabled = false,
        clickBackground = true,
        showDescription = false,
        animateBar = false
    )
    private val expectedWidgetPickerItemSecondary = expectedItemSecondary.copy(
        isEnabled = false,
        clickBackground = true,
        showDescription = false,
        animateBar = false
    )

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
            expectedWidgetPickerItemPrimary,
            expectedWidgetPickerItemSecondary
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

        assertValue(listOf(HomeItemType.Header,
            expectedWidgetPickerItemPrimary,
            expectedWidgetPickerItemSecondary
        ), sut.outputs.list)
        assertValue(false, sut.outputs.isSavedEnabled)

        sut.inputs.checkedItem(mockCountdownPrimaryId)

        assertValue(listOf(HomeItemType.Header,
            expectedWidgetPickerItemPrimary.copy(isEnabled = true),
            expectedWidgetPickerItemSecondary
        ), sut.outputs.list)
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