package tmg.hourglass.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.threeten.bp.LocalDateTime
import tmg.hourglass.R
import tmg.hourglass.domain.data.CountdownInterpolator
import tmg.hourglass.domain.data.CountdownType
import tmg.hourglass.domain.data.models.Countdown
import tmg.hourglass.home.HomeItemAction
import tmg.hourglass.home.HomeItemType
import tmg.hourglass.home.HomeTab
import tmg.hourglass.theme.*
import tmg.hourglass.ui.home.views.HomeBottomBar
import tmg.hourglass.ui.home.views.HomeHeader
import tmg.hourglass.ui.home.views.HomeItem
import tmg.hourglass.ui.home.views.Tab
import tmg.hourglass.ui.home.views.Tab.NOW
import tmg.utilities.lifecycle.DataEvent
import tmg.utilities.lifecycle.Event

@Composable
fun HomeScreen(
    inputs: HomeViewModelInputs,
    outputs: HomeViewModelOutputs
) {
    val items = outputs.items.observeAsState(emptyList())
    val currentTab = outputs.currentTab.observeAsState(NOW)

    Scaffold(
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true,
        floatingActionButton = {
            Button(
                modifier = Modifier.clip(RoundedCornerShape(RadiusSmall)),
                onClick = { inputs.clickAdd() }
            ) {
                Icon(Icons.Filled.Add, contentDescription = stringResource(R.string.ab_add))
                Text(text = stringResource(id = R.string.ab_add))
            }
        },
        bottomBar = {
            HomeBottomBar(
                cutoutShape = RoundedCornerShape(RadiusSmall),
                selection = currentTab.value,
                tabClicked = {
                    inputs.switchList(it)
                }
            )
        },
        content = {
            LazyColumn(
                contentPadding = PaddingValues(bottom = 60.dp)
            ) {
                item {
                    HomeHeader()
                }
                itemsIndexed(items.value) { index, item ->
                    HomeItem(
                        item = item,
                        itemClicked = {

                        },
                        iconClicked = {

                        }
                    )
                }
            }
        }
    )
}

@Composable
@Preview
fun HomeScreenPreview() {
    val exampleHomeItem = HomeItemType.Item(
        countdown = Countdown(
            id = "test",
            name = "Car Insurance",
            description = "Lorem Ipsum",
            colour = "#E87034",
            start = LocalDateTime.MIN,
            end = LocalDateTime.MAX,
            initial = "0",
            finishing = "1000",
            countdownType = CountdownType.DAYS,
            interpolator = CountdownInterpolator.LINEAR
        ),
        action = HomeItemAction.DELETE,
        isEnabled = true,
        showDescription = true,
        clickBackground = true,
        animateBar = false
    )
    HomeScreen(
        inputs = object : HomeViewModelInputs {
            override fun clickAdd() { }
            override fun editItem(id: String) { }
            override fun deleteItem(id: String) { }
            override fun switchList(tab: Tab) { }
        },
        outputs = object : HomeViewModelOutputs {
            override val currentTab: LiveData<Tab> = MutableLiveData(Tab.PREVIOUS)
            override val items: LiveData<List<HomeItemType.Item>>
                get() = MutableLiveData(listOf(
                    exampleHomeItem,
                    exampleHomeItem.copy(action = HomeItemAction.EDIT)
                ))
        }
    )
}