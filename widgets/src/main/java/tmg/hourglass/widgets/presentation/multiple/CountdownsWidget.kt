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
import tmg.hourglass.domain.model.Countdown
import tmg.hourglass.domain.model.WidgetReference
import tmg.hourglass.domain.utils.ProgressUtils
import tmg.hourglass.strings.R.string
import tmg.hourglass.widgets.di.WidgetsEntryPoints
import tmg.hourglass.widgets.presentation.CountdownWidgetTheming
import tmg.hourglass.widgets.presentation.OpenApp
import tmg.hourglass.widgets.presentation.RefreshWidget
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
    action: androidx.glance.action.Action,
    modifier: GlanceModifier = GlanceModifier
) {
    LazyColumn(
        modifier = modifier
            .surface(theming.backgroundColor)
            .padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        items(countdowns, itemId = { it.id.hashCode().toLong() }) { countdown ->
            CountdownRow(
                countdownModel = countdown,
                theming = theming,
                modifier = GlanceModifier
                    .fillMaxWidth()
                    .clickable(action)
                    .padding(vertical = 4.dp)
            )
        }
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
            modifier = GlanceModifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            LinearProgressIndicator(
                modifier = GlanceModifier
                    .fillMaxWidth()
                    .height(24.dp)
                    .cornerRadius(12.dp),
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
    context: Context
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
