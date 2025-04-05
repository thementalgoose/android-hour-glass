package tmg.hourglass.presentation.modify

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import java.time.LocalDateTime
import tmg.hourglass.presentation.modify.ModifyData.today
import tmg.hourglass.presentation.modify.ModifyData.tomorrow
import tmg.hourglass.presentation.modify.ModifyData.uiStateDays
import tmg.hourglass.presentation.modify.ModifyData.uiStateNumber
import tmg.hourglass.presentation.modify.ModifyData.yesterday
import tmg.hourglass.presentation.modify.UiState.Direction.CountDown
import tmg.hourglass.presentation.modify.UiState.Types.EndDate
import tmg.hourglass.presentation.modify.UiState.Types.Values

internal class UiStateTest {

    enum class InvalidUiState(val uiState: UiState) {
        MISSING_TITLE(uiState = uiStateDays.copy(title = "")),
        MISSING_COLOR(uiState = uiStateDays.copy(colorHex = "")),
        MISSING_DAYS_FINISH_DATE(uiState = uiStateDays.copy(inputTypes = EndDate(null))),
        INVALID_DAYS_FINISH_DATE(uiState = uiStateDays.copy(inputTypes = EndDate(LocalDateTime.now().minusDays(1L)))),
        MISSING_NUMBER_INITIAL_AND_FINISHING(uiState = uiStateNumber.copy(
            inputTypes = Values(valueDirection = CountDown, startDate = today, endDate = tomorrow, startValue = "", endValue = ""))
        ),
        MISSING_NUMBER_START_EQUALS_END(uiState = uiStateNumber.copy(
            inputTypes = Values(valueDirection = CountDown, startDate = today, endDate = today, startValue = "0", endValue = "100"))
        ),
        INVALID_NUMBER_START_IN_FUTURE(uiState = uiStateNumber.copy(
            inputTypes = Values(valueDirection = CountDown, startDate = tomorrow, endDate = today, startValue = "0", endValue = "100"))
        ),
        INVALID_NUMBER_END_IN_PAST(uiState = uiStateNumber.copy(
            inputTypes = Values(valueDirection = CountDown, startDate = today, endDate = yesterday, startValue = "0", endValue = "100"))
        ),
        INVALID_NUMBER_END_BEFORE_START(uiState = uiStateNumber.copy(
            inputTypes = Values(valueDirection = CountDown, startDate = tomorrow, endDate = yesterday, startValue = "0", endValue = "100"))
        )
    }

    enum class ValidUiStates(val uiState: UiState) {
        VALID_DAYS(uiState = uiStateDays),
        VALID_NUMBER(uiState = uiStateNumber)
    }

    @ParameterizedTest
    @EnumSource(InvalidUiState::class)
    fun `invalid ui state is considered invalid`(invalidUiState: InvalidUiState) {
        assertFalse(invalidUiState.uiState.saveEnabled)
    }

    @ParameterizedTest
    @EnumSource(ValidUiStates::class)
    fun `valid ui state is considered valid`(validUiState: ValidUiStates) {
        assertTrue(validUiState.uiState.saveEnabled)
    }

}