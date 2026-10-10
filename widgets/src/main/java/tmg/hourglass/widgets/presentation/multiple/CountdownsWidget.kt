package tmg.hourglass.widgets.presentation.multiple

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.LinearProgressIndicator
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.unit.ColorProvider
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import androidx.glance.GlanceTheme
import androidx.glance.LocalContext
import androidx.glance.preview.ExperimentalGlancePreviewApi
import androidx.glance.preview.Preview
import java.time.LocalDate
import tmg.hourglass.domain.enums.CountdownType
import tmg.hourglass.domain.model.Countdown
import tmg.hourglass.domain.model.WidgetReference
import tmg.hourglass.domain.utils.ProgressUtils
import tmg.hourglass.strings.R.string
import tmg.hourglass.widgets.di.WidgetsEntryPoints
import tmg.hourglass.widgets.presentation.CountdownWidgetTheming
import tmg.hourglass.widgets.presentation.OpenApp
import tmg.hourglass.widgets.presentation.getCountdownWidgetColors
import tmg.hourglass.widgets.utils.appWidgetId
import tmg.hourglass.widgets.utils.fromHex

class CountdownsWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val widgetConnector = WidgetsEntryPoints.get(context = context).widgetConnector()
        val countdownConnector = WidgetsEntryPoints.get(context = context).countdownConnector()

        provideContent {
            val theming = getCountdownWidgetColors()

            Log.i("CountdownsWidget", "provideContent App Widget Id ${id.appWidgetId}")
            val widgetRefState = widgetConnector.get(id.appWidgetId).collectAsState(null)
            val allCountdownsState = countdownConnector.all().collectAsState(null)

            val widgetRef = widgetRefState.value
            val allCountdowns = allCountdownsState.value

            val action = if (widgetRef?.openAppOnClick == true) {
                actionRunCallback<OpenApp>()
            } else {
                actionRunCallback<OpenApp>() // Default to opening app on item click
            }

            if (allCountdowns == null) {
                NoCountdowns(
                    modifier = GlanceModifier.clickable(action),
                    theming = theming,
                    context = context
                )
                return@provideContent
            }

            val filteredList = when (widgetRef) {
                is WidgetReference.Multiple -> {
                    if (widgetRef.tagId != null) {
                        allCountdowns.filter { it.tag?.tagId == widgetRef.tagId }
                    } else {
                        allCountdowns
                    }
                }
                else -> allCountdowns
            }.sortedBy { it.endDate }

            if (filteredList.isEmpty()) {
                NoCountdowns(
                    modifier = GlanceModifier.clickable(action),
                    theming = theming,
                    context = context
                )
            } else {
                CountdownsList(
                    countdowns = filteredList,
                    theming = theming,
                    action = action
                )
            }
        }
    }
}

@Composable
internal fun CountdownsList(
    countdowns: List<Countdown>,
    theming: CountdownWidgetTheming,
    action: androidx.glance.action.Action = actionRunCallback<OpenApp>(),
    modifier: GlanceModifier = GlanceModifier
) {

    if (countdowns.size > 1) {
        LazyColumn(
            modifier = modifier
                .surface(theming.backgroundColor)
        ) {
            item {
                Spacer(GlanceModifier.height(8.dp))
            }
            items(countdowns, itemId = { it.id.hashCode().toLong() }) { countdown ->
                CountdownRow(
                    countdownModel = countdown,
                    theming = theming,
                    modifier = GlanceModifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp, start = 8.dp, end = 8.dp)
                        .clickable(action)
                )
            }
        }
    } else {
        CountdownBar(
            countdownModel = countdowns.first(),
            theming = theming,
            modifier = GlanceModifier
                .fillMaxWidth()
                .clickable(action)
        )
    }
}

@Composable
internal fun CountdownRow(
    countdownModel: Countdown,
    theming: CountdownWidgetTheming,
    modifier: GlanceModifier = GlanceModifier
) {
    val (progress, label) = countdownModel.getProgressAndInfo()
    Column(
        modifier = modifier
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = GlanceModifier
                .fillMaxWidth()
                .padding(bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val titleText = if (!countdownModel.emoji.isNullOrBlank()) {
                "${countdownModel.emoji} ${countdownModel.name}"
            } else {
                countdownModel.name
            }
            Text(
                text = titleText,
                modifier = GlanceModifier
                    .defaultWeight()
                    .padding(end = 4.dp),
                maxLines = 1,
                style = theming.title.copy(
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start
                ),
            )
            Text(
                text = label,
                style = theming.content.copy(
                    textAlign = TextAlign.End
                )
            )
        }
        Row(
            modifier = GlanceModifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            LinearProgressIndicator(
                modifier = GlanceModifier
                    .fillMaxWidth()
                    .height(32.dp)
                    .cornerRadius(16.dp),
                progress = progress,
                backgroundColor = theming.barBackgroundColor,
                color = ColorProvider(Color.fromHex(countdownModel.colour))
            )
        }
    }
}



@Composable
internal fun CountdownBar(
    countdownModel: Countdown,
    theming: CountdownWidgetTheming,
    modifier: GlanceModifier = GlanceModifier
) {
    val (progress, label) = countdownModel.getProgressAndInfo()
    Column(
        modifier = modifier
            .surface(theming.backgroundColor)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = GlanceModifier
                .fillMaxWidth()
                .padding(4.dp)
                .defaultWeight(),
            verticalAlignment = Alignment.Top
        ) {
            val titleText = if (!countdownModel.emoji.isNullOrBlank()) {
                "${countdownModel.emoji} ${countdownModel.name}"
            } else {
                countdownModel.name
            }
            Text(
                text = titleText,
                modifier = GlanceModifier.defaultWeight()
                    .padding(end = 4.dp),
                maxLines = 1,
                style = theming.title.copy(
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start
                ),
            )
            Text(
                text = label,
                style = theming.content.copy(
                    textAlign = TextAlign.End
                )
            )
        }
        Row(
            modifier = GlanceModifier
                .fillMaxWidth()
                .defaultWeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            LinearProgressIndicator(
                modifier = GlanceModifier
                    .fillMaxWidth()
                    .height(32.dp)
                    .cornerRadius(16.dp),
                progress = progress,
                backgroundColor = theming.barBackgroundColor,
                color = ColorProvider(Color.fromHex(countdownModel.colour))
            )
        }
    }
}

@Composable
internal fun NoCountdowns(
    modifier: GlanceModifier = GlanceModifier,
    theming: CountdownWidgetTheming,
    context: Context = LocalContext.current
) {
    Box(
        modifier = modifier.surface(theming.backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Text(
            style = theming.title,
            text = context.getString(string.widget_value)
        )
    }
}

@Composable
private fun GlanceModifier.surface(color: ColorProvider): GlanceModifier = this
    .fillMaxSize()
    .background(color)
    .padding(0.dp)

private fun Countdown.getProgressAndInfo(): Pair<Float, String> {
    val progress = ProgressUtils.getProgress(this)
    val label = this.getLabel(progress)
    return Pair(progress, label)
}

// MARK: - Previews

private val previewCountdowns = listOf(
    Countdown.Static(
        id = "1",
        name = "Holiday",
        description = "Trip to Tokyo",
        colour = "#4CAF50",
        emoji = "✈️",
        start = LocalDate.now().minusDays(10).toString(),
        end = LocalDate.now().plusDays(20).toString(),
        startValue = "0",
        endValue = "30",
        countdownType = CountdownType.DAYS,
        tag = null
    ),
    Countdown.Static(
        id = "2",
        name = "Birthday",
        description = "Party time",
        colour = "#2196F3",
        emoji = "🎂",
        start = LocalDate.now().minusDays(5).toString(),
        end = LocalDate.now().plusDays(45).toString(),
        startValue = "0",
        endValue = "50",
        countdownType = CountdownType.DAYS,
        tag = null
    ),
    Countdown.Static(
        id = "3",
        name = "Marathon",
        description = "Race day",
        colour = "#FF9800",
        emoji = "🏃",
        start = LocalDate.now().minusDays(30).toString(),
        end = LocalDate.now().plusDays(60).toString(),
        startValue = "0",
        endValue = "90",
        countdownType = CountdownType.DAYS,
        tag = null
    )
)

@OptIn(ExperimentalGlancePreviewApi::class)
@Preview(widthDp = 250, heightDp = 200)
@Composable
private fun CountdownsWidgetPreview() {
    GlanceTheme {
        CountdownsList(
            countdowns = previewCountdowns,
            theming = getCountdownWidgetColors(),
            action = actionRunCallback<OpenApp>()
        )
    }
}

@OptIn(ExperimentalGlancePreviewApi::class)
@Preview(widthDp = 250, heightDp = 60)
@Composable
private fun CountdownsWidgetRowPreview() {
    GlanceTheme {
        CountdownRow(
            countdownModel = previewCountdowns.first(),
            theming = getCountdownWidgetColors()
        )
    }
}

@OptIn(ExperimentalGlancePreviewApi::class)
@Preview(widthDp = 250, heightDp = 150)
@Composable
private fun CountdownsWidgetEmptyPreview() {
    GlanceTheme {
        NoCountdowns(
            theming = getCountdownWidgetColors()
        )
    }
}
