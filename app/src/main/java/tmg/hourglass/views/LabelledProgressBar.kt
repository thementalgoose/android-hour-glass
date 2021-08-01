package tmg.hourglass.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import tmg.hourglass.theme.PaddingMedium
import tmg.hourglass.theme.RadiusSmall

@Composable
fun LabelledProgressBar(
    progress: Float,
    height: Dp = 36.dp,
    backgroundColor: Color = Color.White,
    color: Color = Color.Blue,
    evaluator: (progress: Float) -> String
) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(RadiusSmall))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .background(backgroundColor)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth(progress)
                .height(height)
                .background(color)
        )
        Text(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = PaddingMedium),
            text = evaluator(progress)
        )
    }
}

@Composable
@Preview
private fun Preview() {
    LabelledProgressBar(
        progress = 0.8f,
        evaluator = {
            "${(it * 100).toInt()} days"
        }
    )
}
