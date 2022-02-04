package tmg.hourglass.modify

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.res.stringResource
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.hourglass.R
import tmg.hourglass.base.BaseActivity
import tmg.hourglass.domain.enums.CountdownColors
import tmg.hourglass.domain.enums.CountdownType
import tmg.hourglass.modify.layout.*
import tmg.hourglass.presentation.AppTheme
import tmg.hourglass.presentation.layouts.TitleBar
import tmg.utilities.extensions.observeEvent

class ModifyActivity: BaseActivity() {

    private val viewModel: ModifyViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val id = intent?.extras?.getString(keyCountdownId)
        viewModel.inputs.initialise(id)

        setContent(id != null)

        observeEvent(viewModel.outputs.close) {
            finish()
        }
    }

    private fun setContent(isEdit: Boolean) {
        setContent {
            AppTheme {
                Scaffold(content = {
                    Column(
                        modifier = androidx.compose.ui.Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                    ) {
                        TitleBar(
                            title = when (isEdit) {
                                true -> stringResource(id = R.string.modify_header_edit)
                                false -> stringResource(id = R.string.modify_header_add)
                            },
                            backClicked = viewModel.inputs::backClicked
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
                            saveClicked = viewModel.inputs::saveClicked,
                            deleteClicked = viewModel.inputs::deleteClicked
                        )
                    }
                })
            }
        }
    }
    companion object {

        private const val keyCountdownId: String = "countdownId"

        fun intent(context: Context, passageId: String? = null): Intent {
            val intent = Intent(context, ModifyActivity::class.java)
            intent.putExtra(keyCountdownId, passageId)
            return intent
        }
    }
}