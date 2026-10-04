package tmg.hourglass.wearos.data

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import tmg.hourglass.domain.model.Countdown
import tmg.hourglass.domain.model.WearCountdownDto
import tmg.hourglass.domain.model.toCountdown
import tmg.hourglass.domain.model.toWearDto

import dagger.hilt.android.qualifiers.ApplicationContext
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

    suspend fun fetchCountdownsFromDataClient(): List<Countdown> {
        try {
            val dataItems = Wearable.getDataClient(context)
                .getDataItems(Uri.parse("wear://$COUNTDOWNS_PATH"))
                .await()
            for (item in dataItems) {
                if (item.uri.path == COUNTDOWNS_PATH) {
                    val dataMap = DataMapItem.fromDataItem(item).dataMap
                    val jsonString = dataMap.getString(KEY_COUNTDOWNS_JSON)
                    if (jsonString != null) {
                        val dtos = Json.decodeFromString<List<WearCountdownDto>>(jsonString)
                        val countdowns = dtos.map { it.toCountdown() }
                        saveCountdowns(countdowns)
                        dataItems.release()
                        return countdowns
                    }
                }
            }
            dataItems.release()
        } catch (e: Exception) {
            Log.e("WearosRepo", "Failed to fetch countdowns from DataClient", e)
        }
        return getCountdowns()
    }

    fun getCountdowns(): List<Countdown> {
        val json = prefs.getString(KEY_COUNTDOWNS, null) ?: return emptyList()
        return try {
            val dtos = Json.decodeFromString<List<WearCountdownDto>>(json)
            dtos.map { it.toCountdown() }
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun getCountdown(id: String): Countdown? {
        return getCountdowns().firstOrNull { it.id == id }
    }

    fun saveComplicationBinding(complicationId: Int, countdownId: String) {
        prefs.edit().putString(KEY_COMPLICATION_PREFIX + complicationId, countdownId).apply()
    }

    fun getComplicationBinding(complicationId: Int): String? {
        return prefs.getString(KEY_COMPLICATION_PREFIX + complicationId, null)
    }

    fun removeComplicationBinding(complicationId: Int) {
        prefs.edit().remove(KEY_COMPLICATION_PREFIX + complicationId).apply()
    }

    companion object {
        private const val PREFS_NAME = "wearos_hourglass_prefs"
        private const val KEY_COUNTDOWNS = "synced_countdowns"
        private const val KEY_COMPLICATION_PREFIX = "complication_id_"
        private const val COUNTDOWNS_PATH = "/countdowns"
        private const val KEY_COUNTDOWNS_JSON = "countdowns_json"
    }
}
