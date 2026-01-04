package tmg.hourglass.presentation.home.components

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import tmg.hourglass.domain.enums.CountdownType
import tmg.hourglass.domain.extensions.label
import tmg.hourglass.presentation.AppTheme
import tmg.hourglass.presentation.buttons.ErrorButton
import tmg.hourglass.presentation.buttons.SecondaryButton
import tmg.hourglass.presentation.home.SortOrder
import tmg.hourglass.presentation.textviews.TextBody1
import tmg.hourglass.presentation.textviews.TextHeader2
import tmg.hourglass.strings.R.string

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortDialog(
    sortUpdated: (SortOrder) -> Unit,
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
                    text = stringResource(id = string.menu_sort)
                )
                SortOrder.entries.forEach {
                    TextButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            sortUpdated(it)
                            dismissed()
                        }
                    ) {
                        TextBody1(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    start = AppTheme.dimensions.paddingSmall,
                                    end = AppTheme.dimensions.paddingSmall
                                ),
                            text = stringResource(it.label())
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun SortOrder.label(): Int {
    return when (this) {
        SortOrder.ALPHABETICAL -> string.menu_sort_alphabetical
        SortOrder.FINISHING_SOONEST -> string.menu_finishing_soonest
        SortOrder.FINISHING_LATEST -> string.menu_finishing_latest
    }
}