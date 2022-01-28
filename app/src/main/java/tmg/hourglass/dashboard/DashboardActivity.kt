package tmg.hourglass.dashboard

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import tmg.hourglass.R
import tmg.hourglass.base.BaseActivity
import tmg.hourglass.dashboard.view.DashboardCountdownLayout
import tmg.hourglass.dashboard.view.DashboardFavouriteLayout
import tmg.hourglass.dashboard.view.DashboardHeaderLayout
import tmg.hourglass.dashboard.view.DashboardSectionLayout
import tmg.hourglass.domain.model.Countdown
import tmg.hourglass.domain.model.preview
import tmg.hourglass.presentation.AppTheme
import tmg.hourglass.presentation.textviews.TextHeader1
import tmg.hourglass.presentation.textviews.TextHeader2

class DashboardActivity: BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Scaffold(
                content = {
                    LazyColumn(
                        content = {
                            item {
                                DashboardHeaderLayout(
                                    clickSettings = { }
                                )
                            }
                            item {
                                DashboardFavouriteLayout(countdown = Countdown.preview(color = "#982384"))
                            }
                            item {
                                DashboardSectionLayout(text = stringResource(id = R.string.dashboard_title_upcoming))
                            }
                            items(5) {
                                DashboardCountdownLayout(countdown = Countdown.preview())
                            }
                            item {
                                DashboardSectionLayout(text = stringResource(id = R.string.dashboard_title_previous))
                            }
                            items(5) {
                                DashboardCountdownLayout(countdown = Countdown.preview(color = "#535293"))
                            }
                        }
                    )
                }
            )
        }
    }
}