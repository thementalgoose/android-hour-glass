package tmg.hourglass.presentation.home

import androidx.annotation.StringRes
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import tmg.hourglass.BuildConfig
import java.time.LocalDateTime
import java.time.ZoneId
import tmg.hourglass.R
import tmg.hourglass.domain.enums.CountdownColors
import tmg.hourglass.domain.enums.CountdownInterpolator
import tmg.hourglass.domain.enums.CountdownType
import tmg.hourglass.domain.model.Countdown
import tmg.hourglass.domain.model.Countdown.Companion.YYYY_MM_DD_FORMAT
import tmg.hourglass.presentation.AppTheme
import tmg.hourglass.presentation.AppThemePreview
import tmg.hourglass.presentation.PreviewTablet
import tmg.hourglass.presentation.PreviewTheme
import tmg.hourglass.presentation.buttons.FloatingActionButton
import tmg.hourglass.presentation.home.components.Countdown
import tmg.hourglass.presentation.home.components.Empty
import tmg.hourglass.presentation.home.components.SortDialog
import tmg.hourglass.presentation.home.components.label
import tmg.hourglass.presentation.layouts.MasterDetailsPane
import tmg.hourglass.presentation.layouts.TitleBar
import tmg.hourglass.presentation.modify.ModifyScreen
import tmg.hourglass.presentation.textviews.TextBody2
import tmg.hourglass.presentation.textviews.TextHeader2
import tmg.hourglass.strings.R.string
import java.time.format.DateTimeFormatter

@Composable
internal fun HomeScreenVM(
    paddingValues: PaddingValues,
    windowSize: WindowSizeClass,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsState()

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.refresh()
    }

    HomeScreen(
        paddingValues = paddingValues,
        windowSizeClass = windowSize,
        uiState = uiState.value,
        sortUpdated = viewModel::updateSortOrder,
        edit = viewModel::edit,
        delete = viewModel::delete,
        openCreateNew = viewModel::createNew,
        closeDetailPane = viewModel::closeAction,
        navigateToSettings = viewModel::navigateToSettings,
        refresh = viewModel::refresh
    )
}

@Composable
internal fun HomeScreen(
    paddingValues: PaddingValues,
    windowSizeClass: WindowSizeClass,
    uiState: UiState,
    sortUpdated: (SortOrder) -> Unit,
    edit: (Countdown) -> Unit,
    delete: (Countdown) -> Unit,
    openCreateNew: () -> Unit,
    closeDetailPane: () -> Unit,
    navigateToSettings: () -> Unit,
    refresh: () -> Unit
) {
    MasterDetailsPane(
        windowSizeClass = windowSizeClass,
        master = {
            ListScreen(
                uiState = uiState,
                windowSizeClass = windowSizeClass,
                editItem = edit,
                deleteItem = delete,
                sortUpdated = sortUpdated,
                navigateToSettings = navigateToSettings
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .navigationBarsPadding()
                    .padding(AppTheme.dimensions.paddingMedium),
                contentAlignment = Alignment.BottomEnd
            ) {
                FloatingActionButton(
                    label = stringResource(id = string.dashboard_fab_new),
                    icon = Icons.Outlined.Add,
                    onClick = openCreateNew
                )
            }
        },
        detailsShow = uiState.action != null,
        details = {
            ModifyScreen(
                paddingValues = paddingValues,
                windowSizeClass = windowSizeClass,
                actionUpClicked = {
                    closeDetailPane()
                    refresh()
                },
                countdown = (uiState.action as? HomeAction.Modify)?.countdown
            )
        },
        detailsActionUpClicked = closeDetailPane
    )
}

@Composable
internal fun ListScreen(
    windowSizeClass: WindowSizeClass,
    navigateToSettings: () -> Unit,
    sortUpdated: (SortOrder) -> Unit,
    uiState: UiState,
    editItem: (Countdown) -> Unit,
    deleteItem: (Countdown) -> Unit
) {
    val sortOrderExpanded = remember { mutableStateOf(false) }

    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        columns = GridCells.Adaptive(minSize = 300.dp),
        content = {
            item(key = "edgetoedge-header", span = { GridItemSpan(maxLineSpan) }) {
                Spacer(Modifier.statusBarsPadding())
            }
            item("header", span = { GridItemSpan(maxLineSpan) }) {
                TitleBar(
                    modifier = Modifier.animateItem(),
                    title = stringResource(id = R.string.app_name),
                    overflowActions = {
                        IconButton(
                            onClick = {
                                sortOrderExpanded.value = true
                            },
                            content = {
                                Icon(
                                    painter = painterResource(R.drawable.ic_sort),
                                    contentDescription = stringResource(string.menu_sort),
                                    tint = AppTheme.colors.textPrimary
                                )
                            },
                        )
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
                    Column(modifier = Modifier.animateItem(),) {
                        if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact) {
                            Spacer(Modifier.height(AppTheme.dimensions.paddingLarge))
                        }
                        Empty()
                    }
                }
            } else {
                item("disclaimer", span = { GridItemSpan(maxLineSpan) }) {
                    val label = stringResource(uiState.sortOrder.label())
                    TextBody2(
                        fontStyle = FontStyle.Italic,
                        text = stringResource(string.menu_sort_order, label),
                        modifier = Modifier
                            .animateItem()
                            .padding(
                                horizontal = AppTheme.dimensions.paddingMedium,
                                vertical = AppTheme.dimensions.paddingXSmall
                            )
                    )
                }
            }
            items(uiState.itemsOrdered, key = { it.id }) {
                Countdown(
                    modifier = Modifier.animateItem(),
                    countdown = it,
                    editClicked = editItem.takeIf { BuildConfig.DEBUG },
                    deleteClicked = deleteItem
                )
            }
            item("spacer", span = { GridItemSpan(maxLineSpan) }) {
                Spacer(modifier = Modifier.height(64.dp))
            }
            item("edgetoedge-footer", span = { GridItemSpan(maxLineSpan) }) {
                Spacer(Modifier.navigationBarsPadding())
            }
        }
    )

    if (sortOrderExpanded.value) {
        SortDialog(
            sortUpdated = sortUpdated,
            dismissed = { sortOrderExpanded.value = false },
        )
    }
}

@Composable
private fun Header(
    @StringRes label: Int
) {
    TextHeader2(
        modifier = Modifier.padding(
            vertical = AppTheme.dimensions.paddingXSmall,
            horizontal = AppTheme.dimensions.paddingMedium
        ),
        text = stringResource(id = label)
    )
}

@PreviewTheme
@Composable
private fun PreviewEmpty() {
    AppThemePreview {
        HomeScreen(
            paddingValues = PaddingValues.Absolute(),
            windowSizeClass = compactWindowSizeClass,
            uiState = UiState.empty(),
            sortUpdated = { },
            edit = { },
            delete = { },
            openCreateNew = { },
            closeDetailPane = { },
            refresh = { },
            navigateToSettings = { }
        )
    }
}

@PreviewTheme
@Composable
private fun PreviewPopulated() {
    AppThemePreview {
        HomeScreen(
            paddingValues = PaddingValues.Absolute(),
            windowSizeClass = compactWindowSizeClass,
            uiState = UiState.upcoming(),
            sortUpdated = { },
            edit = { },
            delete = { },
            openCreateNew = { },
            closeDetailPane = { },
            refresh = { },
            navigateToSettings = { }
        )
    }
}


@PreviewTablet
@Composable
private fun PreviewPopulatedExpanded() {
    AppThemePreview {
        HomeScreen(
            paddingValues = PaddingValues.Absolute(),
            windowSizeClass = expandedWindowSizeClass,
            uiState = UiState.upcoming(),
            sortUpdated = { },
            edit = { },
            delete = { },
            openCreateNew = { },
            closeDetailPane = { },
            refresh = { },
            navigateToSettings = { }
        )
    }
}


private val compactWindowSizeClass: WindowSizeClass =
    WindowSizeClass.calculateFromSize(DpSize(400.dp, 600.dp))
private val expandedWindowSizeClass: WindowSizeClass =
    WindowSizeClass.calculateFromSize(DpSize(910.dp, 600.dp))

private fun UiState.Companion.empty(
    withAction: Boolean = false
): UiState = UiState(
    items = emptyList(),
    sortOrder = SortOrder.ALPHABETICAL,
    action = if (withAction) HomeAction.Add else null
)

private fun UiState.Companion.upcoming(
    withAction: Boolean = false,
    withUpcoming: Boolean = true,
): UiState = UiState(
    items = if (withUpcoming) listOf(fakeCountdownUpcoming, fakeCountdownUpcoming.copy(id = "upcoming_2")) else emptyList(),
    sortOrder = SortOrder.ALPHABETICAL,
    action = if (withAction) HomeAction.Add else null
)

private val fakeCountdownUpcoming = Countdown.Static(
    id = "upcoming_1",
    name = "Expired",
    description = "This is an expired countdown",
    colour = CountdownColors.COLOUR_1.hex,
    start = LocalDateTime.now(ZoneId.of("UTC")).minusDays(1).format(YYYY_MM_DD_FORMAT),
    end = LocalDateTime.now(ZoneId.of("UTC")).plusDays(2).format(YYYY_MM_DD_FORMAT),
    startValue = "0",
    endValue = "10000",
    countdownType = CountdownType.DAYS
)