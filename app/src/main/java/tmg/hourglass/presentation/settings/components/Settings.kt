package tmg.hourglass.presentation.settings.components

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ExitToApp
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import tmg.hourglass.presentation.AppTheme
import tmg.hourglass.presentation.AppThemePreview
import tmg.hourglass.presentation.PreviewTheme
import tmg.hourglass.presentation.textviews.TextBody1
import tmg.hourglass.presentation.textviews.TextBody2
import tmg.hourglass.presentation.textviews.TextHeader2

private val internalSettingSpacing = 6.dp

@Composable
internal fun SettingsHeader(
    @StringRes
    title: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        TextHeader2(
            text = stringResource(id = title),
            brand = true
        )
    }
}

@Composable
internal fun SettingsOption(
    @StringRes
    title: Int,
    @StringRes
    subtitle: Int,
    optionClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(AppTheme.dimensions.radiusSmall))
            .background(AppTheme.colors.backgroundSecondary)
            .clickable(
                onClick = optionClicked
            )
            .padding(
                start = AppTheme.dimensions.paddingMedium,
                end = AppTheme.dimensions.paddingMedium,
                top = AppTheme.dimensions.paddingNSmall,
                bottom = AppTheme.dimensions.paddingNSmall
            )
    ) {
        TextBody1(
            text = stringResource(id = title),
            bold = true
        )
        Spacer(modifier = Modifier.height(internalSettingSpacing))
        TextBody2(
            text = stringResource(id = subtitle)
        )
    }
}

@Composable
internal fun SettingsOption(
    title: String,
    subtitle: String,
    optionClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(AppTheme.dimensions.radiusSmall))
            .background(AppTheme.colors.backgroundSecondary)
            .clickable(
                onClick = optionClicked
            )
            .padding(
                start = AppTheme.dimensions.paddingMedium,
                end = AppTheme.dimensions.paddingMedium,
                top = AppTheme.dimensions.paddingNSmall,
                bottom = AppTheme.dimensions.paddingNSmall
            )
    ) {
        TextBody1(
            text = title,
            bold = true
        )
        Spacer(modifier = Modifier.height(internalSettingSpacing))
        TextBody2(
            text = subtitle
        )
    }
}

@Composable
internal fun SettingsCategory(
    @StringRes
    title: Int,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .padding(horizontal = AppTheme.dimensions.paddingMedium),
        verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.paddingNSmall)
    ) {
        SettingsHeader(
            title = title
        )
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(AppTheme.dimensions.radiusMedium)),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            content()
        }
    }
}

@Composable
internal fun SettingsOption(
    @StringRes
    title: Int,
    @StringRes
    subtitle: Int,
    optionClicked: () -> Unit,
    label: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(AppTheme.dimensions.radiusSmall))
            .background(AppTheme.colors.backgroundSecondary)
            .clickable(
                onClick = optionClicked
            )
            .padding(
                start = AppTheme.dimensions.paddingMedium,
                end = AppTheme.dimensions.paddingMedium,
                top = AppTheme.dimensions.paddingNSmall,
                bottom = AppTheme.dimensions.paddingNSmall
            )
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            TextBody1(
                text = stringResource(id = title),
                bold = true
            )
            Spacer(modifier = Modifier.height(internalSettingSpacing))
            TextBody2(
                text = stringResource(id = subtitle)
            )
        }
        label()
    }
}

@Composable
internal fun SettingsSwitch(
    @StringRes
    title: Int,
    @StringRes
    subtitle: Int,
    isChecked: Boolean,
    optionClicked: (newValue: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(AppTheme.dimensions.radiusSmall))
            .background(AppTheme.colors.backgroundSecondary)
            .toggleable(
                value = isChecked,
                role = Role.Switch,
                onValueChange = optionClicked
            )
            .padding(
                start = AppTheme.dimensions.paddingMedium,
                end = AppTheme.dimensions.paddingMedium,
                top = AppTheme.dimensions.paddingNSmall,
                bottom = AppTheme.dimensions.paddingNSmall
            )
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(
                    end = AppTheme.dimensions.paddingSmall
                )
        ) {
            TextBody1(
                text = stringResource(id = title),
                bold = true
            )
            Spacer(modifier = Modifier.height(internalSettingSpacing))
            TextBody2(
                text = stringResource(id = subtitle)
            )
        }
        Switch(
            checked = isChecked,
            onCheckedChange = null,
            colors = SwitchDefaults.colors(
                checkedTrackColor = AppTheme.colors.accent
            )
        )
    }
}

@PreviewTheme
@Composable
private fun PreviewCategory() {
    AppThemePreview {
        SettingsCategory(
            title = tmg.hourglass.strings.R.string.settings_title
        ) {
            SettingsOption(
                title = tmg.hourglass.strings.R.string.settings_theme_theme_title,
                subtitle = tmg.hourglass.strings.R.string.settings_theme_theme_description,
                optionClicked = { }
            )
            SettingsSwitch(
                title = tmg.hourglass.strings.R.string.settings_theme_theme_title,
                subtitle = tmg.hourglass.strings.R.string.settings_theme_theme_description,
                isChecked = false,
                optionClicked = { }
            )
        }
    }
}

@PreviewTheme
@Composable
private fun PreviewHeader() {
    AppThemePreview {
        SettingsHeader(title = tmg.hourglass.strings.R.string.settings_title)
    }
}

@PreviewTheme
@Composable
private fun PreviewSwitch() {
    AppThemePreview {
        SettingsSwitch(
            title = tmg.hourglass.strings.R.string.settings_theme_theme_title,
            subtitle = tmg.hourglass.strings.R.string.settings_theme_theme_description,
            isChecked = false,
            optionClicked = { }
        )
    }
}

@PreviewTheme
@Composable
private fun PreviewSwitchChecked() {
    AppThemePreview {
        SettingsSwitch(
            title = tmg.hourglass.strings.R.string.settings_theme_theme_title,
            subtitle = tmg.hourglass.strings.R.string.settings_theme_theme_description,
            isChecked = true,
            optionClicked = { }
        )
    }
}

@PreviewTheme
@Composable
private fun PreviewOption() {
    AppThemePreview {
        SettingsOption(
            title = tmg.hourglass.strings.R.string.settings_theme_theme_title,
            subtitle = tmg.hourglass.strings.R.string.settings_theme_theme_description,
            optionClicked = { }
        )
    }
}

@PreviewTheme
@Composable
private fun PreviewBadge() {
    AppThemePreview {
        SettingsOption(
            title = tmg.hourglass.strings.R.string.settings_theme_theme_title,
            subtitle = tmg.hourglass.strings.R.string.settings_theme_theme_description,
            optionClicked = { },
            label = {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ExitToApp,
                    contentDescription = null,
                    tint = AppTheme.colors.textPrimary
                )
            }
        )
    }
}