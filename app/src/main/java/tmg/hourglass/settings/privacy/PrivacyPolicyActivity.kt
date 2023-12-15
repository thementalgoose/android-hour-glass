package tmg.hourglass.settings.privacy

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Scaffold
import dagger.hilt.android.AndroidEntryPoint
import tmg.hourglass.presentation.AppTheme
import tmg.utilities.extensions.observeEvent

@AndroidEntryPoint
class PrivacyPolicyActivity: AppCompatActivity() {

    private val viewModel: PrivacyPolicyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Scaffold(content = {
                    PrivacyPolicyLayout(
                        backClicked = viewModel.inputs::clickBack
                    )
                })
            }
        }

        observeEvent(viewModel.outputs.goBack) {
            finish()
        }
    }
}