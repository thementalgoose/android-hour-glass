package tmg.hourglass.presentation.modify.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import tmg.hourglass.presentation.AppTheme
import tmg.hourglass.presentation.AppThemePreview
import tmg.hourglass.presentation.PreviewTheme
import tmg.hourglass.presentation.buttons.PrimaryButton
import tmg.hourglass.presentation.buttons.SecondaryIconButton
import tmg.hourglass.presentation.dialog.TextDialog
import tmg.hourglass.presentation.inputs.Input
import tmg.hourglass.presentation.modify.NotificationType
import tmg.hourglass.presentation.modify.UiNotification
import tmg.hourglass.presentation.textviews.TextBody1
import tmg.hourglass.presentation.textviews.TextHeader2
import tmg.hourglass.strings.R

@Composable
fun NotificationsLayout(
    notifications: List<UiNotification>,
    notificationsEnabled: Boolean,
    onUpdateValue: (id: String, value: String) -> Unit,
    onUpdateType: (id: String, type: NotificationType) -> Unit,
    onDelete: (id: String) -> Unit,
    onEnableNotifications: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(AppTheme.dimensions.paddingMedium),
        verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.paddingSmall)
    ) {
        TextHeader2(text = stringResource(id = R.string.modify_notifications_title))
        TextBody1(text = stringResource(id = R.string.modify_notifications_subtitle))

        notifications.forEach { notification ->
            NotificationRow(
                notification = notification,
                isEnabled = notificationsEnabled,
                onUpdateValue = onUpdateValue,
                onUpdateType = onUpdateType,
                onDelete = onDelete
            )
        }

        if (!notificationsEnabled) {
            Spacer(modifier = Modifier.height(4.dp))
            PrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.modify_notifications_enable),
                onClick = onEnableNotifications
            )
        }
    }
}

@Composable
private fun NotificationRow(
    notification: UiNotification,
    isEnabled: Boolean,
    onUpdateValue: (id: String, value: String) -> Unit,
    onUpdateType: (id: String, type: NotificationType) -> Unit,
    onDelete: (id: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val showTypeDialog = remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.paddingXSmall)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .clip(RoundedCornerShape(AppTheme.dimensions.radiusSmall))
                .background(
                    if (isEnabled) AppTheme.colors.backgroundSecondary
                    else AppTheme.colors.backgroundSecondary.copy(alpha = 0.5f)
                )
                .clickable(enabled = isEnabled) {
                    showTypeDialog.value = true
                }
                .padding(AppTheme.dimensions.paddingMedium)
        ) {
            TextBody1(
                modifier = Modifier.align(Alignment.CenterStart),
                text = when (notification.type) {
                    NotificationType.VALUE -> stringResource(id = R.string.modify_notifications_type_value)
                    NotificationType.TIME -> stringResource(id = R.string.modify_notifications_type_time)
                }
            )
        }

        Input(
            modifier = Modifier.weight(1f),
            initial = notification.value,
            inputUpdated = { onUpdateValue(notification.id, it) },
            keyboardType = KeyboardType.Number,
            hint = stringResource(id = R.string.modify_notifications_value_hint),
            enabled = isEnabled
        )

        SecondaryIconButton(
            modifier = Modifier.fillMaxHeight(),
            icon = Icons.Default.Delete,
            contentDescription = "Delete",
            onClick = { onDelete(notification.id) },
            isEnabled = isEnabled
        )
    }

    if (showTypeDialog.value) {
        TextDialog(
            items = listOf(NotificationType.VALUE),
            itemClicked = { onUpdateType(notification.id, it) },
            dismissed = { showTypeDialog.value = false },
            title = stringResource(id = R.string.modify_notifications_title),
            itemSelected = notification.type,
            itemLabel = { type ->
                when (type) {
                    NotificationType.VALUE -> stringResource(id = R.string.modify_notifications_type_value)
                    NotificationType.TIME -> stringResource(id = R.string.modify_notifications_type_time)
                }
            }
        )
    }
}

@PreviewTheme
@Composable
private fun PreviewNotificationsLayoutEnabled() {
    AppThemePreview {
        NotificationsLayout(
            notifications = listOf(
                UiNotification(id = "1", type = NotificationType.VALUE, value = "350"),
                UiNotification(id = "2", type = NotificationType.VALUE, value = "")
            ),
            notificationsEnabled = true,
            onUpdateValue = { _, _ -> },
            onUpdateType = { _, _ -> },
            onDelete = { },
            onEnableNotifications = { }
        )
    }
}

@PreviewTheme
@Composable
private fun PreviewNotificationsLayoutDisabled() {
    AppThemePreview {
        NotificationsLayout(
            notifications = listOf(
                UiNotification(id = "1", type = NotificationType.VALUE, value = "350")
            ),
            notificationsEnabled = false,
            onUpdateValue = { _, _ -> },
            onUpdateType = { _, _ -> },
            onDelete = { },
            onEnableNotifications = { }
        )
    }
}
