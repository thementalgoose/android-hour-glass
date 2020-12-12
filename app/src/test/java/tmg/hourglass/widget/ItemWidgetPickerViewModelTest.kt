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
        sut = ItemWidgetPickerViewModel(mockWidgetReferenceConnector, mockCountdownConnector)
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

        sut.outputs.list.test {
            assertValue(expected)
        }
    }

    @Test
    fun `ItemWidgetPickerViewModel placeholder item is loaded when there are no items in the database`() = coroutineTest {

        whenever(mockCountdownConnector.all()).thenReturn(flow { emit(emptyList<Countdown>()) })
        val expected = listOf(
            HomeItemType.Header,
            HomeItemType.Placeholder
        )

        initSUT()
        advanceUntilIdle()

        sut.outputs.list.test {
            assertValue(expected)
        }
    }

    @Test
    fun `ItemWidgetPickerViewModel check item marks the item checked in`() = coroutineTest {

        whenever(mockCountdownConnector.all()).thenReturn(flow { emit(mockCountdownItems) })

        initSUT()
        advanceUntilIdle()

        val listObserver = sut.outputs.list.testObserve()
        val savedObserver = sut.outputs.isSavedEnabled.testObserve()

        listObserver.assertValue(listOf(HomeItemType.Header,
            expectedWidgetPickerItemPrimary,
            expectedWidgetPickerItemSecondary
        ))
        savedObserver.assertValue(false)

        sut.inputs.checkedItem(mockCountdownPrimaryId)

        listObserver.assertValue(listOf(HomeItemType.Header,
            expectedWidgetPickerItemPrimary.copy(isEnabled = true),
            expectedWidgetPickerItemSecondary
        ))
        savedObserver.assertValue(true)
    }

    @Test
    fun `ItemWidgetPickerViewModel with item checked, clicking save saves to widget reference connector and emits save event`() = coroutineTest {

        whenever(mockCountdownConnector.all()).thenReturn(flow { emit(mockCountdownItems)})

        initSUT()
        advanceUntilIdle()

        sut.inputs.checkedItem(mockCountdownSecondaryId)

        sut.outputs.isSavedEnabled.test {
            assertValue(true)
        }

        sut.inputs.clickSave()

        verify(mockWidgetReferenceConnector).saveSync(any(), any())
        sut.outputs.save.test {
            assertDataEventValue(mockCountdownSecondaryId)
        }
    }

    @AfterEach
    internal fun tearDown() {

        reset(mockWidgetReferenceConnector, mockCountdownConnector)
    }
}