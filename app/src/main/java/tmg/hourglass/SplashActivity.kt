package tmg.hourglass

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import tmg.hourglass.dashboard.DashboardActivity

class SplashActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startActivity(Intent(this, DashboardActivity::class.java))
        finish()
    }
}