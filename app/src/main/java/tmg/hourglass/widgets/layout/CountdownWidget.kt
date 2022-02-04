package tmg.hourglass.widgets.layout

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceModifier
import androidx.glance.LocalSize
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.background
import androidx.glance.extractModifier
import androidx.glance.layout.*
import androidx.glance.text.Text

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
    Row(modifier = GlanceModifier
        .fillMaxSize()
        .padding(8.dp)
    ) {
        val modifier = GlanceModifier.fillMaxSize().padding(4.dp).defaultWeight()
        (1..6).forEach {
            Box(
                modifier = modifier
                    .background(if (it % 2 == 0) Color.White else Color.Red)) {

            }
        }
    }
}

@Composable
private fun Column() {
    Row(modifier = GlanceModifier
        .fillMaxSize()
        .background(Color.Red)
    ) {
        Text("COLUMN")
    }
}