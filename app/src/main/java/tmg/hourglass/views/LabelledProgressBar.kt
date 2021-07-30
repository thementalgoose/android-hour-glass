package tmg.hourglass.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun LabelledProgressBar(
    progress: Float,
    height: Dp = 50.dp,
    backgroundColor: Color = Color.White,
    color: Color = Color.Blue,
    evaluator: (progress: Float) -> String
) {
    Row(modifier = Modifier.fillMaxWidth()) {
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
