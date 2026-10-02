package tmg.hourglass.presentation.modify

import android.content.Context
import androidx.core.app.NotificationManagerCompat
import app.cash.turbine.test
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.hourglass.core.crashlytics.AnalyticsManager
import tmg.hourglass.core.googleanalytics.CrashReporter
import tmg.hourglass.domain.model.Tag
import tmg.hourglass.domain.repositories.CountdownRepository
import tmg.hourglass.domain.repositories.TagRepository
import tmg.hourglass.domain.usecases.CancelNotificationsUseCase
import tmg.hourglass.domain.usecases.ScheduleNotificationsUseCase
import tmg.hourglass.presentation.modify.ModifyData.countdownDays
import tmg.hourglass.presentation.modify.ModifyData.countdownNumber
import tmg.hourglass.presentation.modify.ModifyData.today
import tmg.hourglass.presentation.modify.ModifyData.uiStateDays
import java.time.LocalDateTime

internal class ModifyViewModelTest {

    private val mockCountdownRepository: CountdownRepository = mockk(relaxed = true)
    private val mockTagRepository: TagRepository = mockk(relaxed = true)
    private val mockScheduleNotificationsUseCase: ScheduleNotificationsUseCase = mockk(relaxed = true)
    private val mockCancelNotificationsUseCase: CancelNotificationsUseCase = mockk(relaxed = true)
    private val mockCrashReporter: CrashReporter = mockk(relaxed = true)
    private val mockAnalyticsManager: AnalyticsManager = mockk(relaxed = true)
    private val mockContext: Context = mockk(relaxed = true)
    private val mockNotificationManagerCompat: NotificationManagerCompat = mockk(relaxed = true)

    private lateinit var underTest: ModifyViewModel

    @BeforeEach
    fun setUp() {
        mockkStatic(NotificationManagerCompat::class)
        every { NotificationManagerCompat.from(any()) } returns mockNotificationManagerCompat
        every { mockNotificationManagerCompat.areNotificationsEnabled() } returns true
        every { mockTagRepository.getAll() } returns kotlinx.coroutines.flow.flowOf(emptyList())
    }

    @AfterEach
    fun tearDown() {
        unmockkStatic(NotificationManagerCompat::class)
    }

    private fun initUnderTest() {
        underTest = ModifyViewModel(
            countdownRepository = mockCountdownRepository,
            tagRepository = mockTagRepository,
            scheduleNotificationsUseCase = mockScheduleNotificationsUseCase,
            cancelNotificationsUseCase = mockCancelNotificationsUseCase,
            crashReporter = mockCrashReporter,
            analyticsManager = mockAnalyticsManager,
            context = mockContext
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
    fun `setType clears configured notifications`() = runTest {
        every { mockTagRepository.getAll() } returns flow { emit(emptyList<Tag>()) }

        initUnderTest()

        underTest.uiState.test {
            val item1 = awaitItem()
            val firstId = item1.notifications[0].id
            underTest.updateNotificationValue(firstId, "10")

            val item2 = awaitItem()
            assertEquals(2, item2.notifications.size)

            underTest.setType(tmg.hourglass.domain.enums.CountdownType.NUMBER)
            val item3 = awaitItem()
            assertEquals(1, item3.notifications.size)
            assertEquals("", item3.notifications[0].value)
        }
    }

    @Test
    fun `EndDate notification daysBefore out of range adds NOTIFICATION_OUT_OF_RANGE error and disables save`() = runTest {
        every { mockTagRepository.getAll() } returns flow { emit(emptyList<Tag>()) }
        every { mockCountdownRepository.getSync("1") } returns ModifyData.countdownDays

        initUnderTest()

        underTest.uiState.test {
            awaitItem()
            underTest.initialise("1")
            val loaded = awaitItem()
            val firstId = loaded.notifications[0].id

            // Set daysBefore to 50 when countdown duration is only 1 day (today to tomorrow)
            underTest.updateNotificationValue(firstId, "50")
            val invalidState = awaitItem()

            assertEquals(true, invalidState.errors.contains(UiState.ErrorTypes.NOTIFICATION_OUT_OF_RANGE))
            assertEquals(false, invalidState.isSaveEnabled)
            val notificationError = invalidState.getNotificationError(invalidState.notifications[0])
            assertEquals(NotificationError.DaysOutOfRange(maxDays = 1L), notificationError)
        }
    }

    @Test
    fun `Values notification value out of range adds NOTIFICATION_OUT_OF_RANGE error and disables save`() = runTest {
        every { mockTagRepository.getAll() } returns flow { emit(emptyList<Tag>()) }
        every { mockCountdownRepository.getSync("2") } returns ModifyData.countdownNumber

        initUnderTest()

        underTest.uiState.test {
            awaitItem()
            underTest.initialise("2")
            val loaded = awaitItem()
            val firstId = loaded.notifications[0].id

            // countdownNumber range is 0 to 100. Enter 200 (out of range).
            underTest.updateNotificationValue(firstId, "200")
            val invalidState = awaitItem()

            assertEquals(true, invalidState.errors.contains(UiState.ErrorTypes.NOTIFICATION_OUT_OF_RANGE))
            assertEquals(false, invalidState.isSaveEnabled)
            val notificationError = invalidState.getNotificationError(invalidState.notifications[0])
            assertEquals(NotificationError.ValueOutOfRange(minVal = 0, maxVal = 100), notificationError)
        }
    }

    @Test
    fun `updateNotificationValue appends new blank row when all rows are filled`() = runTest {
        every { mockTagRepository.getAll() } returns flow { emit(emptyList<Tag>()) }

        initUnderTest()

        underTest.uiState.test {
            val item1 = awaitItem()
            assertEquals(1, item1.notifications.size)
            val firstId = item1.notifications[0].id

            underTest.updateNotificationValue(firstId, "350")

            val item2 = awaitItem()
            assertEquals(2, item2.notifications.size)
            assertEquals("350", item2.notifications[0].value)
            assertEquals("", item2.notifications[1].value)
        }
    }

    @Test
    fun `updateNotificationValue clearing value cleans up extra rows leaving trailing blank row`() = runTest {
        every { mockTagRepository.getAll() } returns flow { emit(emptyList<Tag>()) }

        initUnderTest()

        underTest.uiState.test {
            val item1 = awaitItem()
            val firstId = item1.notifications[0].id

            underTest.updateNotificationValue(firstId, "350")
            val item2 = awaitItem()
            assertEquals(2, item2.notifications.size)

            // Now clear the value
            underTest.updateNotificationValue(firstId, "")
            val item3 = awaitItem()
            assertEquals(1, item3.notifications.size)
            assertEquals("", item3.notifications[0].value)
        }
    }

    @Test
    fun `deleteNotification removes specified notification`() = runTest {
        every { mockTagRepository.getAll() } returns flow { emit(emptyList<Tag>()) }

        initUnderTest()

        underTest.uiState.test {
            val item1 = awaitItem()
            val firstId = item1.notifications[0].id

            underTest.updateNotificationValue(firstId, "350")
            val item2 = awaitItem()
            assertEquals(2, item2.notifications.size)

            underTest.deleteNotification(firstId)
            val item3 = awaitItem()
            assertEquals(1, item3.notifications.size)
            assertEquals("", item3.notifications[0].value)
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
    fun `save when valid will call repository saveSync, cancelNotificationsUseCase and scheduleNotificationsUseCase`() = runTest {
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

            verify { mockAnalyticsManager.event(any(), any()) }
            coVerify { mockCancelNotificationsUseCase("1") }
            verify { mockCountdownRepository.saveSync(any()) }
            coVerify { mockScheduleNotificationsUseCase("1") }
        }
    }

    @Test
    fun `delete will call cancelNotificationsUseCase and repository delete when id is set`() = runTest {
        every { mockTagRepository.getAll() } returns flow { emit(emptyList<Tag>()) }
        every { mockCountdownRepository.getSync("1") } returns countdownNumber

        initUnderTest()

        underTest.uiState.test {
            awaitItem()

            underTest.initialise("1")

            awaitItem()

            underTest.delete()

            verify { mockAnalyticsManager.event(any()) }
            coVerify { mockCancelNotificationsUseCase("1") }
            verify { mockCountdownRepository.delete("1") }
        }
    }
}
