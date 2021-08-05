package tmg.hourglass.ui.modify

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import tmg.hourglass.R
import tmg.hourglass.theme.AppTheme
import tmg.hourglass.theme.PaddingMedium
import tmg.hourglass.theme.brand
import tmg.hourglass.ui.modify.views.ModifyButtonColor
import tmg.hourglass.ui.modify.views.ModifySection
import tmg.hourglass.ui.modify.views.ModifyText
import tmg.hourglass.views.ScreenHeader

@Composable
fun ModifyScreen(
    inputs: ModifyViewModelInputs,
    outputs: ModifyViewModelOutputs
) {
    Column {
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            item {
                ScreenHeader(
                    title = stringResource(R.string.modify_header_add),
                    onClick = { }
                )
            }
            item {
                ModifySection(
                    title = stringResource(id = R.string.modify_field_name), 
                    description = stringResource(id = R.string.modify_field_name_desc)
                )
                ModifyText(hint = "Title")
                ModifyText(hint = "Description")
                ModifyButtonColor(
                    colour = brand,
                    onClick = { }
                )
            }
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = PaddingMedium,
                    end = PaddingMedium,
                    top = PaddingMedium,
                    bottom = PaddingMedium
                ),
            onClick = { /*TODO*/ }
        ) {
            Text(stringResource(id = R.string.modify_header_save))
        }
    }
}

@Composable
@Preview
private fun Preview() {
    AppTheme {
        Surface {
            ModifyScreen(
                inputs = object : ModifyViewModelInputs { },
                outputs = object : ModifyViewModelOutputs { }
            )
        }
    }
}