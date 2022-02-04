package tmg.hourglass.dashboard

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import tmg.hourglass.R
import tmg.hourglass.dashboard.layout.*
import tmg.hourglass.domain.model.Countdown
import tmg.hourglass.domain.model.preview
import tmg.hourglass.presentation.AppThemePreview

@Composable
fun DashboardLayout(
    clickSettings: () -> Unit,
    clickEdit: (String) -> Unit,
    clickDelete: (String) -> Unit,
    items: State<List<Countdown>?>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        content = {
            item {
                DashboardHeaderLayout(
                    clickSettings = clickSettings
                )
            }
            if (items.value?.isNotEmpty() == true) {
                if (items.value!!.any { !it.isFinished}) {
                    item {
                        DashboardSectionLayout(text = stringResource(id = R.string.dashboard_title_upcoming))
                    }
                    items(items.value!!.filter { !it.isFinished }) {
                        DashboardCountdownLayout(
                            countdown = it,
                            editClicked = clickEdit,
                            deleteClicked = clickDelete
                        )
                    }
                }
                if (items.value!!.any { it.isFinished }) {
                    item {
                        DashboardSectionLayout(text = stringResource(id = R.string.dashboard_title_previous))
                    }
                    items(items.value!!.filter { it.isFinished }) {
                        DashboardCountdownLayout(
                            countdown = it,
                            editClicked = clickEdit,
                            deleteClicked = clickDelete
                        )
                    }
                }
            }
            else {
                item {
                    PlaceholderLayout()
                }
            }
        }
    )
}

@Preview
@Composable
private fun PreviewItems() {
    AppThemePreview {
        val sample = remember { mutableStateOf(listOf(Countdown.preview())) }
        DashboardLayout(
            clickSettings = {  },
            clickEdit = {  },
            clickDelete = {  },
            items = sample
        )
    }
}