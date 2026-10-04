package tmg.hourglass.wearos.presentation

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.wear.watchface.complications.datasource.ComplicationDataSourceUpdateRequester
import dagger.hilt.android.AndroidEntryPoint
import tmg.hourglass.wearos.service.HourglassComplicationService
import tmg.hourglass.wearos.style.WearTheme

@AndroidEntryPoint
class ComplicationConfigActivity : ComponentActivity() {

    private val viewModel: ComplicationConfigViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var complicationId = intent.getIntExtra(EXTRA_CONFIG_COMPLICATION_ID, -1)
        if (complicationId == -1) {
            complicationId = intent.getIntExtra(EXTRA_CONFIG_COMPLICATION_ID_FALLBACK, -1)
        }

        setContent {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            WearTheme {
                ComplicationConfigScreen(
                    uiState = uiState,
                    onCountdownSelected = { selected ->
                        if (complicationId != -1) {
                            viewModel.saveComplicationBinding(complicationId, selected.id)
                            updateComplication(complicationId)
                        }
                        setResult(RESULT_OK, Intent().apply {
                            putExtra(EXTRA_CONFIG_COMPLICATION_ID, complicationId)
                        })
                        finish()
                    },
                    onDismiss = {
                        finish()
                    },
                    onRefresh = {
                        viewModel.refresh()
                    },
                    onOpenPlayStoreWatch = {
                        viewModel.openPlayStoreOnWatch(this)
                    },
                    onOpenPlayStorePhone = {
                        viewModel.openPlayStoreOnPhone(this)
                    }
                )
            }
        }
    }

    private fun updateComplication(id: Int) {
        val requester = ComplicationDataSourceUpdateRequester.create(
            this,
            ComponentName(this, HourglassComplicationService::class.java)
        )
        requester.requestUpdate(id)
    }

    companion object {
        const val EXTRA_CONFIG_COMPLICATION_ID = "android.support.wearable.complications.EXTRA_CONFIG_COMPLICATION_ID"
        private const val EXTRA_CONFIG_COMPLICATION_ID_FALLBACK = "com.google.android.wearable.complications.EXTRA_CONFIG_COMPLICATION_ID"
    }
}
