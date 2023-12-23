package tmg.hourglass.presentation

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import tmg.hourglass.aboutthisapp.AboutThisAppConfig
import javax.inject.Inject


@AndroidEntryPoint
class HomeActivity: AppCompatActivity(), SplashScreen.KeepOnScreenCondition {

    @Inject
    protected lateinit var aboutThisAppConfig: AboutThisAppConfig

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            splashScreen.setSplashScreenTheme(R.style.AppTheme)
        }
        val splashScreen = installSplashScreen()

        setContent {
            val windowSizeClass = calculateWindowSizeClass(activity = this)
            AppTheme {
                HomeScreen(
                    windowSizeClass = windowSizeClass,
                    goToAboutThisApp = ::goToAboutThisApp,
                    goToMarketPage = ::goToMarketPage
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