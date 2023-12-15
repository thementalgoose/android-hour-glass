package tmg.hourglass.modify

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import tmg.hourglass.strings.R.string
import tmg.hourglass.domain.enums.CountdownColors
import tmg.hourglass.domain.enums.CountdownType
import tmg.hourglass.modify.layout.*
import tmg.hourglass.presentation.layouts.TitleBar

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ModifyScreenVM(
    id: String?,
    isEdit: Boolean,
    actionUpClicked: () -> Unit,
) {
    val viewModel = viewModel<ModifyViewModel>()
    viewModel.inputs.initialise(id)

    Scaffold(
        modifier = Modifier
            .systemBarsPadding(),
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                TitleBar(
                    title = when (isEdit) {
                        true -> stringResource(id = string.modify_header_edit)
                        false -> stringResource(id = string.modify_header_add)
                    },
                    backClicked = actionUpClicked
                )

                // Personalise
                val name = viewModel.outputs.name
                    .observeAsState("")
                val description = viewModel.outputs.description
                    .observeAsState("")
                val color = viewModel.outputs.color
                    .observeAsState(CountdownColors.COLOUR_1.hex)
                PersonaliseLayout(
                    name = name.value,
                    nameUpdated = viewModel.inputs::name,
                    description = description.value,
                    descriptionUpdated = viewModel.inputs::description,
                    color = color.value,
                    colorPicked = viewModel.inputs::color
                )

                // Type
                val type = viewModel.outputs.type.observeAsState()
                TypeLayout(
                    type = type.value ?: CountdownType.NUMBER,
                    typeUpdated = viewModel.inputs::type
                )

                // Range
                val initial = viewModel.outputs.initial
                    .observeAsState("")
                val finished = viewModel.outputs.finished
                    .observeAsState("")
                RangeLayout(
                    initial = initial.value,
                    initialUpdated = viewModel.inputs::initial,
                    finished = finished.value,
                    finishedUpdated = viewModel.inputs::finish
                )

                // Dates
                val start = viewModel.outputs.startDate.observeAsState()
                val end = viewModel.outputs.endDate.observeAsState()
                DatesLayout(
                    startDate = start.value,
                    startDateUpdated = viewModel.inputs::startDate,
                    endDate = end.value,
                    endDateUpdated = viewModel.inputs::endDate
                )

                // Save
                SaveLayout(
                    isEdit = isEdit,
                    saveEnabled = viewModel.outputs.saveEnabled.observeAsState(false).value,
                    saveClicked = {
                        viewModel.inputs.saveClicked()
                        actionUpClicked()
                    },
                    deleteClicked = {
                        viewModel.inputs.deleteClicked()
                        actionUpClicked()
                    }
                )
            }
        })
}