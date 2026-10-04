package tmg.hourglass.wearos.service

import android.content.ComponentName
import android.util.Log
import androidx.wear.watchface.complications.datasource.ComplicationDataSourceUpdateRequester
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.WearableListenerService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.json.Json
import tmg.hourglass.domain.model.WearCountdownDto
import tmg.hourglass.domain.model.WearSyncContract
import tmg.hourglass.domain.model.toCountdown
import tmg.hourglass.wearos.data.WearosCountdownRepository
import javax.inject.Inject

@AndroidEntryPoint
class CountdownDataListenerService : WearableListenerService() {

    @Inject
    lateinit var repository: WearosCountdownRepository

    override fun onDataChanged(dataEvents: DataEventBuffer) {
        dataEvents
            .filter { it.type == DataEvent.TYPE_CHANGED && it.dataItem.uri.path == WearSyncContract.COUNTDOWNS_PATH }
            .forEach { event ->
                val dataMap = DataMapItem.fromDataItem(event.dataItem).dataMap
                val version = dataMap.getInt(WearSyncContract.KEY_SCHEMA_VERSION, 1)

                if (!WearSyncContract.isSchemaVersionSupported(version)) {
                    Log.w(TAG, "Received payload with unsupported schema version: $version")
                    repository.setSchemaSupported(false)
                    requestComplicationsUpdate()
                    return@forEach
                }

                repository.setSchemaSupported(true)
                val jsonString = dataMap.getString(WearSyncContract.KEY_COUNTDOWNS_JSON) ?: return@forEach

                runCatching {
                    val dtos = Json.decodeFromString<List<WearCountdownDto>>(jsonString)
                    val countdowns = dtos.map { it.toCountdown() }
                    repository.saveCountdowns(countdowns)
                    Log.d(TAG, "Received ${countdowns.size} countdowns from phone with schema version $version")
                    requestComplicationsUpdate()
                }.onFailure { e ->
                    Log.e(TAG, "Failed to parse countdowns", e)
                }
            }
    }

    private fun requestComplicationsUpdate() {
        val requester = ComplicationDataSourceUpdateRequester.create(
            this,
            ComponentName(this, HourglassComplicationService::class.java)
        )
        requester.requestUpdateAll()
    }

    companion object {
        private const val TAG = "CountdownListener"
    }
}
