package tmg.hourglass.room.sync

import android.content.Context
import android.util.Log
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import tmg.hourglass.domain.model.Countdown
import tmg.hourglass.domain.model.WearSyncContract
import tmg.hourglass.domain.model.toWearDto
import tmg.hourglass.domain.repositories.CountdownRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WearDataSyncManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val countdownRepository: CountdownRepository
) {
    companion object {
        private const val TAG = "WearDataSyncManager"
        const val COUNTDOWNS_PATH = WearSyncContract.COUNTDOWNS_PATH
        const val KEY_COUNTDOWNS_JSON = WearSyncContract.KEY_COUNTDOWNS_JSON
        const val KEY_TIMESTAMP = WearSyncContract.KEY_TIMESTAMP
    }

    fun startSyncing(coroutineScope: CoroutineScope) {
        countdownRepository.allCurrent()
            .onEach { countdowns ->
                syncCountdowns(countdowns)
            }
            .catch { e ->
                Log.e(TAG, "Error monitoring countdowns for Wear sync", e)
            }
            .launchIn(coroutineScope)
    }

    suspend fun syncCountdowns(countdowns: List<Countdown>) {
        try {
            val dtos = countdowns.map { it.toWearDto() }
            val jsonString = Json.encodeToString(dtos)

            val putDataMapRequest = PutDataMapRequest.create(WearSyncContract.COUNTDOWNS_PATH).apply {
                dataMap.putString(WearSyncContract.KEY_COUNTDOWNS_JSON, jsonString)
                dataMap.putLong(WearSyncContract.KEY_TIMESTAMP, System.currentTimeMillis())
                dataMap.putInt(WearSyncContract.KEY_SCHEMA_VERSION, WearSyncContract.CURRENT_SCHEMA_VERSION)
            }
            val request = putDataMapRequest.asPutDataRequest().setUrgent()
            Wearable.getDataClient(context).putDataItem(request).await()
            Log.d(TAG, "Successfully synced ${countdowns.size} countdowns to Wear OS with schema version ${WearSyncContract.CURRENT_SCHEMA_VERSION}")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to sync countdowns to Wear OS", e)
        }
    }
}

