package tmg.hourglass

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import tmg.hourglass.home.HomeActivity

typealias HomeActivity2 = tmg.hourglass.ui.home.HomeActivity

class SplashActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startActivity(Intent(this, HomeActivity2::class.java))
        finish()
    }
}