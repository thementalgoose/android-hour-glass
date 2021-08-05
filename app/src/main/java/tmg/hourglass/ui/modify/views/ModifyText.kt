package tmg.hourglass.ui.modify.views

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import tmg.hourglass.theme.AppTheme
import tmg.hourglass.theme.RadiusMedium

@Composable
fun ModifyText(
    hint: String,
    editable: Boolean = true,
    textUpdated: (String) -> Unit = { }
) {
    var content: String by remember { mutableStateOf("") }

    TextField(
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        placeholder = {
            Text(hint)
        },
        readOnly = !editable,
        modifier = Modifier.fillMaxWidth(),
        value = content,
        shape = RoundedCornerShape(RadiusMedium),
        onValueChange = {
            content = it
            textUpdated(it)
        }
    )
}

@Composable
@Preview
private fun Preview() {
    AppTheme {
        Surface {
            ModifyText(
                hint = "Information",
                textUpdated = { }
            )
        }
    }
}
