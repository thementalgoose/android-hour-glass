package tmg.hourglass.dashboard

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import org.koin.androidx.viewmodel.ext.android.viewModel
import tmg.hourglass.R
import tmg.hourglass.base.BaseActivity
import tmg.hourglass.dashboard.layout.DeleteDialog
import tmg.hourglass.modify.ModifyActivity
import tmg.hourglass.presentation.AppTheme
import tmg.hourglass.settings.SettingsActivity
import tmg.utilities.extensions.observeEvent

class DashboardActivity: BaseActivity() {

    private val viewModel: DashboardViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent()

        observeEvent(viewModel.outputs.goToAdd) {
            startActivity(ModifyActivity.intent(this))
        }

        observeEvent(viewModel.outputs.goToEdit) { id ->
            startActivity(ModifyActivity.intent(this, id))
        }

        observeEvent(viewModel.outputs.goToSettings) {
            startActivity(SettingsActivity.intent(this))
        }
    }

    private fun setContent() {
        setContent {
            AppTheme {
                Scaffold(
                    floatingActionButton = {
                        FloatingActionButton(
                            shape = RoundedCornerShape(AppTheme.dimensions.radiusSmall),
                            backgroundColor = AppTheme.colors.primary,
                            onClick = viewModel.inputs::clickAdd
                        ) {
                            Icon(Icons.Outlined.Add, tint = Color.White, contentDescription = stringResource(id = R.string.ab_add))
                        }
                    },
                    content = {
                        val openDialog = remember { mutableStateOf(false) }
                        val selectedId = remember { mutableStateOf<String?>(null) }

                        val items = viewModel.outputs.upcoming.observeAsState()
                        DashboardLayout(
                            clickSettings = viewModel.inputs::clickSettings,
                            clickEdit = viewModel.inputs::clickEdit,
                            clickDelete = {
                                selectedId.value = it
                                openDialog.value = true
                            },
                            items = items
                        )

                        if (openDialog.value) {
                            DeleteDialog(
                                confirmed = {
                                    selectedId.value?.let {
                                        viewModel.inputs.clickDelete(it)
                                    }
                                    openDialog.value = false
                                    selectedId.value = null
                                },
                                dismissed = {
                                    openDialog.value = false
                                    selectedId.value = null
                                }
                            )
                        }
                    }
                )
            }
        }
    }
}