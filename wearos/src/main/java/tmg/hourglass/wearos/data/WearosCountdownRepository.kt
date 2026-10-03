package tmg.hourglass.wearos.data

import android.content.Context
import android.content.SharedPreferences
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import tmg.hourglass.domain.model.Countdown
import tmg.hourglass.domain.model.WearCountdownDto
import tmg.hourglass.domain.model.toCountdown
import tmg.hourglass.domain.model.toWearDto

class WearosCountdownRepository(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveCountdowns(countdowns: List<Countdown>) {
        val dtos = countdowns.map { it.toWearDto() }
        val json = Json.encodeToString(dtos)
        prefs.edit().putString(KEY_COUNTDOWNS, json).apply()
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
    }
}
