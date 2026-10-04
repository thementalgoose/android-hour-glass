package tmg.hourglass.wearos.presentation

import android.app.Activity
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

@AndroidEntryPoint
class ComplicationConfigActivity : ComponentActivity() {

    private val viewModel: ComplicationConfigViewModel by viewModels()

    private var complicationId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        complicationId = intent.getIntExtra(EXTRA_CONFIG_COMPLICATION_ID, -1)
        if (complicationId == -1) {
            complicationId = intent.getIntExtra(EXTRA_CONFIG_COMPLICATION_ID_FALLBACK, -1)
        }

        setContent {
            val countdowns by viewModel.countdowns.collectAsStateWithLifecycle()

            ComplicationConfigScreen(
                countdowns = countdowns,
                onCountdownSelected = { selected ->
                    if (complicationId != -1) {
                        viewModel.saveComplicationBinding(complicationId, selected.id)
                        updateComplication(complicationId)
                    }
                    setResult(Activity.RESULT_OK, Intent().apply {
                        putExtra(EXTRA_CONFIG_COMPLICATION_ID, complicationId)
                    })
                    finish()
                },
                onDismiss = {
                    finish()
                }
            )
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
