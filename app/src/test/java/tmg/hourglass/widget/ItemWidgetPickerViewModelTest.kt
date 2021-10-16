package tmg.hourglass.widget

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.hourglass.*
import tmg.hourglass.domain.connectors.CountdownConnector
import tmg.hourglass.domain.connectors.WidgetConnector
import tmg.hourglass.domain.model.Countdown
import tmg.hourglass.home.HomeItemType
import tmg.testutils.BaseTest
import tmg.testutils.livedata.assertDataEventValue
import tmg.testutils.livedata.test
import tmg.testutils.livedata.testObserve

internal class ItemWidgetPickerViewModelTest: BaseTest() {

    lateinit var sut: ItemWidgetPickerViewModel

    private var mockWidgetReferenceConnector: WidgetConnector = mockk(relaxed = true)
    private var mockCountdownConnector: CountdownConnector = mockk(relaxed = true)

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

        every { mockCountdownConnector.all() } returns flow { emit(mockCountdownItems) }
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

        every { mockCountdownConnector.all() } returns flow { emit(emptyList<Countdown>()) }
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

        every { mockCountdownConnector.all() } returns flow { emit(mockCountdownItems) }

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

        every {mockCountdownConnector.all() } returns flow { emit(mockCountdownItems)}

        initSUT()
        advanceUntilIdle()

        sut.inputs.checkedItem(mockCountdownSecondaryId)

        sut.outputs.isSavedEnabled.test {
            assertValue(true)
        }

        sut.inputs.clickSave()

        verify {
            mockWidgetReferenceConnector.saveSync(any(), any())
        }
        sut.outputs.save.test {
            assertDataEventValue(mockCountdownSecondaryId)
        }
    }
}