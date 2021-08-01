package tmg.hourglass.ui.home.views

import android.graphics.drawable.Icon
import android.widget.GridLayout
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.ArrowDownward
import androidx.compose.material.icons.outlined.ArrowUpward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Start
import androidx.compose.ui.Alignment.Companion.TopEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jakewharton.threetenabp.AndroidThreeTen
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import tmg.hourglass.R
import tmg.hourglass.domain.data.CountdownInterpolator
import tmg.hourglass.domain.data.CountdownType
import tmg.hourglass.domain.data.models.Countdown
import tmg.hourglass.home.HomeItemAction
import tmg.hourglass.home.HomeItemType
import tmg.hourglass.theme.AppTheme
import tmg.hourglass.theme.PaddingMedium
import tmg.hourglass.theme.PaddingSmall
import tmg.hourglass.utils.ProgressUtils.Companion.getProgress
import tmg.hourglass.views.LabelledProgressBar
import kotlin.coroutines.coroutineContext

@Composable
fun HomeItem(
    item: HomeItemType.Item,
    itemClicked: (HomeItemType.Item) -> Unit = { },
    iconClicked: (type: HomeItemAction) -> Unit = { }
) {

    val start = item.countdown.start.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))
    val end = item.countdown.end.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { itemClicked(item) }
            .padding(
                start = PaddingMedium,
                end = PaddingMedium,
                top = PaddingSmall,
                bottom = PaddingSmall
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
                onClick = { iconClicked(item.action) }
            ) {
                when (item.action) {
                    HomeItemAction.EDIT -> Icon(Icons.Filled.Edit, stringResource(R.string.ab_edit))
                    HomeItemAction.DELETE -> Icon(Icons.Filled.Delete, stringResource(R.string.ab_delete))
                    HomeItemAction.CHECK -> Icon(Icons.Filled.Check, stringResource(if (item.isEnabled) R.string.ab_selected else R.string.ab_not_selected))
                }
            }
        }
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = item.countdown.description
        )
        DataRow(
            icon = Icons.Outlined.ArrowUpward,
            message = stringResource(R.string.home_item_start, item.countdown.initial, start)
        )
        DataRow(
            icon = Icons.Outlined.ArrowDownward,
            message = stringResource(R.string.home_item_end, item.countdown.finishing, end)
        )
        LabelledProgressBar(
            progress = 0.5f, // getProgress(item.countdown),
            evaluator = { item.countdown.countdownType.converter("5") }
        )
    }
}

@Composable
private fun DataRow(
    icon: ImageVector,
    message: String
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(icon, contentDescription = null)
        Spacer(modifier = Modifier.width(PaddingMedium))
        Text(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .fillMaxWidth(),
            text = message
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
