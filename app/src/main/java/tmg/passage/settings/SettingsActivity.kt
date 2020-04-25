package tmg.passage.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import tmg.passage.R
import tmg.passage.base.BaseActivity
import tmg.utilities.extensions.initToolbar

class SettingsActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
    }
}