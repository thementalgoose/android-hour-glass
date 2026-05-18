package tmg.hourglass.wearos

import androidx.wear.watchface.complications.data.ComplicationData
import androidx.wear.watchface.complications.data.ComplicationType
import androidx.wear.watchface.complications.data.PlainComplicationText
import androidx.wear.watchface.complications.data.ShortTextComplicationData
import androidx.wear.watchface.complications.datasource.ComplicationRequest
import androidx.wear.watchface.complications.datasource.SuspendingComplicationDataSourceService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withTimeoutOrNull
import tmg.hourglass.domain.model.Countdown
import tmg.hourglass.domain.repositories.CountdownRepository
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@AndroidEntryPoint
class CountdownComplicationDataSourceService : SuspendingComplicationDataSourceService() {

    @Inject
    lateinit var countdownRepository: CountdownRepository

    override suspend fun onComplicationRequest(request: ComplicationRequest): ComplicationData {
        // Retrieve the latest info for inclusion in the data. Use a small timeout
        // so that complication updates never hang the provider.
        val text = try {
            withTimeoutOrNull(3_000) {
                getLatestData()
            } ?: "No events"
        } catch (t: Throwable) {
            t.printStackTrace()
            "No events"
        }

        return shortTextComplicationData(text)
    }

    override fun getPreviewData(type: ComplicationType): ComplicationData? {
        return shortTextComplicationData("Event — 3d")
    }

    private suspend fun getLatestData(): String {
        // Read a snapshot from the repository flow
        val list = countdownRepository.allCurrent().firstOrNull() ?: emptyList()

        // Pick the nearest upcoming countdown
        val upcoming: Countdown = list
            .filter { !it.isFinished }
            .minByOrNull { it.endDate }
            ?: return "No events"

        return formatCountdownShort(upcoming)
    }

    private fun formatCountdownShort(countdown: Countdown): String {
        val now = LocalDateTime.now()
        val daysLeft = ChronoUnit.DAYS.between(now.toLocalDate().atStartOfDay(), countdown.endDate.toLocalDate().atStartOfDay()).toInt()

        val suffix = when {
            daysLeft <= 0 -> "Today"
            daysLeft == 1 -> "1d"
            else -> "${'$'}{daysLeft}d"
        }

        // Keep short: "Name — 3d". Trim or fallback to name only if too long.
        val name = countdown.name.ifBlank { "Countdown" }
        val combined = "$name — $suffix"
        return if (combined.length > 24) {
            // Try shorter form
            "$name: $suffix"
        } else combined
    }

    private fun shortTextComplicationData(text: String) = ShortTextComplicationData
        .Builder(
            text = PlainComplicationText.Builder(text).build(),
            contentDescription = PlainComplicationText.Builder(text).build()
        )
            // Add further optional details here such as icon, tap action, and title.
        .build()
}
