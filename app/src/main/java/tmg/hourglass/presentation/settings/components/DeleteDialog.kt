package tmg.hourglass.presentation.settings.components

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import tmg.hourglass.strings.R.string
import tmg.hourglass.presentation.textviews.TextBody1
import tmg.hourglass.presentation.textviews.TextHeader2

@Composable
fun DeleteDialog(
    confirmed: () -> Unit,
    dismissed: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        modifier = modifier,
        title = {
            TextHeader2(text = stringResource(id = string.settings_reset_all_title))
        },
        text = {
            TextBody1(text = stringResource(id = string.settings_reset_all_description))
        },
        onDismissRequest = dismissed,
        dismissButton = {
            Button(onClick = dismissed) {
                Text(text = stringResource(id = string.settings_reset_all_cancel))
            }
        },
        confirmButton = {
            Button(onClick = confirmed) {
                Text(text = stringResource(id = string.settings_reset_all_confirm))
            }
        }
    )
}