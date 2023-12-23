package tmg.hourglass.presentation.usecases

import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode
import tmg.hourglass.presentation.ThemePref
import javax.inject.Inject

class ChangeThemeUseCase @Inject constructor() {
    fun update(theme: ThemePref) {
        when (theme) {
            ThemePref.AUTO -> setDefaultNightMode(MODE_NIGHT_FOLLOW_SYSTEM)
            ThemePref.LIGHT -> setDefaultNightMode(MODE_NIGHT_NO)
            ThemePref.DARK -> setDefaultNightMode(MODE_NIGHT_YES)
        }
    }
}