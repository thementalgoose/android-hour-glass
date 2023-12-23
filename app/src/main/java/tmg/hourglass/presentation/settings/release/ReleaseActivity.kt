package tmg.hourglass.presentation.settings.release

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Scaffold
import androidx.compose.runtime.livedata.observeAsState
import dagger.hilt.android.AndroidEntryPoint
import tmg.hourglass.presentation.AppTheme
import tmg.utilities.extensions.observeEvent
import javax.inject.Inject

@AndroidEntryPoint
class ReleaseActivity: AppCompatActivity() {

    private val viewModel: ReleaseViewModel by viewModels()

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