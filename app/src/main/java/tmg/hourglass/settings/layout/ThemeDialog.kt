package tmg.hourglass.settings.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import tmg.hourglass.strings.R.string
import tmg.hourglass.extensions.title
import tmg.hourglass.prefs.ThemePref
import tmg.hourglass.presentation.AppTheme
import tmg.hourglass.presentation.textviews.TextBody1
import tmg.hourglass.presentation.textviews.TextHeader2


@Composable
fun ThemeDialog(
    theme: ThemePref,
    themePicked: (ThemePref) -> Unit,
    dismissed: () -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(
        properties = DialogProperties(),
        onDismissRequest = dismissed,
        content = {
            Column(
                modifier = modifier
                    .background(AppTheme.colors.backgroundSecondary)
                    .padding(
                        start = AppTheme.dimensions.paddingSmall,
                        end = AppTheme.dimensions.paddingSmall,
                        top = AppTheme.dimensions.paddingMedium,
                        bottom = AppTheme.dimensions.paddingMedium
                    )
                    .verticalScroll(rememberScrollState())
            ) {
                TextHeader2(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = AppTheme.dimensions.paddingMedium,
                            top = AppTheme.dimensions.paddingMedium,
                            bottom = AppTheme.dimensions.paddingMedium,
                            end = AppTheme.dimensions.paddingMedium
                        ),
                    text = stringResource(id = string.modify_field_type)
                )
                ThemePref.values().forEach {
                    TextButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            dismissed()
                            themePicked(it)
                        }
                    ) {
                        TextBody1(
                            bold = it == theme,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    start = AppTheme.dimensions.paddingSmall,
                                    end = AppTheme.dimensions.paddingSmall
                                ),
                            text = stringResource(it.title)
                        )
                    }
                }
            }
        }
    )
}