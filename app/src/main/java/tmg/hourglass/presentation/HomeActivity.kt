package tmg.hourglass.presentation

import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity: AppCompatActivity(), SplashScreen.KeepOnScreenCondition {

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
                    windowSizeClass = windowSizeClass
                )
            }
        }

        splashScreen.setKeepOnScreenCondition(this)
    }

    override fun shouldKeepOnScreen() = false
}