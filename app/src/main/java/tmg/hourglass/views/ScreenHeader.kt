package tmg.hourglass.views

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import tmg.hourglass.R
import tmg.hourglass.theme.*

@Composable
fun ScreenHeader(
    title: String,
    icon: ImageVector = Icons.Default.ArrowBack,
    iconContentDescription: String = stringResource(R.string.ab_back),
    onClick: () -> Unit
) {
    return Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = PaddingMedium,
                end = PaddingMedium,
                top = PaddingMedium,
                bottom = PaddingMedium
            )
    ) {
        IconButton(
            onClick = onClick
        ) {
            Icon(icon, contentDescription = iconContentDescription)
        }
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = title,
            style = Typography.hero
        )
    }
}

@Composable
@Preview
private fun Preview() {
    AppTheme {
        Surface {
            ScreenHeader(
                title = "Add a countdown",
                onClick = { }
            )
        }
    }
}
