package tmg.hourglass.settings

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import tmg.hourglass.R
import tmg.hourglass.presentation.layouts.TitleBar
import tmg.hourglass.settings.layout.SettingHeader
import tmg.hourglass.settings.layout.SettingPref
import tmg.hourglass.settings.layout.SettingSwitchPref

@Composable
fun SettingsLayout(
    list: List<SettingsModel>,
    clickBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        content = {
            item {
                TitleBar(
                    title = stringResource(id = R.string.settings_title),
                    backClicked = clickBack
                )
            }
            items(list) { item ->
                when (item) {
                    is SettingsModel.Header -> {
                        SettingHeader(text = stringResource(item.title))
                    }
                    is SettingsModel.Pref -> {
                        SettingPref(
                            title = stringResource(id = item.title),
                            subtitle = stringResource(id = item.description),
                            onClick = {
                                item.onClick?.invoke()
                            }
                        )
                    }
                    is SettingsModel.SwitchPref -> {
                        SettingSwitchPref(
                            title = stringResource(id = item.title),
                            subtitle = stringResource(id = item.description),
                            checkboxState = item.getState(),
                            onClick = {
                                item.saveState(!item.getState())
                            }
                        )
                    }
                }
            }
        }
    )
}