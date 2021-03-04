package tmg.hourglass.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.koin.android.ext.android.inject
import tmg.hourglass.R
import tmg.hourglass.prefs.PreferencesManager
import tmg.hourglass.prefs.ThemePref
import tmg.utilities.extensions.isInDayMode

abstract class BaseActivity: AppCompatActivity() {

    val prefs: PreferencesManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        when (prefs.theme) {
            ThemePref.LIGHT -> setTheme(R.style.LightTheme)
            ThemePref.AUTO -> {
                if (isInDayMode()) {
                    setTheme(R.style.LightTheme)
                }
                else {
                    setTheme(R.style.DarkTheme)
                }
            }
            ThemePref.DARK -> setTheme(R.style.DarkTheme)
        }
        super.onCreate(savedInstanceState)
    }
}