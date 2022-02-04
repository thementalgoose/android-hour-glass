package tmg.hourglass.settings.privacy

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material.Scaffold
import org.koin.androidx.viewmodel.ext.android.viewModel
import tmg.hourglass.base.BaseActivity
import tmg.hourglass.presentation.AppTheme
import tmg.utilities.extensions.observeEvent

class PrivacyPolicyActivity: BaseActivity() {

    private val viewModel: PrivacyPolicyViewModel by viewModel()

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