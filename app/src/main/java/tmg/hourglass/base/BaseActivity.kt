package tmg.hourglass.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import tmg.hourglass.prefs.PreferencesManager
import javax.inject.Inject

abstract class BaseActivity: AppCompatActivity() {

    @Inject
    protected lateinit var prefs: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}