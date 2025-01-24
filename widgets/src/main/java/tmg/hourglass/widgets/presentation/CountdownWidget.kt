package tmg.hourglass.widgets.presentation

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.LocalSize
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.LinearProgressIndicator
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.unit.ColorProvider
import tmg.hourglass.domain.model.Countdown
import tmg.hourglass.domain.utils.ProgressUtils
import tmg.hourglass.strings.R.string
import tmg.hourglass.widgets.di.WidgetsEntryPoints
import tmg.hourglass.widgets.utils.appWidgetId
import tmg.hourglass.widgets.utils.fromHex
import tmg.utilities.extensions.isInDayMode
import kotlin.math.floor

class CountdownWidget : GlanceAppWidget() {

    companion object {
        private val configCircle = DpSize(48.dp, 48.dp)
        private val configBar = DpSize(150.dp, 48.dp)
    }

    override val sizeMode = SizeMode.Responsive(
        setOf(
            configCircle,
            configBar,
        )
    )

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val widgetConnector = WidgetsEntryPoints.get(context = context).widgetConnector()

        provideContent {
            val config = LocalSize.current
            val theming = getCountdownWidgetColors(context, !context.isInDayMode(ifUndefinedDefaultTo = true))
            Log.i("Widget", "App Widget Id ${id.appWidgetId}")
            val countdownModel = widgetConnector.getCountdownModel(id.appWidgetId).collectAsState(null)

            Log.i("Widget", "Countdown model loaded to be ${countdownModel.value}")
            when (val model = countdownModel.value) {
                null -> {
                    NoCountdown(
                        modifier = GlanceModifier.clickable(actionRunCallback<RefreshWidget>()),
                        theming = theming,
                        context = context
                    )
                }
                else -> {
                    when (config) {
                        configCircle -> CountdownSmall(
                            countdownModel = model,
                            theming = theming,
                            modifier = GlanceModifier.clickable(actionRunCallback<RefreshWidget>()),
                        )

                        configBar -> CountdownBar(
                            countdownModel = model,
                            theming = theming,
                            modifier = GlanceModifier.clickable(actionRunCallback<RefreshWidget>()),
                        )

                        else -> {
                            Log.e("UpNextWidget", "Invalid size, throwing IAW")
                            throw IllegalArgumentException("Invalid size not matching the provided ones")
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun CountdownSmall(
    countdownModel: Countdown,
    theming: CountdownWidgetTheming,
    modifier: GlanceModifier = GlanceModifier
) {
    val (progress, label) = countdownModel.getProgressAndInfo()
    Column(
        modifier = modifier.surface(theming.backgroundColor),
        verticalAlignment = Alignment.CenterVertically,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = GlanceModifier.defaultWeight(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                style = theming.content.copy(
                    textAlign = TextAlign.Center
                )
            )
        }
        LinearProgressIndicator(
            modifier = GlanceModifier
                .fillMaxWidth()
                .height(32.dp),
            progress = progress,
            backgroundColor = ColorProvider(theming.barBackgroundColor),
            color = ColorProvider(Color.fromHex(countdownModel.colour))
        )
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
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = GlanceModifier
                .fillMaxWidth()
                .defaultWeight(),
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = countdownModel.name,
                modifier = GlanceModifier.defaultWeight(),
                style = theming.title.copy(
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start
                )
            )
            Text(
                text = label,
                modifier = GlanceModifier.defaultWeight(),
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
                modifier = GlanceModifier.fillMaxWidth()
                    .height(30.dp),
                progress = progress,
                backgroundColor = ColorProvider(theming.barBackgroundColor),
                color = ColorProvider(Color.fromHex(countdownModel.colour))
            )
        }
    }
}

@Composable
internal fun NoCountdown(
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
private fun GlanceModifier.surface(color: Color): GlanceModifier = this
    .fillMaxSize()
    .background(color)
    .padding(0.dp)

private fun Countdown.getProgressAndInfo(): Pair<Float, String> {
    val progress = ProgressUtils.getProgress(this)
    val label = this.getProgress(progress)
    return Pair(progress, label)
}