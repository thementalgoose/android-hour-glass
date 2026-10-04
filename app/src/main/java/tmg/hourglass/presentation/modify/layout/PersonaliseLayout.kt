package tmg.hourglass.presentation.modify.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.graphics.toColorInt
import androidx.emoji2.emojipicker.EmojiPickerView
import tmg.hourglass.domain.enums.CountdownColors
import tmg.hourglass.presentation.AppTheme
import tmg.hourglass.presentation.AppThemePreview
import tmg.hourglass.presentation.PreviewTheme
import tmg.hourglass.presentation.inputs.Input
import tmg.hourglass.presentation.textviews.TextBody1
import tmg.hourglass.presentation.textviews.TextHeader2
import tmg.hourglass.strings.R.string

@Composable
fun PersonaliseLayout(
    emoji: String?,
    emojiPicked: (String?) -> Unit,
    name: String,
    nameUpdated: (String) -> Unit,
    nameError: Boolean,
    description: String,
    descriptionUpdated: (String) -> Unit,
    color: String,
    colorPicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    val colorPicker = remember { mutableStateOf(false) }
    val emojiPicker = remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(AppTheme.dimensions.paddingMedium)
    ) {
        TextHeader2(text = stringResource(id = string.modify_field_name))
        Spacer(modifier = Modifier.height(8.dp))
        TextBody1(text = stringResource(id = string.modify_field_name_desc))
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(AppTheme.dimensions.radiusSmall))
                    .background(AppTheme.colors.backgroundSecondary)
                    .clickable { emojiPicker.value = true },
                contentAlignment = Alignment.Center
            ) {
                if (emoji != null && emoji.isNotBlank()) {
                    Text(
                        text = emoji,
                        fontSize = 24.sp
                    )
                } else {
                    Text(
                        text = "😀",
                        fontSize = 24.sp,
                        modifier = Modifier.alpha(0.4f)
                    )
                }
            }
            Input(
                modifier = Modifier.weight(1f),
                initial = name,
                inputUpdated = nameUpdated,
                hint = stringResource(id = string.modify_field_name_hint),
                error = when (nameError) {
                    true -> stringResource(id = string.modify_error_title_missing)
                    false -> null
                }
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Input(
            modifier = Modifier,
            initial = description,
            inputUpdated = descriptionUpdated,
            hint = stringResource(id = string.modify_field_description_hint)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(AppTheme.dimensions.radiusSmall))
                .background(AppTheme.colors.backgroundSecondary)
                .clickable(
                    onClick = {
                        colorPicker.value = true
                    }
                )
                .padding(AppTheme.dimensions.paddingMedium)
        ) {
            TextBody1(
                text = stringResource(id = string.modify_field_colour_hint),
                modifier = Modifier
                    .padding(
                        end = AppTheme.dimensions.paddingMedium
                    )
                    .align(Alignment.CenterVertically)
                    .weight(1f)
            )
            Box(
                modifier = Modifier
                    .size(
                        width = 24.dp,
                        height = 24.dp
                    )
                    .clip(RoundedCornerShape(AppTheme.dimensions.radiusSmall))
                    .background(Color(color.toColorInt()))
            )
        }
    }

    if (emojiPicker.value) {
        EmojiPickerDialog(
            onEmojiPicked = { selectedEmoji ->
                emojiPicked(selectedEmoji)
                emojiPicker.value = false
            },
            onClearEmoji = {
                emojiPicked(null)
                emojiPicker.value = false
            },
            dismiss = {
                emojiPicker.value = false
            }
        )
    }

    if (colorPicker.value) {
        ColorPicker(
            colorPicked = colorPicked,
            dismiss = {
                colorPicker.value = false
            }
        )
    }
}

@Composable
private fun EmojiPickerDialog(
    onEmojiPicked: (String) -> Unit,
    onClearEmoji: () -> Unit,
    dismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = dismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .fillMaxHeight(0.7f)
                .clip(RoundedCornerShape(AppTheme.dimensions.radiusMedium))
                .background(AppTheme.colors.backgroundSecondary)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(AppTheme.dimensions.paddingMedium),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextHeader2(text = stringResource(id = string.modify_field_emoji_picker_title))
                TextButton(onClick = onClearEmoji) {
                    TextBody1(
                        text = stringResource(id = string.modify_field_emoji_clear),
                        textColor = AppTheme.colors.appColors.error
                    )
                }
            }
            AndroidView(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                factory = { context ->
                    EmojiPickerView(context).apply {
                        setOnEmojiPickedListener { emojiViewItem ->
                            onEmojiPicked(emojiViewItem.emoji)
                        }
                    }
                }
            )
        }
    }
}

@Composable
private fun ColorPicker(
    colorPicked: (String) -> Unit,
    dismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = dismiss,
        properties = DialogProperties(),
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AppTheme.colors.backgroundSecondary)
            ) {
                TextHeader2(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = AppTheme.dimensions.paddingMedium,
                            top = AppTheme.dimensions.paddingMedium,
                            end = AppTheme.dimensions.paddingMedium
                        ),
                    text = stringResource(id = string.modify_field_colour_hint)
                )
                LazyVerticalGrid(
                    columns = GridCells.Fixed(5),
                    contentPadding = PaddingValues(AppTheme.dimensions.paddingMedium),
                    content = {
                        items(CountdownColors.values().toList()) {
                            Box(
                                modifier = Modifier
                                    .height(64.dp)
                                    .padding(AppTheme.dimensions.paddingSmall)
                                    .background(Color(it.hex.toColorInt()))
                                    .clickable(
                                        onClick = {
                                            colorPicked(it.hex)
                                            dismiss()
                                        }
                                    )
                            )
                        }
                    }
                )
            }
        }
    )
}

@PreviewTheme
@Composable
private fun Preview() {
    AppThemePreview {
        PersonaliseLayout(
            emoji = "🚀",
            emojiPicked = { },
            name = "name",
            nameUpdated = { },
            nameError = false,
            description = "description",
            descriptionUpdated = { },
            color = "#263892",
            colorPicked = { },
        )
    }
}

@PreviewTheme
@Composable
private fun PreviewErrors() {
    AppThemePreview {
        PersonaliseLayout(
            emoji = null,
            emojiPicked = { },
            name = "name",
            nameUpdated = { },
            nameError = true,
            description = "description",
            descriptionUpdated = { },
            color = "#263892",
            colorPicked = { },
        )
    }
}