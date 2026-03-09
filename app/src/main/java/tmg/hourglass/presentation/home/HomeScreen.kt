package tmg.hourglass.presentation.home

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import tmg.hourglass.core.crashlytics.screenview.ScreenView
import tmg.hourglass.domain.enums.CountdownColors
import tmg.hourglass.domain.enums.CountdownType
import tmg.hourglass.domain.model.Countdown
import tmg.hourglass.domain.model.Countdown.Companion.YYYY_MM_DD_FORMAT
import tmg.hourglass.domain.model.Tag
import tmg.hourglass.domain.model.TagOrdering
import tmg.hourglass.presentation.AppTheme
import tmg.hourglass.presentation.AppThemePreview
import tmg.hourglass.presentation.PreviewTablet
import tmg.hourglass.presentation.PreviewTheme
import tmg.hourglass.presentation.buttons.FloatingActionButton
import tmg.hourglass.presentation.home.components.Countdown
import tmg.hourglass.presentation.home.components.Empty
import tmg.hourglass.presentation.home.components.Header
import tmg.hourglass.presentation.home.components.TagHeader
import tmg.hourglass.presentation.textviews.TextHeader2
import tmg.hourglass.strings.R
import tmg.hourglass.strings.R.string
import java.time.LocalDateTime
import java.time.ZoneId

@Composable
internal fun HomeScreenVM(
    paddingValues: PaddingValues,
    windowSize: WindowSizeClass,
    navigateToAdd: () -> Unit,
    navigateToModify: (id: String) -> Unit,
    navigateToSettings: () -> Unit,
    navigateToTags: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    ScreenView("Home")
    val uiState = viewModel.uiState.collectAsState()

    HomeScreen(
        paddingValues = paddingValues,
        windowSizeClass = windowSize,
        uiState = uiState.value,
        tagSortUpdated = viewModel::tagSortUpdated,
        tagExpanded = viewModel::tagExpanded,
        untaggedSort = viewModel::untaggedSort,
        edit = { navigateToModify(it.id) },
        delete = viewModel::delete,
        openCreateNew = navigateToAdd,
        navigateToSettings = navigateToSettings,
        navigateToTags = navigateToTags
    )
}

@Composable
internal fun HomeScreen(
    paddingValues: PaddingValues,
    windowSizeClass: WindowSizeClass,
    uiState: UiState,
    tagSortUpdated: (Tag, TagOrdering) -> Unit,
    untaggedSort: (TagOrdering) -> Unit,
    tagExpanded: (Tag, Boolean) -> Unit,
    edit: (Countdown) -> Unit,
    delete: (Countdown) -> Unit,
    openCreateNew: () -> Unit,
    navigateToSettings: () -> Unit,
    navigateToTags: () -> Unit,
) {
    ListScreen(
        paddingValues = paddingValues,
        uiState = uiState,
        windowSizeClass = windowSizeClass,
        editItem = edit,
        deleteItem = delete,
        untaggedSort = untaggedSort,
        tagSortUpdated = tagSortUpdated,
        tagExpanded = tagExpanded,
        navigateToSettings = navigateToSettings,
        navigateToTags = navigateToTags
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
}

@Composable
internal fun ListScreen(
    paddingValues: PaddingValues,
    windowSizeClass: WindowSizeClass,
    navigateToSettings: () -> Unit,
    navigateToTags: () -> Unit,
    tagSortUpdated: (Tag, TagOrdering) -> Unit,
    untaggedSort: (TagOrdering) -> Unit,
    tagExpanded: (Tag, Boolean) -> Unit,
    uiState: UiState,
    editItem: (Countdown) -> Unit,
    deleteItem: (Countdown) -> Unit
) {
    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        columns = GridCells.Adaptive(minSize = 250.dp),
        contentPadding = PaddingValues(
            start = AppTheme.dimensions.paddingMedium,
            end = AppTheme.dimensions.paddingMedium,
            top = paddingValues.calculateTopPadding(),
            bottom = paddingValues.calculateBottomPadding()
        ),
        verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.paddingXSmall),
        horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.paddingXSmall),
        content = {
            item("header", span = { GridItemSpan(maxLineSpan) }) {
                Header(
                    modifier = Modifier.animateItem(),
                    windowSizeClass = windowSizeClass,
                    navigateToSettings = navigateToSettings,
                    navigateToTags = navigateToTags
                )
            }
            if (uiState.isEmpty) {
                item("empty", span = { GridItemSpan(maxLineSpan) }) {
                    Column(modifier = Modifier.animateItem()) {
                        if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact) {
                            Spacer(Modifier.height(AppTheme.dimensions.paddingLarge))
                        }
                        Empty()
                    }
                }
            }
            items(
                items = uiState.items,
                key = { it.id },
                span = {
                    when (it) {
                        is ListItem.CountdownItem -> GridItemSpan(1)
                        is ListItem.TagHeader -> GridItemSpan(maxLineSpan)
                        is ListItem.UntaggedHeader -> GridItemSpan(maxLineSpan)
                    }
                },
                itemContent = {
                    when (it) {
                        is ListItem.CountdownItem -> {
                            Countdown(
                                modifier = Modifier
                                    .animateItem(),
                                countdown = it.countdown,
                                editClicked = editItem,
                                deleteClicked = deleteItem
                            )
                        }
                        is ListItem.TagHeader -> {
                            TagHeader(
                                name = it.tag.name,
                                sortClicked = { sort ->
                                    tagSortUpdated(it.tag, sort)
                                },
                                expandClicked = { expanded ->
                                    tagExpanded(it.tag, expanded)
                                },
                                expanded = it.expand ?: false,
                                showCollapse = true,
                                modifier = Modifier
                                    .animateItem()
                                    .padding(top = AppTheme.dimensions.paddingNSmall),
                                sort = it.sort
                            )
                        }
                        is ListItem.UntaggedHeader -> {
                            TagHeader(
                                name = stringResource(R.string.title_all),
                                sortClicked = { sort ->
                                    untaggedSort(sort)
                                },
                                expandClicked = { },
                                expanded = false,
                                showCollapse = false,
                                modifier = Modifier
                                    .animateItem()
                                    .padding(top = AppTheme.dimensions.paddingNSmall),
                                sort = it.sort
                            )
                        }
                    }
                }
            )
            item("spacer", span = { GridItemSpan(maxLineSpan) }) {
                Spacer(modifier = Modifier.height(64.dp))
            }
            item("edgetoedge-footer", span = { GridItemSpan(maxLineSpan) }) {
                Spacer(Modifier.navigationBarsPadding())
            }
        }
    )
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
            edit = { },
            delete = { },
            openCreateNew = { },
            tagSortUpdated = { _, _ -> },
            tagExpanded = { _, _ -> },
            untaggedSort = { },
            navigateToSettings = { },
            navigateToTags = { }
        )
    }
}

@PreviewTheme
@PreviewTablet
@Composable
private fun PreviewUntagged() {
    AppThemePreview {
        HomeScreen(
            paddingValues = PaddingValues.Absolute(),
            windowSizeClass = compactWindowSizeClass,
            uiState = UiState.untagged(),
            edit = { },
            delete = { },
            openCreateNew = { },
            tagSortUpdated = { _, _ -> },
            tagExpanded = { _, _ -> },
            untaggedSort = { },
            navigateToSettings = { },
            navigateToTags = { }
        )
    }
}


@PreviewTheme
@PreviewTablet
@Composable
private fun PreviewTagged() {
    AppThemePreview {
        HomeScreen(
            paddingValues = PaddingValues.Absolute(),
            windowSizeClass = expandedWindowSizeClass,
            uiState = UiState.tagged(),
            edit = { },
            delete = { },
            openCreateNew = { },
            tagSortUpdated = { _, _ -> },
            tagExpanded = { _, _ -> },
            untaggedSort = { },
            navigateToSettings = { },
            navigateToTags = { }
        )
    }
}


private val compactWindowSizeClass: WindowSizeClass =
    WindowSizeClass.calculateFromSize(DpSize(400.dp, 600.dp))
private val expandedWindowSizeClass: WindowSizeClass =
    WindowSizeClass.calculateFromSize(DpSize(910.dp, 600.dp))

private fun UiState.Companion.empty(): UiState = UiState(
    items = emptyList()
)

private fun UiState.Companion.untagged(): UiState = UiState(
    items = listOf(
        ListItem.CountdownItem(
            countdown = fakeCountdownUpcoming
        ),
        ListItem.CountdownItem(
            countdown = fakeCountdownUpcoming.copy(id = "upcoming_2")
        )
    ))

private fun UiState.Companion.tagged(): UiState = UiState(
    items = listOf(
        ListItem.TagHeader(
            tag = Tag("tag1", "A", "", TagOrdering.ALPHABETICAL),
            expand = true,
            sort = TagOrdering.ALPHABETICAL
        ),
        ListItem.CountdownItem(
            countdown = fakeCountdownUpcoming.copy(id = "upcoming_1")
        ),
        ListItem.CountdownItem(
            countdown = fakeCountdownUpcoming.copy(id = "upcoming_2")
        ),
        ListItem.TagHeader(
            tag = Tag("tag2", "A", "", TagOrdering.ALPHABETICAL),
            expand = true,
            sort = TagOrdering.ALPHABETICAL
        ),
        ListItem.CountdownItem(
            countdown = fakeCountdownUpcoming.copy(id = "upcoming_3")
        ),
        ListItem.CountdownItem(
            countdown = fakeCountdownUpcoming.copy(id = "upcoming_4")
        )
    ))

private val fakeCountdownUpcoming = Countdown.Static(
    id = "upcoming_1",
    name = "Expired",
    description = "This is an expired countdown",
    colour = CountdownColors.COLOUR_1.hex,
    start = LocalDateTime.now(ZoneId.of("UTC")).minusDays(1).format(YYYY_MM_DD_FORMAT),
    end = LocalDateTime.now(ZoneId.of("UTC")).plusDays(2).format(YYYY_MM_DD_FORMAT),
    startValue = "0",
    endValue = "10000",
    countdownType = CountdownType.DAYS,
    tag = null
)