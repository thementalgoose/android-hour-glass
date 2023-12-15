package tmg.hourglass.presentation

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview

@Preview(
    showBackground = true,
    device = "spec:shape=Normal,width=480,height=720,unit=dp,dpi=480",
    name = "Phone"
)
@Preview(
    showBackground = true,
    device = "spec:shape=Normal,width=1200,height=800,unit=dp,dpi=480",
    name = "Tablet"
)
annotation class PreviewDevices

@Preview(
    showBackground = true,
    device = "spec:shape=Normal,width=1200,height=800,unit=dp,dpi=480",
    name = "Tablet"
)
annotation class PreviewTablet

@Preview(
    showBackground = true,
    device = "spec:shape=Normal,width=480,height=720,unit=dp,dpi=480",
    name = "Phone"
)
annotation class PreviewPhone

@Preview(
    showBackground = true,
    device = "spec:shape=Normal,width=673,height=841,unit=dp,dpi=480",
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