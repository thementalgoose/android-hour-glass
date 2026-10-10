package tmg.hourglass.widgets.presentation.single

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
import androidx.glance.appwidget.cornerRadius
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
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import androidx.glance.GlanceTheme
import androidx.glance.LocalContext
import androidx.glance.layout.fillMaxHeight
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
        val countdownConnector = WidgetsEntryPoints.get(context = context).countdownConnector()

        provideContent {
            val config = LocalSize.current
            val theming = getCountdownWidgetColors()

            Log.i("CountdownWidget", "provideContent App Widget Id ${id.appWidgetId}")
            val countdownModel = widgetConnector.get(id.appWidgetId)
                .flatMapLatest { model ->
                    if (model !is WidgetReference.Single) {
                        return@flatMapLatest flow<Countdown?> { emit(null) }
                    }
                    return@flatMapLatest countdownConnector.get(model.countdownId)
                }
                .collectAsState(null)

            val action = actionRunCallback<OpenApp>()

            Log.i("CountdownWidget", "Countdown model loaded to be ${countdownModel.value}")
            when (val model = countdownModel.value) {
                null -> {
                    Log.e("CountdownWidget", "Observing countdown to be null")
                    NoCountdown(
                        modifier = GlanceModifier.clickable(action),
                        theming = theming,
                        context = context
                    )
                }
                else -> {
                    when (config) {
                        configCircle -> {
                            Log.d("CountdownWidget", "Drawing circle widget")
                            CountdownSmall(
                                countdownModel = model,
                                theming = theming,
                                modifier = GlanceModifier.clickable(action),
                            )
                        }

                        configBar -> {
                            Log.d("CountdownWidget", "Drawing bar widget")
                            CountdownBar(
                                countdownModel = model,
                                theming = theming,
                                modifier = GlanceModifier.clickable(action),
                            )
                        }

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
        Box(
            modifier = GlanceModifier.fillMaxWidth()
                .defaultWeight()
                .padding(
                    start = 8.dp,
                    end = 8.dp,
                    bottom = 8.dp
                ),
        ) {
            LinearProgressIndicator(
                modifier = GlanceModifier
                    .fillMaxWidth()
                    .fillMaxHeight()
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
internal fun NoCountdown(
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

private val previewCountdown = Countdown.Static(
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
)

@OptIn(ExperimentalGlancePreviewApi::class)
@Preview(widthDp = 150, heightDp = 48)
@Preview(widthDp = 250, heightDp = 96)
@Composable
private fun CountdownWidgetPreview() {
    GlanceTheme {
        CountdownBar(
            countdownModel = previewCountdown,
            theming = getCountdownWidgetColors()
        )
    }
}

@OptIn(ExperimentalGlancePreviewApi::class)
@Preview(widthDp = 48, heightDp = 48)
@Composable
private fun CountdownWidgetCirclePreview() {
    GlanceTheme {
        CountdownSmall(
            countdownModel = previewCountdown,
            theming = getCountdownWidgetColors()
        )
    }
}

@OptIn(ExperimentalGlancePreviewApi::class)
@Preview(widthDp = 150, heightDp = 48)
@Composable
private fun CountdownWidgetEmptyPreview() {
    GlanceTheme {
        NoCountdown(
            theming = getCountdownWidgetColors()
        )
    }
}