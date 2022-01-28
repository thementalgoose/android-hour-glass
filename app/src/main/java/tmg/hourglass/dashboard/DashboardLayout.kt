package tmg.hourglass.dashboard

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import tmg.hourglass.R
import tmg.hourglass.dashboard.view.DashboardCountdownLayout
import tmg.hourglass.domain.model.Countdown
import tmg.hourglass.domain.model.preview
import tmg.hourglass.presentation.AppThemePreview
import tmg.hourglass.presentation.textviews.TextBody2
import tmg.hourglass.presentation.textviews.TextHeader1

@Composable
private fun Preview() {
    AppThemePreview {
        Scaffold(
            content = {
                TextHeader1(text = stringResource(id = R.string.app_name))
                LazyColumn(
                    content = {
                        items(5) {
                            DashboardCountdownLayout(countdown = Countdown.preview())
                        }
                    }
                )
            }
        )
    }
}