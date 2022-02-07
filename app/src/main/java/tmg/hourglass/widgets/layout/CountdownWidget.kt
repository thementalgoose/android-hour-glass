package tmg.hourglass.widgets.layout

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.LocalSize
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.background
import androidx.glance.extractModifier
import androidx.glance.layout.*
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.unit.ColorProvider
import org.koin.core.component.getScopeId
import java.lang.reflect.Modifier

class CountdownWidget(
    private val countdownId: String
): GlanceAppWidget() {

    companion object {
        private val SMALL_BOX = DpSize(90.dp, 90.dp)
        private val BIG_BOX = DpSize(180.dp, 180.dp)
        private val ROW = DpSize(180.dp, 48.dp)
        private val LARGE_ROW = DpSize(300.dp, 48.dp)
        private val COLUMN = DpSize(48.dp, 180.dp)
        private val LARGE_COLUMN = DpSize(48.dp, 300.dp)
    }


    override val sizeMode = SizeMode.Responsive(
        setOf(SMALL_BOX, BIG_BOX, ROW, LARGE_ROW, COLUMN, LARGE_COLUMN)
    )


    @Composable
    override fun Content() {
        // Content will be called for each of the provided sizes
        when (LocalSize.current) {
            COLUMN -> Column()
            ROW -> Row()
            LARGE_COLUMN -> Column()
            LARGE_ROW -> Row()
            SMALL_BOX -> Box()
            BIG_BOX -> Box()
            else -> throw IllegalArgumentException("Invalid size not matching the provided ones")
        }
    }
}

@Composable
private fun Box() {
    Row(modifier = GlanceModifier
        .fillMaxSize()
        .background(Color.Cyan)
    ) {
        Text("BOX")
    }
}

@Composable
private fun Row() {
    Column(modifier = GlanceModifier
        .fillMaxSize()
    ) {
        Row {
            Text(
                text = "countdown-id",
                style = androidx.glance.text.TextStyle(
                    fontSize = 20.sp,
                    color = ColorProvider(Color.White)
                )
            )
        }
        Row(
            modifier = GlanceModifier.fillMaxWidth()
        ) {
            Text(
                modifier = GlanceModifier.defaultWeight(),
                text = "0",
                style = androidx.glance.text.TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = ColorProvider(Color.White)
                )
            )
            Text(
                modifier = GlanceModifier.defaultWeight(),
                text = "10000",
                style = androidx.glance.text.TextStyle(
                    fontSize = 16.sp,
                    textAlign = TextAlign.End,
                    fontWeight = FontWeight.Bold,
                    color = ColorProvider(Color.White)
                )
            )
        }
        Row(modifier = GlanceModifier
            .fillMaxSize()
        ) {
            ProgressBar(
                progress = 0.6f,
                barColor = Color.Green,
                backgroundColor = Color.White
            )
        }
    }
}

@Composable
private fun Column() {
    Column(modifier = GlanceModifier
        .fillMaxSize()
    ) {
        ProgressBar(
            progress = 0.6f,
            barColor = Color.Blue
        )
    }
}



private const val defaultGranularity = 10

@Composable
private fun RowScope.ProgressBar(
    progress: Float,
    barColor: Color,
    modifier: GlanceModifier = GlanceModifier,
    backgroundColor: Color = Color.Transparent,
    granularity: Int = defaultGranularity
) {
    for (x in 0 until granularity) {
        val boxThreshold = x.toFloat() / granularity.toFloat()
        Box(
            modifier = modifier
                .fillMaxSize()
                .defaultWeight()
                .background(
                    when (progress) {
                        1f -> barColor
                        0f -> backgroundColor
                        else -> {
                            if (boxThreshold <= progress) {
                                barColor
                            } else {
                                backgroundColor
                            }
                        }
                    })
        ) { }
    }
}

@Composable
private fun ColumnScope.ProgressBar(
    progress: Float,
    barColor: Color,
    modifier: GlanceModifier = GlanceModifier,
    backgroundColor: Color = Color.Transparent,
    granularity: Int = defaultGranularity
) {
    (granularity..1).forEach {
        val boxThreshold = it / granularity
        Box(
            modifier = modifier
                .fillMaxSize()
                .defaultWeight()
                .background(
                    when (progress) {
                        1f -> barColor
                        0f -> backgroundColor
                        else -> {
                            if (boxThreshold <= progress) {
                                barColor
                            } else {
                                backgroundColor
                            }
                        }
                    })
                ) { }
    }
}