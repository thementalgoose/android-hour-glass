package tmg.hourglass.presentation.settings.privacy

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.Scaffold
import dagger.hilt.android.AndroidEntryPoint
import tmg.hourglass.presentation.AppTheme

@AndroidEntryPoint
class PrivacyPolicyActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Scaffold(content = {
                    PrivacyPolicyLayout(
                        backClicked = {
                            finish()
                        }
                    )
                })
            }
        }
    }
}