package tmg.hourglass

import org.threeten.bp.LocalDateTime
import tmg.hourglass.domain.enums.CountdownInterpolator
import tmg.hourglass.domain.enums.CountdownType
import tmg.hourglass.domain.model.Countdown
import tmg.hourglass.home.HomeItemAction
import tmg.hourglass.home.HomeItemType

const val mockCountdownPrimaryId: String = "1"
val mockCountdownPrimary: Countdown = Countdown(
    id = mockCountdownPrimaryId,
    name = "Test1", description = "Test1", colour = "#000001",
    start = LocalDateTime.now(), end = LocalDateTime.now(),
    initial = "0", finishing = "100", countdownType = CountdownType.DAYS,
    interpolator = CountdownInterpolator.LINEAR
)
val expectedItemPrimary: HomeItemType.Item = HomeItemType.Item(
    countdown = mockCountdownPrimary,
    action = HomeItemAction.CHECK,
    isEnabled = false,
    showDescription = true,
    clickBackground = false,
    animateBar = true
)


const val mockCountdownSecondaryId: String = "2"
val mockCountdownSecondary: Countdown = Countdown(
    id = mockCountdownSecondaryId,
    name = "Test2", description = "Test2", colour = "#000002",
    start = LocalDateTime.now(), end = LocalDateTime.now(),
    initial = "100", finishing = "0", countdownType = CountdownType.DAYS,
    interpolator = CountdownInterpolator.LINEAR
)
val expectedItemSecondary: HomeItemType.Item = HomeItemType.Item(
    countdown = mockCountdownSecondary,
    action = HomeItemAction.CHECK,
    isEnabled = false,
    showDescription = true,
    clickBackground = false,
    animateBar = true
)


const val mockCountdownTertiaryId: String = "3"
val mockCountdownTertiary: Countdown = Countdown(
    id = mockCountdownTertiaryId,
    name = "Test3", description = "Test3", colour = "#000003",
    start = LocalDateTime.now(), end = LocalDateTime.now(),
    initial = "50", finishing = "150", countdownType = CountdownType.DAYS,
    interpolator = CountdownInterpolator.LINEAR
)
val expectedItemTertiary: HomeItemType.Item = HomeItemType.Item(
    countdown = mockCountdownTertiary,
    action = HomeItemAction.CHECK,
    isEnabled = false,
    showDescription = true,
    clickBackground = false,
    animateBar = true
)

