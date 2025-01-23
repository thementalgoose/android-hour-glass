package tmg.hourglass.presentation.modify

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.threeten.bp.LocalDateTime
import tmg.hourglass.presentation.modify.ModifyData.uiStateDays
import tmg.hourglass.presentation.modify.ModifyData.uiStateNumber

internal class UiStateTest {

    enum class InvalidUiState(val uiState: UiState) {
        MISSING_TITLE(uiState = uiStateDays.copy(title = "")),
        MISSING_COLOR(uiState = uiStateDays.copy(colorHex = "")),
        MISSING_DAYS_FINISH_DATE(uiState = uiStateDays.copy(inputTypes = UiState.Types.EndDate(null))),
        INVALID_DAYS_FINISH_DATE(uiState = uiStateDays.copy(inputTypes = UiState.Types.EndDate(LocalDateTime.now().minusDays(1L))))
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