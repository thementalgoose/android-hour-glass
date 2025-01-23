@file:OptIn(ExperimentalMaterial3Api::class)

package tmg.hourglass.presentation.utils

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import tmg.hourglass.presentation.AppTheme
import tmg.hourglass.presentation.buttons.PrimaryButton
import tmg.hourglass.presentation.buttons.SecondaryButton
import tmg.hourglass.presentation.textviews.TextBody1
import tmg.hourglass.presentation.textviews.TextHeader2
import tmg.hourglass.strings.R.string

@Composable
fun DeleteDialog(
    confirmed: () -> Unit,
    dismissed: () -> Unit,
    @StringRes
    title: Int,
    @StringRes
    subtitle: Int,
    modifier: Modifier = Modifier
) {
    ModalBottomSheet(
        containerColor = AppTheme.colors.backgroundPrimary,
        modifier = modifier,
        onDismissRequest = dismissed,
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppTheme.dimensions.paddingMedium)
                    .padding(bottom = AppTheme.dimensions.paddingXLarge),
                verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.paddingSmall)
            ) {
                TextHeader2(text = stringResource(id = title))
                TextBody1(text = stringResource(id = subtitle))
                PrimaryButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = string.settings_reset_all_confirm),
                    onClick = confirmed
                )
                SecondaryButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = string.settings_reset_all_cancel),
                    onClick = dismissed
                )
            }
        }
    )
}