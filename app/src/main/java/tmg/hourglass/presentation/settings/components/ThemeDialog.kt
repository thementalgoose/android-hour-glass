@file:OptIn(ExperimentalMaterial3Api::class)

package tmg.hourglass.presentation.settings.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import tmg.hourglass.R
import tmg.hourglass.extensions.title
import tmg.hourglass.presentation.AppTheme
import tmg.hourglass.presentation.ThemePref
import tmg.hourglass.presentation.textviews.TextBody1
import tmg.hourglass.presentation.textviews.TextBody2
import tmg.hourglass.presentation.textviews.TextHeader2
import tmg.hourglass.strings.R.string


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
                Column(Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = AppTheme.dimensions.paddingMedium,
                        vertical = AppTheme.dimensions.paddingXSmall
                    )
                ) {
                    TextHeader2(
                        text = stringResource(id = string.settings_theme_theme_title)
                    )
                    TextBody2(
                        modifier = Modifier.padding(top = 2.dp),
                        text = stringResource(id = string.settings_theme_theme_description)
                    )
                }
                ThemePref.entries.forEach {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                themePicked(it)
                                dismissed()
                            }
                            .padding(
                                horizontal = AppTheme.dimensions.paddingMedium,
                                vertical = AppTheme.dimensions.paddingSmall
                            ),
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
                        Switch(
                            checked = it == theme,
                            onCheckedChange = null,
                        )
                    }
                }
            }
        }
    )
}