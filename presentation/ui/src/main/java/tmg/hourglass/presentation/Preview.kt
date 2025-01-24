package tmg.hourglass.presentation

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview

@Preview(
    showBackground = true,
    device = "spec:width=480dp,height=720dp,dpi=480",
    name = "Phone"
)
@Preview(
    showBackground = true,
    device = "spec:width=1200dp,height=800dp,dpi=480",
    name = "Tablet"
)
annotation class PreviewDevices

@Preview(
    showBackground = true,
    device = "spec:width=1200dp,height=800dp,dpi=480",
    name = "Tablet"
)
annotation class PreviewTablet

@Preview(
    showBackground = true,
    device = "spec:width=480dp,height=720dp,dpi=480",
    name = "Phone"
)
annotation class PreviewPhone

@Preview(
    showBackground = true,
    device = "spec:width=673dp,height=841dp,dpi=480",
    name = "Foldable"
)
annotation class PreviewFoldable


@Preview(
    showBackground = true,
    name = "Light",
    backgroundColor = 4294506744,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    device = Devices.PIXEL_4
)
@Preview(
    showBackground = true,
    name = "Dark",
    backgroundColor = 4279769112,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = Devices.PIXEL_4
)
annotation class PreviewTheme