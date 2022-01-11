package tmg.hourglass.settings.release

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import tmg.hourglass.R
import tmg.hourglass.ReleaseNotes
import tmg.hourglass.presentation.AppThemePreview
import tmg.hourglass.presentation.layouts.TitleBar

@Composable
fun ReleaseLayout(
    content: List<ReleaseNotes>,
    backClicked: () -> Unit
) {
    LazyColumn {
        item {
            TitleBar(
                title = stringResource(id = R.string.settings_help_release_notes_title),
                backClicked = backClicked
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