package tmg.hourglass.ui.modify.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tmg.hourglass.R
import tmg.hourglass.theme.AppTheme
import tmg.hourglass.theme.PaddingMedium
import tmg.hourglass.theme.RadiusMedium
import tmg.hourglass.theme.brand

@Composable
fun ModifyButtonColor(
    colour: Color = Color.Transparent,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .background(MaterialTheme.colors.secondary)
            .clip(RoundedCornerShape(RadiusMedium))
            .clickable { onClick() }
    ) {
        Text(
            text = stringResource(id = R.string.modify_field_colour_hint),
            modifier = Modifier.weight(1f)
        )
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(RadiusMedium))
                .width(24.dp)
                .height(24.dp)
                .background(colour)
                .padding(
                    end = PaddingMedium,
                    top = PaddingMedium,
                    bottom = PaddingMedium
                )
        )
    }
}

@Composable
@Preview
private fun Preview() {
    AppTheme {
        Surface {
            ModifyButtonColor(
                colour = brand,
                onClick = { }
            )
        }
    }
}
