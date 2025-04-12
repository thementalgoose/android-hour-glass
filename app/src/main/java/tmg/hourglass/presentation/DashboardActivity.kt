package tmg.hourglass.presentation

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.collectAsState
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.window.layout.WindowInfoTracker
import androidx.window.layout.WindowLayoutInfo
import dagger.hilt.android.AndroidEntryPoint
import tmg.hourglass.aboutthisapp.AboutThisAppConfig
import tmg.hourglass.presentation.navigation.NavigationController
import javax.inject.Inject


@AndroidEntryPoint
class DashboardActivity: AppCompatActivity(), SplashScreen.KeepOnScreenCondition {

    @Inject
    lateinit var aboutThisAppConfig: AboutThisAppConfig

    @Inject
    lateinit var navigationController: NavigationController

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        this.enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            splashScreen.setSplashScreenTheme(R.style.AppTheme)
        }
        val splashScreen = installSplashScreen()

        val windowInfoTracker = WindowInfoTracker
            .getOrCreate(this)
            .windowLayoutInfo(this@DashboardActivity)

        setContent {
            val windowSizeClass = calculateWindowSizeClass(activity = this)
            AppTheme {
                DashboardNavScreen(
                    windowSize = windowSizeClass,
                    windowLayoutInfo = windowInfoTracker.collectAsState(WindowLayoutInfo(emptyList())).value,
                    navigator = navigationController,
                    closeApp = { finish() },
                    viewModelStore = this.viewModelStore,
                    deeplink = null,
                    goToAboutThisApp = ::goToAboutThisApp,
                    goToMarketPage = ::goToMarketPage,
                )
            }
        }

        splashScreen.setKeepOnScreenCondition(this)
    }

    private fun goToAboutThisApp() {
        aboutThisAppConfig.startActivity(this)
    }

    private fun goToMarketPage() {
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")))
        } catch (anfe: ActivityNotFoundException) {
            startActivity(
                Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$packageName"))
            )
        }
    }

    override fun shouldKeepOnScreen() = false
}