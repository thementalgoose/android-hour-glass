package tmg.hourglass.ui.home.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import tmg.hourglass.R
import tmg.hourglass.theme.*

@Composable
fun HomeHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = PaddingMedium,
                start = PaddingMedium,
                end = PaddingMedium,
                bottom = PaddingMedium
            )
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth(0.3f),
            painter = painterResource(id = R.drawable.ic_icon),
            contentDescription = null,
            colorFilter = ColorFilter.tint(brand)
        )
        Text(
            style = Typography.hero,
            text = stringResource(R.string.app_name)
        )
    }
}

@Composable
@Preview
private fun Preview() {
    AppTheme {
        Surface {
            HomeHeader()
        }
    }
}
