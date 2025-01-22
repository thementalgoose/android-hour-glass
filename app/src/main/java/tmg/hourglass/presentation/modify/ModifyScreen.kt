package tmg.hourglass.presentation.modify

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
            .verticalScroll(scrollState),
    ) {
        TitleBar(
            title = stringResource(id = if (countdown != null) R.string.modify_header_edit else R.string.modify_header_add),
            showBack = true,
            actionUpClicked = actionUpClicked
        )

        PersonaliseLayout(
            name = uiState.value.title,
            nameUpdated = viewModel::setTitle,
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
                DataSingleDateLayout(
                    date = inputData.finishDate,
                    dateUpdated = viewModel::setEndDate
                )
            }
            is UiState.Types.Values -> {
                DataRangeDateLayout(
                    startDate = inputData.startDate,
                    startDateUpdated = viewModel::setStartDate,
                    endDate = inputData.finishDate,
                    endDateUpdated = viewModel::setEndDate
                )

                DataRangeInputLayout(
                    initial = inputData.initial,
                    initialUpdated = viewModel::setStartValue,
                    finishing = inputData.finishing,
                    finishingUpdated = viewModel::setEndValue
                )
            }
        }

        SaveLayout(
            isEdit = countdown != null,
            saveEnabled = uiState.value.saveEnabled,
            saveClicked = {
                viewModel.save()
                actionUpClicked()
            },
            deleteClicked = viewModel::delete
        )
    }
}