package tmg.hourglass.presentation.modify.layout

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import tmg.hourglass.R
import tmg.hourglass.strings.R.string
import tmg.hourglass.presentation.AppTheme
import tmg.hourglass.presentation.AppThemePreview
import tmg.hourglass.presentation.PreviewTheme
import tmg.hourglass.presentation.buttons.ErrorButton
import tmg.hourglass.presentation.buttons.PrimaryButton
import tmg.hourglass.presentation.buttons.PrimaryIconButton
import tmg.hourglass.presentation.buttons.SecondaryButton
import tmg.hourglass.presentation.buttons.SecondaryIconButton

@Composable
fun SaveLayout(
    isEdit: Boolean,
    saveEnabled: Boolean,
    saveClicked: () -> Unit,
    deleteClicked: () -> Unit,
    cancelClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = AppTheme.dimensions.paddingMedium,
                vertical = AppTheme.dimensions.paddingSmall
            ),
        verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.paddingSmall)
    ) {
        PrimaryButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = string.modify_header_save),
            isEnabled = saveEnabled,
            onClick = saveClicked
        )
        SecondaryButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = string.modify_header_cancel),
            onClick = cancelClicked,
        )
        if (isEdit) {
            ErrorButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = deleteClicked,
                text = stringResource(string.modify_header_delete)
            )
        }
    }
}

@PreviewTheme
@Composable
private fun PreviewEdit() {
    AppThemePreview {
        SaveLayout(
            isEdit = true,
            saveEnabled = true,
            saveClicked = { },
            deleteClicked = { },
            cancelClicked = { }
        )
    }
}

@PreviewTheme
@Composable
private fun PreviewEditSaveDisabled() {
    AppThemePreview {
        SaveLayout(
            isEdit = true,
            saveEnabled = false,
            saveClicked = { },
            deleteClicked = { },
            cancelClicked = { }
        )
    }
}

@PreviewTheme
@Composable
private fun PreviewAdd() {
    AppThemePreview {
        SaveLayout(
            isEdit = false,
            saveEnabled = true,
            saveClicked = { },
            deleteClicked = { },
            cancelClicked = { }
        )
    }
}

@PreviewTheme
@Composable
private fun PreviewAddSaveDisabled() {
    AppThemePreview {
        SaveLayout(
            isEdit = false,
            saveEnabled = false,
            saveClicked = { },
            deleteClicked = { },
            cancelClicked = { }
        )
    }
}