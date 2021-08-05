package tmg.hourglass.ui.modify.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import tmg.hourglass.theme.AppTheme
import tmg.hourglass.theme.Typography

@Composable
fun ModifySection(
    title: String,
    description: String
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = title,
            style = Typography.subtitle1
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = description,
            style = Typography.subtitle2
        )
    }
}

@Composable
@Preview
private fun Preview() {
    AppTheme {
        Surface {
            ModifySection(
                title = "Personalise",
                description = "Pick a name, a description, and a colour for what you want to track here"
            )
        }
    }
}
