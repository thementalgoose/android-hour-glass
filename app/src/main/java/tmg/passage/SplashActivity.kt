package tmg.passage

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import tmg.passage.home.HomeActivity

class SplashActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }
}