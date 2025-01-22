package tmg.hourglass.presentation.dashboard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import tmg.hourglass.R
import tmg.hourglass.domain.enums.CountdownColors
import tmg.hourglass.domain.enums.CountdownInterpolator
import tmg.hourglass.domain.enums.CountdownType
import tmg.hourglass.domain.model.Countdown
import tmg.hourglass.presentation.AppTheme
import tmg.hourglass.presentation.AppThemePreview
import tmg.hourglass.presentation.PreviewTablet
import tmg.hourglass.presentation.PreviewTheme
import tmg.hourglass.presentation.dashboard.components.Countdown
import tmg.hourglass.presentation.dashboard.components.Empty
import tmg.hourglass.presentation.layouts.MasterDetailsPane
import tmg.hourglass.presentation.layouts.TitleBar
import tmg.hourglass.presentation.modify.ModifyScreen
import tmg.hourglass.presentation.textviews.TextBody1
import tmg.hourglass.presentation.textviews.TextHeader2
import tmg.hourglass.strings.R.string

@Composable
internal fun DashboardScreen(
    windowSizeClass: WindowSizeClass,
    navigateToSettings: () -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsState()

    DashboardScreen(
        windowSizeClass = windowSizeClass,
        uiState = uiState.value,
        navigateToSettings = navigateToSettings,
        edit = viewModel::edit,
        delete = viewModel::delete,
        openCreateNew = viewModel::createNew,
        closeDetailPane = viewModel::closeAction,
        refresh = viewModel::refresh
    )
}

@Composable
internal fun DashboardScreen(
    windowSizeClass: WindowSizeClass,
    uiState: UiState,
    navigateToSettings: () -> Unit,
    edit: (Countdown) -> Unit,
    delete: (Countdown) -> Unit,
    openCreateNew: () -> Unit,
    closeDetailPane: () -> Unit,
    refresh: () -> Unit
) {
    MasterDetailsPane(
        windowSizeClass = windowSizeClass,
        master = {
            ListScreen(
                uiState = uiState,
                navigateToSettings = navigateToSettings,
                windowSizeClass = windowSizeClass,
                editItem = edit,
                deleteItem = delete
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(AppTheme.dimensions.paddingMedium),
                contentAlignment = Alignment.BottomEnd
            ) {
                NewFAB(onClick = openCreateNew)
            }
        },
        detailsShow = uiState.action != null,
        details = {
            ModifyScreen(
                windowSizeClass = windowSizeClass,
                actionUpClicked = {
                    closeDetailPane()
                    refresh()
                },
                countdown = (uiState.action as? DashboardAction.Modify)?.countdown
            )
        },
        detailsActionUpClicked = closeDetailPane
    )
}

@Composable
internal fun ListScreen(
    windowSizeClass: WindowSizeClass,
    uiState: UiState,
    navigateToSettings: () -> Unit,
    editItem: (Countdown) -> Unit,
    deleteItem: (Countdown) -> Unit
) {
    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        columns = GridCells.Adaptive(minSize = 300.dp),
        content = {
            item("header", span = { GridItemSpan(maxLineSpan) }) {
                TitleBar(
                    title = stringResource(id = R.string.app_name),
                    showSpace = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact,
                    overflowActions = {
                        if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact) {
                            IconButton(
                                onClick = navigateToSettings,
                                content = {
                                    Icon(
                                        imageVector = Icons.Outlined.Settings,
                                        contentDescription = stringResource(string.menu_settings),
                                        tint = AppTheme.colors.textPrimary
                                    )
                                }
                            )
                        }
                    }
                )
            }
            if (uiState.isEmpty) {
                item("empty", span = { GridItemSpan(maxLineSpan) }) {
                    Empty(
                        modifier = Modifier.padding(
                            top = if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact) AppTheme.dimensions.paddingLarge else 0.dp
                        )
                    )
                }
            }
            if (uiState.expired.isNotEmpty()) {
                item("expired", span = { GridItemSpan(maxLineSpan) }) {
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
                item("upcoming", span = { GridItemSpan(maxLineSpan) }) {
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
            item("spacer", span = { GridItemSpan(maxLineSpan) }) {
                Spacer(modifier = Modifier.height(64.dp))
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
                textColor = AppTheme.colors.onAccent,
            )
        },
        icon = {
            Icon(
                imageVector = Icons.Outlined.Add,
                contentDescription = null,
                tint = AppTheme.colors.onAccent
            )
        },
        containerColor = AppTheme.colors.accent,
        onClick = onClick
    )
}


@PreviewTheme
@Composable
private fun PreviewEmpty() {
    AppThemePreview {
        DashboardScreen(
            windowSizeClass = compactWindowSizeClass,
            uiState = UiState.empty(),
            navigateToSettings = { },
            edit = { },
            delete = { },
            openCreateNew = { },
            closeDetailPane = { }) {
        }
    }
}

@PreviewTheme
@Composable
private fun PreviewPopulated() {
    AppThemePreview {
        DashboardScreen(
            windowSizeClass = compactWindowSizeClass,
            uiState = UiState.upcoming(),
            navigateToSettings = { },
            edit = { },
            delete = { },
            openCreateNew = { },
            closeDetailPane = { }) {
        }
    }
}


@PreviewTablet
@Composable
private fun PreviewPopulatedExpanded() {
    AppThemePreview {
        DashboardScreen(
            windowSizeClass = expandedWindowSizeClass,
            uiState = UiState.upcoming(),
            navigateToSettings = { },
            edit = { },
            delete = { },
            openCreateNew = { },
            closeDetailPane = { }) {
        }
    }
}


private val compactWindowSizeClass: WindowSizeClass =
    WindowSizeClass.calculateFromSize(DpSize(400.dp, 600.dp))
private val expandedWindowSizeClass: WindowSizeClass =
    WindowSizeClass.calculateFromSize(DpSize(910.dp, 600.dp))

private fun UiState.Companion.empty(
    withAction: Boolean = false
): UiState = UiState(
    upcoming = emptyList(),
    expired = emptyList(),
    action = if (withAction) DashboardAction.Add else null
)

private fun UiState.Companion.upcoming(
    withAction: Boolean = false,
    withUpcoming: Boolean = true,
    withExpired: Boolean = false
): UiState = UiState(
    upcoming = if (withUpcoming) listOf(fakeCountdownUpcoming) else emptyList(),
    expired = if (withExpired) listOf(fakeCountdownExpired) else emptyList(),
    action = if (withAction) DashboardAction.Add else null
)

private val fakeCountdownExpired = Countdown(
    id = "expired",
    name = "Expired",
    description = "This is an expired countdown",
    colour = CountdownColors.COLOUR_1.hex,
    start = LocalDateTime.now(ZoneId.of("UTC")).minusDays(2),
    end = LocalDateTime.now(ZoneId.of("UTC")).minusDays(1),
    initial = "0",
    finishing = "10000",
    countdownType = CountdownType.DAYS,
    interpolator = CountdownInterpolator.LINEAR,
    notifications = emptyList()
)
private val fakeCountdownUpcoming = Countdown(
    id = "expired",
    name = "Expired",
    description = "This is an expired countdown",
    colour = CountdownColors.COLOUR_1.hex,
    start = LocalDateTime.now(ZoneId.of("UTC")).minusDays(1),
    end = LocalDateTime.now(ZoneId.of("UTC")).plusDays(2),
    initial = "0",
    finishing = "10000",
    countdownType = CountdownType.DAYS,
    interpolator = CountdownInterpolator.LINEAR,
    notifications = emptyList()
)