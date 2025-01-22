package tmg.hourglass.presentation.settings.release

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import tmg.hourglass.strings.R.string
import tmg.hourglass.ReleaseNotes
import tmg.hourglass.presentation.AppThemePreview
import tmg.hourglass.presentation.layouts.TitleBar
import tmg.hourglass.presentation.views.ProgressBar
import tmg.hourglass.settings.release.ReleaseItemLayout

@Composable
fun ReleaseLayout(
    content: List<ReleaseNotes>,
    backClicked: () -> Unit
) {
    LazyColumn {
        item {
            TitleBar(
                title = stringResource(id = string.settings_help_release_notes_title),
                showBack = true,
                actionUpClicked = backClicked
            )
        }
        content.let { list ->
            items(list) {
                val title = stringResource(id = it.title)
                ReleaseItemLayout(
                    title = when (title.isEmpty()) {
                        true -> it.versionName
                        false -> "${it.versionName} - $title"
                    },
                    content = stringResource(id = it.release)
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    AppThemePreview(isLight = true) {
        ReleaseLayout(
            content = listOf(
                ReleaseNotes.VERSION_27,
                ReleaseNotes.VERSION_22
            ),
            backClicked = { }
        )
    }
}