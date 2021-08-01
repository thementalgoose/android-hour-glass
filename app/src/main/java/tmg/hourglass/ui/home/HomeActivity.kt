package tmg.hourglass.ui.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import org.koin.androidx.viewmodel.ext.android.viewModel
import tmg.hourglass.theme.AppTheme

class HomeActivity: ComponentActivity() {

    private val viewModel: HomeViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Surface(color = MaterialTheme.colors.background) {
                    HomeScreen(
                        inputs = viewModel,
                        outputs = viewModel
                    )
                }
            }
        }
    }
}