package tmg.hourglass.ui.modify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import org.koin.androidx.viewmodel.ext.android.viewModel
import tmg.hourglass.theme.AppTheme

class ModifyActivity: ComponentActivity() {

    private val viewModel: ModifyViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Surface(color = MaterialTheme.colors.background) {
                    ModifyScreen(
                        inputs = viewModel,
                        outputs = viewModel
                    )
                }
            }
        }
    }
}