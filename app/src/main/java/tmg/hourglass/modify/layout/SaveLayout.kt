package tmg.hourglass.modify.layout

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import tmg.hourglass.R
import tmg.hourglass.strings.R.string
import tmg.hourglass.presentation.AppTheme
import tmg.hourglass.presentation.AppThemePreview
import tmg.hourglass.presentation.buttons.PrimaryButton
import tmg.hourglass.presentation.buttons.PrimaryIconButton

@Composable
fun SaveLayout(
    isEdit: Boolean,
    saveEnabled: Boolean,
    saveClicked: () -> Unit,
    deleteClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        if (isEdit) {
            PrimaryIconButton(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(
                        top = AppTheme.dimensions.paddingMedium,
                        bottom = AppTheme.dimensions.paddingMedium,
                        start = AppTheme.dimensions.paddingMedium
                    ),
                icon = R.drawable.ic_delete,
                onClick = deleteClicked
            )
        }
        PrimaryButton(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(
                    top = AppTheme.dimensions.paddingMedium,
                    start = AppTheme.dimensions.paddingMedium,
                    end = AppTheme.dimensions.paddingMedium,
                    bottom = AppTheme.dimensions.paddingMedium,
                ),
            text = stringResource(id = string.modify_header_save),
            isEnabled = saveEnabled,
            onClick = saveClicked
        )
    }
}

@Preview
@Composable
private fun PreviewEdit() {
    AppThemePreview {
        SaveLayout(
            isEdit = true,
            saveEnabled = true,
            saveClicked = { },
            deleteClicked = { }
        )
    }
}

@Preview
@Composable
private fun PreviewAdd() {
    AppThemePreview {
        SaveLayout(
            isEdit = false,
            saveEnabled = true,
            saveClicked = { },
            deleteClicked = { }
        )
    }
}