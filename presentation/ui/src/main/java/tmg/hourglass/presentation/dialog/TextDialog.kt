package tmg.hourglass.presentation.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RadioButton
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import tmg.hourglass.presentation.AppTheme
import tmg.hourglass.presentation.textviews.TextBody1
import tmg.hourglass.presentation.textviews.TextHeader1

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> TextDialog(
    items: List<T>,
    itemClicked: (T) -> Unit,
    dismissed: () -> Unit,
    title: String,
    modifier: Modifier = Modifier,
    itemSelected: T? = null,
    showSelection: Boolean = itemSelected != null,
    itemLabel: @Composable (T) -> String = @Composable { it.toString() },
) {
    Dialog(
        properties = DialogProperties(),
        onDismissRequest = dismissed,
        content = {
            LazyColumn(
                modifier = modifier
                    .clip(RoundedCornerShape(AppTheme.dimensions.radiusMedium))
                    .background(AppTheme.colors.backgroundPrimary)
                    .padding(
                        start = AppTheme.dimensions.paddingSmall,
                        end = AppTheme.dimensions.paddingSmall,
                        top = AppTheme.dimensions.paddingMedium,
                        bottom = AppTheme.dimensions.paddingMedium
                    ),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                item("header") {
                    TextHeader1(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = AppTheme.dimensions.paddingMedium,
                                top = AppTheme.dimensions.paddingMedium,
                                bottom = AppTheme.dimensions.paddingMedium,
                                end = AppTheme.dimensions.paddingMedium
                            ),
                        text = title
                    )
                }
                items(items, key = { it.toString() }) {
                    TextButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(AppTheme.dimensions.radiusSmall))
                            .background(AppTheme.colors.backgroundSecondary),
                        onClick = {
                            itemClicked(it)
                            dismissed()
                        }
                    ) {
                        TextBody1(
                            modifier = Modifier
                                .weight(1f)
                                .padding(
                                    start = AppTheme.dimensions.paddingSmall,
                                    end = AppTheme.dimensions.paddingSmall
                                ),
                            text = itemLabel(it)
                        )
                        if (showSelection) {
                            RadioButton(
                                selected = itemSelected == it,
                                onClick = null,
                            )
                        }
                    }
                }
            }
        }
    )
}
