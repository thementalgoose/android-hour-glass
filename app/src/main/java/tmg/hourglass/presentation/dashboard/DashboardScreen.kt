package tmg.hourglass.presentation.dashboard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import tmg.hourglass.R
import tmg.hourglass.domain.model.Countdown
import tmg.hourglass.strings.R.string
import tmg.hourglass.presentation.AppTheme
import tmg.hourglass.presentation.HomeTab
import tmg.hourglass.presentation.dashboard.components.Countdown
import tmg.hourglass.presentation.modify.ModifyScreen
import tmg.hourglass.presentation.layouts.MasterDetailsPane
import tmg.hourglass.presentation.layouts.TitleBar
import tmg.hourglass.presentation.textviews.TextBody1
import tmg.hourglass.presentation.textviews.TextBody2
import tmg.hourglass.presentation.textviews.TextHeader2

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
                editItem = viewModel::edit,
                deleteItem = viewModel::delete
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(AppTheme.dimensions.paddingMedium),
                contentAlignment = Alignment.BottomEnd
            ) {
                NewFAB(onClick = viewModel::createNew)
            }
        },
        detailsShow = uiState.value.action != null,
        details = {
            ModifyScreen(
                actionUpClicked = {
                    viewModel.closeAction()
                    viewModel.refresh()
                },
                countdown = (uiState.value.action as? DashboardAction.Modify)?.countdown
            )
        },
        detailsActionUpClicked = viewModel::closeAction
    )
}

@Composable
internal fun ListScreen(
    windowSizeClass: WindowSizeClass,
    uiState: UiState,
    editItem: (Countdown) -> Unit,
    deleteItem: (Countdown) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        content = {
            item("header") {
                TitleBar(title = stringResource(id = R.string.app_name))
            }
            if (uiState.expired.isNotEmpty()) {
                item("expired") {
                    TextHeader2(
                        modifier = Modifier.padding(
                            vertical = AppTheme.dimensions.paddingXSmall,
                            horizontal = AppTheme.dimensions.paddingMedium
                        ),
                        text = stringResource(id = string.dashboard_title_previous)
                    )
                }
            }
            items(uiState.expired, key = { it.id }) {
                Countdown(
                    countdown = it,
                    editClicked = null,
                    deleteClicked = deleteItem
                )
            }
            if (uiState.upcoming.isNotEmpty()) {
                item("upcoming") {
                    TextHeader2(
                        modifier = Modifier.padding(
                            vertical = AppTheme.dimensions.paddingXSmall,
                            horizontal = AppTheme.dimensions.paddingMedium
                        ),
                        text = stringResource(id = string.dashboard_title_upcoming)
                    )
                }
            }
            items(uiState.upcoming, key = { it.id }) {
                Countdown(
                    countdown = it,
                    editClicked = editItem,
                    deleteClicked = deleteItem
                )
            }
        }
    )
}

@Composable
private fun NewFAB(
    onClick: () -> Unit
) {
    ExtendedFloatingActionButton(
        text = {
            TextBody1(
                text = stringResource(id = string.dashboard_fab_new),
                textColor = Color.White,
            )
        },
        icon = {
            Icon(
                imageVector = Icons.Outlined.Add,
                contentDescription = null,
                tint = Color.White
            )
        },
        containerColor = AppTheme.colors.accent,
        onClick = onClick
    )
}