package tmg.hourglass.presentation.modify

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import tmg.hourglass.domain.enums.CountdownType
import tmg.hourglass.domain.model.Countdown
import tmg.hourglass.presentation.layouts.TitleBar
import tmg.hourglass.presentation.modify.layout.DataRangeDateLayout
import tmg.hourglass.presentation.modify.layout.DataRangeInputLayout
import tmg.hourglass.presentation.modify.layout.PersonaliseLayout
import tmg.hourglass.presentation.modify.layout.DataSingleDateLayout
import tmg.hourglass.presentation.modify.layout.SaveLayout
import tmg.hourglass.presentation.modify.layout.TypeLayout
import tmg.hourglass.strings.R
import java.time.LocalDateTime

@Composable
fun ModifyScreenVM(
    windowSizeClass: WindowSizeClass,
    actionUpClicked: () -> Unit,
    id: String?,
    viewModel: ModifyViewModel = hiltViewModel()
) {
    DisposableEffect(id) {
        Log.d("Modify", "Initialising VM with value $id")
        viewModel.initialise(id)
        return@DisposableEffect onDispose { }
    }

    val uiState = viewModel.uiState.collectAsState()
    ModifyScreen(
        windowSizeClass = windowSizeClass,
        actionUpClicked = actionUpClicked,
        isEdit = id != null,
        uiState = uiState.value,
        setTitle = viewModel::setTitle,
        setDescription = viewModel::setDescription,
        setColor = viewModel::setColor,
        setType = viewModel::setType,
        setStartDate = viewModel::setStartDate,
        setEndDate = viewModel::setEndDate,
        setStartValue = viewModel::setStartValue,
        setEndValue = viewModel::setEndValue,
        save = viewModel::save,
        delete = viewModel::delete
    )
}

@Composable
private fun ModifyScreen(
    windowSizeClass: WindowSizeClass,
    actionUpClicked: () -> Unit,
    isEdit: Boolean,
    uiState: UiState,
    setTitle: (String) -> Unit,
    setDescription: (String) -> Unit,
    setColor: (String) -> Unit,
    setType: (CountdownType) -> Unit,
    setStartDate: (LocalDateTime) -> Unit,
    setEndDate: (LocalDateTime) -> Unit,
    setStartValue: (String) -> Unit,
    setEndValue: (String) -> Unit,
    save: () -> Unit,
    delete: () -> Unit
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(scrollState),
    ) {
        TitleBar(
            title = stringResource(id = if (isEdit) R.string.modify_header_edit else R.string.modify_header_add),
            showBack = true,
            actionUpClicked = actionUpClicked
        )

        PersonaliseLayout(
            name = uiState.title,
            nameUpdated = setTitle,
            description = uiState.description,
            descriptionUpdated = setDescription,
            color = uiState.colorHex,
            colorPicked = setColor
        )

        TypeLayout(
            type = uiState.type,
            typeUpdated = setType
        )

        when (val inputData = uiState.inputTypes) {
            is UiState.Types.EndDate -> {
                DataSingleDateLayout(
                    date = inputData.finishDate,
                    dateUpdated = setEndDate
                )
            }
            is UiState.Types.Values -> {
                DataRangeDateLayout(
                    startDate = inputData.startDate,
                    startDateUpdated = setStartDate,
                    endDate = inputData.endDate,
                    endDateUpdated = setEndDate
                )

                DataRangeInputLayout(
                    initial = inputData.startValue,
                    initialUpdated = setStartValue,
                    finishing = inputData.endValue,
                    finishingUpdated = setEndValue
                )
            }
        }

        SaveLayout(
            isEdit = isEdit,
            saveEnabled = uiState.saveEnabled,
            saveClicked = {
                save()
                actionUpClicked()
            },
            deleteClicked = {
                delete()
                actionUpClicked()
            },
            cancelClicked = actionUpClicked
        )

        Spacer(Modifier.imePadding())
    }
}