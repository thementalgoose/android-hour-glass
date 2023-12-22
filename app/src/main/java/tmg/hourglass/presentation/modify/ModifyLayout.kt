package tmg.hourglass.presentation.modify

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import org.threeten.bp.LocalDateTime
import tmg.hourglass.R
import tmg.hourglass.strings.R.string
import tmg.hourglass.domain.enums.CountdownType
import tmg.hourglass.domain.model.Countdown
import tmg.hourglass.presentation.modify.layout.DatesLayout
import tmg.hourglass.presentation.modify.layout.PersonaliseLayout
import tmg.hourglass.presentation.modify.layout.RangeLayout
import tmg.hourglass.presentation.modify.layout.SaveLayout
import tmg.hourglass.presentation.modify.layout.TypeLayout
import tmg.hourglass.presentation.layouts.TitleBar


@Composable
fun ModifyScreen(
    actionUpClicked: () -> Unit,
    countdown: Countdown?,
    viewModel: ModifyViewModel = hiltViewModel()
) {
    DisposableEffect(countdown) {
        Log.d("Modify", "Initialising VM with value ${countdown?.id}")
        viewModel.inputs.initialise(countdown?.id)
        return@DisposableEffect onDispose { }
    }

    val name = viewModel.outputs.name.collectAsState()
    val description = viewModel.outputs.description.collectAsState()
    val colour = viewModel.outputs.color.collectAsState()
    val type = viewModel.outputs.type.collectAsState()
    val initial = viewModel.outputs.initial.collectAsState()
    val finished = viewModel.outputs.finished.collectAsState()
    val startDate = viewModel.outputs.startDate.collectAsState()
    val endDate = viewModel.outputs.endDate.collectAsState()
    val save = viewModel.outputs.saveEnabled.collectAsState()

    ModifyScreen(
        actionUpClicked = actionUpClicked,
        countdown = countdown,
        name = name.value,
        nameUpdated = viewModel.inputs::name,
        description = description.value,
        descriptionUpdated = viewModel.inputs::description,
        colour = colour.value,
        colourUpdated = viewModel.inputs::color,
        type = type.value,
        typeUpdated = viewModel.inputs::type,
        initial = initial.value,
        initialUpdated = viewModel.inputs::initial,
        finished = finished.value,
        finishedUpdated = viewModel.inputs::finish,
        start = startDate.value,
        startUpdated = viewModel.inputs::startDate,
        end = endDate.value,
        endUpdated = viewModel.inputs::endDate,
        saveEnabled = save.value,
        saveClicked = viewModel.inputs::saveClicked,
        deleteClicked = viewModel.inputs::deleteClicked
    )
}

@Composable
fun ModifyScreen(
    actionUpClicked: () -> Unit,
    countdown: Countdown?,
    name: String,
    nameUpdated: (String) -> Unit,
    description: String,
    descriptionUpdated: (String) -> Unit,
    colour: String,
    colourUpdated: (String) -> Unit,
    type: CountdownType?,
    typeUpdated: (CountdownType) -> Unit,
    initial: String,
    initialUpdated: (String) -> Unit,
    finished: String,
    finishedUpdated: (String) -> Unit,
    start: LocalDateTime?,
    startUpdated: (LocalDateTime) -> Unit,
    end: LocalDateTime?,
    endUpdated: (LocalDateTime) -> Unit,
    saveEnabled: Boolean,
    saveClicked: () -> Unit,
    deleteClicked: () -> Unit
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
    ) {
        TitleBar(
            title = stringResource(id = if (countdown != null) string.modify_header_edit else string.modify_header_add),
            backClicked = actionUpClicked
        )
        PersonaliseLayout(
            name = name,
            nameUpdated = nameUpdated,
            description = description,
            descriptionUpdated = descriptionUpdated,
            color = colour,
            colorPicked = colourUpdated
        )
        TypeLayout(
            type = type ?: CountdownType.NUMBER,
            typeUpdated = typeUpdated
        )
        RangeLayout(
            initial = initial,
            initialUpdated = initialUpdated,
            finished = finished,
            finishedUpdated = finishedUpdated
        )
        DatesLayout(
            startDate = start,
            startDateUpdated = startUpdated,
            endDate = end,
            endDateUpdated = endUpdated
        )
        SaveLayout(
            isEdit = countdown != null,
            saveEnabled = saveEnabled,
            saveClicked = {
                saveClicked()
                actionUpClicked()
            },
            deleteClicked = {
                deleteClicked()
                actionUpClicked()
            }
        )
    }
}