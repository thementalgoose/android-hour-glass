package tmg.hourglass.wearos.service

import android.content.ComponentName
import android.util.Log
import androidx.wear.watchface.complications.datasource.ComplicationDataSourceUpdateRequester
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.WearableListenerService
import kotlinx.serialization.json.Json
import tmg.hourglass.domain.model.WearCountdownDto
import tmg.hourglass.domain.model.toCountdown
import tmg.hourglass.wearos.data.WearosCountdownRepository

class CountdownDataListenerService : WearableListenerService() {

    override fun onDataChanged(dataEvents: DataEventBuffer) {
        val repository = WearosCountdownRepository(this)
        for (event in dataEvents) {
            if (event.type == DataEvent.TYPE_CHANGED && event.dataItem.uri.path == COUNTDOWNS_PATH) {
                val dataMap = DataMapItem.fromDataItem(event.dataItem).dataMap
                val jsonString = dataMap.getString(KEY_COUNTDOWNS_JSON)
                if (jsonString != null) {
                    try {
                        val dtos = Json.decodeFromString<List<WearCountdownDto>>(jsonString)
                        val countdowns = dtos.map { it.toCountdown() }
                        repository.saveCountdowns(countdowns)
                        Log.d(TAG, "Received ${countdowns.size} countdowns from phone")

                        val requester = ComplicationDataSourceUpdateRequester.create(
                            this,
                            ComponentName(this, HourglassComplicationService::class.java)
                        )
                        requester.requestUpdateAll()
                    } catch (e: Exception) {
                        Log.e(TAG, "Failed to parse countdowns", e)
                    }
                }
            }
        }
    }

    companion object {
        private const val TAG = "CountdownListener"
        private const val COUNTDOWNS_PATH = "/countdowns"
        private const val KEY_COUNTDOWNS_JSON = "countdowns_json"
    }
}
