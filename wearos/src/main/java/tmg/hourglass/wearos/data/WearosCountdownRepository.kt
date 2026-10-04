package tmg.hourglass.wearos.data

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.Wearable
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import tmg.hourglass.domain.model.Countdown
import tmg.hourglass.domain.model.WearCountdownDto
import tmg.hourglass.domain.model.WearSyncContract
import tmg.hourglass.domain.model.toCountdown
import tmg.hourglass.domain.model.toWearDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WearosCountdownRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveCountdowns(countdowns: List<Countdown>) {
        val dtos = countdowns.map { it.toWearDto() }
        val json = Json.encodeToString(dtos)
        prefs.edit().putString(KEY_COUNTDOWNS, json).apply()
    }

    fun setSchemaSupported(supported: Boolean) {
        prefs.edit().putBoolean(KEY_SCHEMA_SUPPORTED, supported).apply()
    }

    fun isSchemaSupported(): Boolean {
        return prefs.getBoolean(KEY_SCHEMA_SUPPORTED, true)
    }

    suspend fun fetchCountdownsFromDataClient(): List<Countdown> {
        try {
            val dataItems = Wearable.getDataClient(context)
                .getDataItems(Uri.parse("wear://${WearSyncContract.COUNTDOWNS_PATH}"))
                .await()

            try {
                val matchingItem = dataItems.firstOrNull { it.uri.path == WearSyncContract.COUNTDOWNS_PATH }
                if (matchingItem != null) {
                    val dataMap = DataMapItem.fromDataItem(matchingItem).dataMap
                    val version = dataMap.getInt(WearSyncContract.KEY_SCHEMA_VERSION, 1)

                    if (!WearSyncContract.isSchemaVersionSupported(version)) {
                        Log.w(TAG, "Unsupported schema version: $version. Current supported version: ${WearSyncContract.CURRENT_SCHEMA_VERSION}")
                        setSchemaSupported(false)
                        return getCountdowns()
                    }

                    setSchemaSupported(true)
                    val jsonString = dataMap.getString(WearSyncContract.KEY_COUNTDOWNS_JSON)
                    if (jsonString != null) {
                        val dtos = Json.decodeFromString<List<WearCountdownDto>>(jsonString)
                        val countdowns = dtos.map { it.toCountdown() }
                        saveCountdowns(countdowns)
                        return countdowns
                    }
                }
            } finally {
                dataItems.release()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to fetch countdowns from DataClient", e)
        }
        return getCountdowns()
    }

    fun getCountdowns(): List<Countdown> {
        val json = prefs.getString(KEY_COUNTDOWNS, null) ?: return emptyList()
        return runCatching {
            val dtos = Json.decodeFromString<List<WearCountdownDto>>(json)
            dtos.map { it.toCountdown() }
        }.getOrElse { e ->
            Log.e(TAG, "Error deserializing stored countdowns", e)
            emptyList()
        }
    }

    fun getCountdown(id: String): Countdown? {
        return getCountdowns().firstOrNull { it.id == id }
    }

    fun saveComplicationBinding(complicationId: Int, countdownId: String) {
        prefs.edit().putString("$KEY_COMPLICATION_PREFIX$complicationId", countdownId).apply()
    }

    fun getComplicationBinding(complicationId: Int): String? {
        return prefs.getString("$KEY_COMPLICATION_PREFIX$complicationId", null)
    }

    fun removeComplicationBinding(complicationId: Int) {
        prefs.edit().remove("$KEY_COMPLICATION_PREFIX$complicationId").apply()
    }

    companion object {
        private const val TAG = "WearosRepo"
        private const val PREFS_NAME = "wearos_hourglass_prefs"
        private const val KEY_COUNTDOWNS = "synced_countdowns"
        private const val KEY_SCHEMA_SUPPORTED = "schema_supported"
        private const val KEY_COMPLICATION_PREFIX = "complication_id_"
    }
}
