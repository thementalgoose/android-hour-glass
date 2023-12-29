package tmg.hourglass.widgets.presentation

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.LocalContext
import androidx.glance.LocalGlanceId
import androidx.glance.LocalSize
import androidx.glance.appwidget.CircularProgressIndicator
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.LinearProgressIndicator
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.unit.ColorProvider
import tmg.hourglass.domain.model.Countdown
import tmg.hourglass.domain.utils.ProgressUtils
import tmg.hourglass.widgets.di.WidgetsEntryPoints
import tmg.hourglass.widgets.utils.appWidgetId
import tmg.hourglass.widgets.utils.fromHex
import kotlin.math.floor

class CountdownWidget : GlanceAppWidget() {

    companion object {
        private val configCircle = DpSize(48.dp, 48.dp)
        private val configBar = DpSize(180.dp, 48.dp)
    }

    override val sizeMode = SizeMode.Responsive(
        setOf(
            configCircle,
            configBar,
        )
    )


    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val realmWidgetConnector = WidgetsEntryPoints.get(context = context).realmWidgetConnector()

        provideContent {
            val config = LocalSize.current
            val appWidgetId = LocalGlanceId.current.appWidgetId
            val countdownModel = realmWidgetConnector.getCountdownModelSync(appWidgetId)

            if (countdownModel == null) {
                NoCountdown(GlanceModifier)
                return@provideContent
            }

            when (config) {
                configCircle -> CountdownCircle(
                    countdownModel = countdownModel,
                    modifier = GlanceModifier
                )
                configBar -> CountdownBar(
                    countdownModel = countdownModel,
                    modifier = GlanceModifier
                )
                else -> {
                    Log.e("UpNextWidget", "Invalid size, throwing IAW")
                    throw IllegalArgumentException("Invalid size not matching the provided ones")
                }
            }
        }
    }
}

@Composable
internal fun CountdownCircle(
    countdownModel: Countdown,
    modifier: GlanceModifier = GlanceModifier
) {
    Column(
        modifier = modifier.surface(Color(0xFFF2F2F2)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            modifier = GlanceModifier.fillMaxWidth(),
            color = ColorProvider(Color.fromHex(countdownModel.colour))
        )
    }
}
@Composable
internal fun CountdownBar(
    countdownModel: Countdown,
    modifier: GlanceModifier = GlanceModifier
) {
    val (progress, label) = countdownModel.getProgressAndInfo()
    Column(
        modifier = modifier.surface(Color(0xFFF2F2F2)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = GlanceModifier.padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = GlanceModifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                LinearProgressIndicator(
                    modifier = GlanceModifier.fillMaxWidth(),
                    progress = progress,
                    color = ColorProvider(Color.fromHex(countdownModel.colour))
                )
            }
        }
    }
}

@Composable
internal fun NoCountdown(
    modifier: GlanceModifier = GlanceModifier
) {

}

@Composable
private fun GlanceModifier.surface(color: Color): GlanceModifier = this
    .fillMaxSize()
    .background(color)
    .padding(0.dp)

private fun Countdown.getProgressAndInfo(): Pair<Float, String> {
    val start = this.initial.toIntOrNull() ?: 0
    val end = this.finishing.toIntOrNull() ?: 100
    val progress = ProgressUtils.getProgress(this.startByType, this.endByType, interpolator = this.interpolator)

    val label = this.countdownType.converter(floor((start + (progress * (end - start)))).toInt().toString())

    return Pair(progress, label)
}