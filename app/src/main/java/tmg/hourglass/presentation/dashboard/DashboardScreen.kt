package tmg.hourglass.presentation.dashboard

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import tmg.hourglass.R
import tmg.hourglass.presentation.layouts.MasterDetailsPane
import tmg.hourglass.presentation.layouts.TitleBar
import tmg.hourglass.presentation.textviews.TextBody1
import tmg.hourglass.presentation.textviews.TextBody2

@Composable
internal fun DashboardScreen(
    windowSizeClass: WindowSizeClass,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsState()

    MasterDetailsPane(
        windowSizeClass = windowSizeClass,
        master = {
            ListScreen(
                uiState = uiState.value,
                windowSizeClass = windowSizeClass,
            )
        },
        detailsShow = uiState.value.action != null,
        details = {

        },
        detailsActionUpClicked = viewModel::closeAction
    )
}

@Composable
internal fun ListScreen(
    windowSizeClass: WindowSizeClass,
    uiState: UiState
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        content = {
            item("header") {
                TitleBar(title = stringResource(id = R.string.app_name))
            }
            items(uiState.upcoming, key = { it.id }) {
                TextBody1(text = it.id)
            }
            items(uiState.expired, key = { it.id }) {
                TextBody2(text = it.id)
            }
        }
    )
}

