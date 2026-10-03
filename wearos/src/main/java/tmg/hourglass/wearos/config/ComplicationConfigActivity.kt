package tmg.hourglass.wearos.config

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.watchface.complications.datasource.ComplicationDataSourceUpdateRequester
import tmg.hourglass.domain.model.Countdown
import tmg.hourglass.wearos.R
import tmg.hourglass.wearos.data.WearosCountdownRepository
import tmg.hourglass.wearos.service.HourglassComplicationService

class ComplicationConfigActivity : ComponentActivity() {

    private var complicationId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        complicationId = intent.getIntExtra(EXTRA_CONFIG_COMPLICATION_ID, -1)
        if (complicationId == -1) {
            complicationId = intent.getIntExtra(EXTRA_CONFIG_COMPLICATION_ID_FALLBACK, -1)
        }

        val repository = WearosCountdownRepository(this)

        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val countdowns = remember { repository.getCountdowns() }
                    ComplicationConfigScreen(
                        countdowns = countdowns,
                        onCountdownSelected = { selected ->
                            if (complicationId != -1) {
                                repository.saveComplicationBinding(complicationId, selected.id)
                                updateComplication(complicationId)
                            }
                            setResult(Activity.RESULT_OK, Intent().apply {
                                putExtra(EXTRA_CONFIG_COMPLICATION_ID, complicationId)
                            })
                            finish()
                        }
                    )
                }
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

@Composable
fun ComplicationConfigScreen(
    countdowns: List<Countdown>,
    onCountdownSelected: (Countdown) -> Unit
) {
    if (countdowns.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(id = R.string.no_countdowns_synced),
                textAlign = TextAlign.Center,
                fontSize = 14.sp
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text(
                    text = stringResource(id = R.string.select_countdown),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }
            items(countdowns) { countdown ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onCountdownSelected(countdown) }
                        .padding(vertical = 8.dp)
                ) {
                    Text(
                        text = countdown.name,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                    if (countdown.description.isNotBlank()) {
                        Text(
                            text = countdown.description,
                            fontSize = 12.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}
