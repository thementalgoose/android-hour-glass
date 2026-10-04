package tmg.hourglass.wearos.style

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

@Preview(
    name = "Small Round - Light",
    device = "id:wearos_small_round",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true
)
@Preview(
    name = "Small Round - Dark",
    device = "id:wearos_small_round",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Preview(
    name = "Large Round - Light",
    device = "id:wearos_large_round",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true
)
@Preview(
    name = "Large Round - Dark",
    device = "id:wearos_large_round",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Preview(
    name = "Square - Light",
    device = "id:wearos_square",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true
)
@Preview(
    name = "Square - Dark",
    device = "id:wearos_square",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
annotation class PreviewWearOS
