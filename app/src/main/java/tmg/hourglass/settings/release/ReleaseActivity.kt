package tmg.hourglass.settings.release

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material.Scaffold
import androidx.compose.runtime.livedata.observeAsState
import org.koin.android.ext.android.inject
import tmg.hourglass.base.BaseActivity
import tmg.hourglass.presentation.AppTheme
import tmg.utilities.extensions.observeEvent

class ReleaseActivity: BaseActivity() {

    private val viewModel: ReleaseViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Scaffold(content = {
                    val content = viewModel.outputs.content.observeAsState()
                    ReleaseLayout(
                        content = content.value ?: emptyList(),
                        backClicked = viewModel.inputs::clickBack
                    )
                })
            }
        }

        observeEvent(viewModel.outputs.goBack) {
            finish()
        }
    }

    companion object {
        fun intent(context: Context): Intent {
            return Intent(context, ReleaseActivity::class.java)
        }
    }
}