@file:OptIn(ExperimentalMaterial3Api::class)

package tmg.hourglass.presentation.settings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material3.TextButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import tmg.hourglass.R
import tmg.hourglass.strings.R.string
import tmg.hourglass.extensions.title
import tmg.hourglass.presentation.ThemePref
import tmg.hourglass.presentation.AppTheme
import tmg.hourglass.presentation.buttons.PrimaryButton
import tmg.hourglass.presentation.buttons.SecondaryButton
import tmg.hourglass.presentation.textviews.TextBody1
import tmg.hourglass.presentation.textviews.TextHeader2


@Composable
fun ThemeDialog(
    theme: ThemePref,
    themePicked: (ThemePref) -> Unit,
    dismissed: () -> Unit,
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
                    .padding(bottom = AppTheme.dimensions.paddingXLarge),
                verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.paddingSmall)
            ) {
                TextHeader2(
                    text = stringResource(id = string.modify_field_type),
                    modifier = Modifier.padding(horizontal = AppTheme.dimensions.paddingMedium)
                )
                ThemePref.entries.forEach {
                    Row(
                        modifier = Modifier
                            .padding(horizontal = AppTheme.dimensions.paddingMedium)
                            .fillMaxWidth()
                            .clickable {
                                themePicked(it)
                                dismissed()
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextBody1(
                            bold = it == theme,
                            modifier = Modifier
                                .weight(1f)
                                .padding(
                                    vertical = AppTheme.dimensions.paddingSmall
                                ),
                            text = stringResource(it.title)
                        )
                        if (it == theme) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_settings_check),
                                contentDescription = null,
                                tint = AppTheme.colors.textPrimary
                            )
                        }
                    }
                }
            }
        }
    )
}