package tmg.hourglass.presentation.modify

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import tmg.hourglass.domain.model.Countdown
import tmg.hourglass.presentation.layouts.TitleBar
import tmg.hourglass.presentation.modify.layout.DataRangeDateLayout
import tmg.hourglass.presentation.modify.layout.DataRangeInputLayout
import tmg.hourglass.presentation.modify.layout.PersonaliseLayout
import tmg.hourglass.presentation.modify.layout.DataSingleDateLayout
import tmg.hourglass.presentation.modify.layout.SaveLayout
import tmg.hourglass.presentation.modify.layout.TypeLayout
import tmg.hourglass.strings.R

@Composable
fun ModifyScreen(
    paddingValues: PaddingValues,
    windowSizeClass: WindowSizeClass,
    actionUpClicked: () -> Unit,
    countdown: Countdown?,
    viewModel: ModifyViewModel = hiltViewModel()
) {
    DisposableEffect(countdown) {
        Log.d("Modify", "Initialising VM with value ${countdown?.id}")
        viewModel.initialise(countdown?.id)
        return@DisposableEffect onDispose { }
    }

    val uiState = viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(paddingValues),
    ) {
        TitleBar(
            title = stringResource(id = if (countdown != null) R.string.modify_header_edit else R.string.modify_header_add),
            showBack = true,
            actionUpClicked = actionUpClicked
        )

        PersonaliseLayout(
            name = uiState.value.title,
            nameUpdated = viewModel::setTitle,
            nameError = uiState.value.errors.any { it == UiState.ErrorTypes.TITLE_BLANK },
            description = uiState.value.description,
            descriptionUpdated = viewModel::setDescription,
            color = uiState.value.colorHex,
            colorPicked = viewModel::setColor
        )

        TypeLayout(
            type = uiState.value.type,
            typeUpdated = viewModel::setType
        )

        when (val inputData = uiState.value.inputTypes) {
            is UiState.Types.EndDate -> {
                val errorString = when {
                    uiState.value.errors.any { it == UiState.ErrorTypes.FINISH_DATE_NULL } -> stringResource(R.string.modify_error_finish_date_null)
                    uiState.value.errors.any { it == UiState.ErrorTypes.FINISH_DATE_IN_PAST } -> stringResource(R.string.modify_error_finish_date_in_past)
                    else -> null
                }
                DataSingleDateLayout(
                    day = inputData.day,
                    month = inputData.month,
                    year = inputData.year,
                    dayUpdated = viewModel::setEndDateDay,
                    monthUpdated = viewModel::setEndDateMonth,
                    yearUpdated = viewModel::setEndDateYear,
                    error = errorString
                )
            }
            is UiState.Types.Values -> {
                val errorDate = when {
                    uiState.value.errors.any { it == UiState.ErrorTypes.FINISH_DATE_NULL } -> stringResource(R.string.modify_error_finish_date_null)
                    uiState.value.errors.any { it == UiState.ErrorTypes.FINISH_DATE_IN_PAST } -> stringResource(R.string.modify_error_finish_date_in_past)
                    uiState.value.errors.any { it == UiState.ErrorTypes.START_DATE_NULL } -> stringResource(R.string.modify_error_start_date_null)
                    uiState.value.errors.any { it == UiState.ErrorTypes.FINISH_DATE_BEFORE_START_DATE } -> stringResource(R.string.modify_error_finish_date_before_start)
                    else -> null
                }
                DataRangeDateLayout(
                    startDate = inputData.startDate,
                    startDateUpdated = viewModel::setStartDate,
                    endDate = inputData.endDate,
                    endDateUpdated = {
                        viewModel.setEndDate(it.dayOfMonth, it.month, it.year)
                    },
                    error = errorDate
                )

                val errorValue = when {
                    uiState.value.errors.any { it == UiState.ErrorTypes.VALUES_EMPTY } -> stringResource(R.string.modify_error_value_empty)
                    uiState.value.errors.any { it == UiState.ErrorTypes.VALUES_MATCH } -> stringResource(R.string.modify_error_value_match)
                    uiState.value.errors.any { it == UiState.ErrorTypes.VALUES_MUST_BE_NUMBER } -> stringResource(R.string.modify_error_value_match)
                    else -> null
                }
                DataRangeInputLayout(
                    initial = inputData.startValue,
                    initialUpdated = viewModel::setStartValue,
                    finishing = inputData.endValue,
                    finishingUpdated = viewModel::setEndValue,
                    error = errorValue
                )
            }
        }

        SaveLayout(
            isEdit = countdown != null,
            saveEnabled = uiState.value.isSaveEnabled,
            saveClicked = {
                viewModel.save()
                actionUpClicked()
            },
            deleteClicked = {
                viewModel.delete()
                actionUpClicked()
            },
            cancelClicked = actionUpClicked
        )

        Spacer(Modifier.imePadding())
    }
}