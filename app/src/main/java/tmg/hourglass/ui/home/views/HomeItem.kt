package tmg.hourglass.ui.home.views

import android.widget.GridLayout
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Start
import androidx.compose.ui.Alignment.Companion.TopEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jakewharton.threetenabp.AndroidThreeTen
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import tmg.hourglass.R
import tmg.hourglass.domain.data.CountdownInterpolator
import tmg.hourglass.domain.data.CountdownType
import tmg.hourglass.domain.data.models.Countdown
import tmg.hourglass.home.HomeItemAction
import tmg.hourglass.home.HomeItemType
import tmg.hourglass.theme.AppTheme
import tmg.hourglass.utils.ProgressUtils.Companion.getProgress
import tmg.hourglass.views.LabelledProgressBar
import kotlin.coroutines.coroutineContext

@Composable
fun HomeItem(
    item: HomeItemType.Item,
    itemClicked: (HomeItemType.Item) -> Unit = { },
    iconClicked: (type: HomeItemAction) -> Unit = { }
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 16.dp,
                end = 16.dp,
                top = 8.dp,
                bottom = 8.dp
            )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .weight(1f)
                    .wrapContentHeight(),
                text = item.countdown.name
            )
            IconButton(
                onClick = { itemClicked(item) }
            ) {
                when (item.action) {
                    HomeItemAction.EDIT -> Icon(Icons.Filled.Edit, stringResource(R.string.ab_edit))
                    HomeItemAction.DELETE -> Icon(Icons.Filled.Delete, stringResource(R.string.ab_delete))
                    HomeItemAction.CHECK -> Icon(Icons.Filled.Check, stringResource(if (item.isEnabled) R.string.ab_selected else R.string.ab_not_selected))
                }
            }
        }
        Text(
            modifier = Modifier.fillMaxWidth(1.0f),
            text = item.countdown.description
        )
        LabelledProgressBar(
            progress = 0.5f, // getProgress(item.countdown),
            evaluator = { item.countdown.countdownType.converter("5") }
        )
    }
}

@Composable
@Preview
private fun Preview() {
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
    AppTheme {
        HomeItem(
            item = exampleHomeItem,
            itemClicked = { },
            iconClicked = { }
        )
    }
}
