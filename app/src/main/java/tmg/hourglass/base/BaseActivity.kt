package tmg.hourglass.base

import android.os.Bundle
import org.koin.android.ext.android.inject
import tmg.hourglass.R
import tmg.hourglass.prefs.IPrefs
import tmg.hourglass.prefs.ThemePref
import tmg.utilities.lifecycle.common.CommonActivity

abstract class BaseActivity: CommonActivity() {

    val prefs: IPrefs by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        when (prefs.theme) {
            ThemePref.LIGHT -> setTheme(R.style.LightTheme)
            ThemePref.AUTO -> setTheme(R.style.LightTheme)
            ThemePref.DARK -> setTheme(R.style.DarkTheme)
        }
        super.onCreate(savedInstanceState)
    }
}