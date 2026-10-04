package tmg.hourglass.wearos.service

import androidx.wear.watchface.complications.data.ComplicationData
import androidx.wear.watchface.complications.data.ComplicationType
import androidx.wear.watchface.complications.data.LongTextComplicationData
import androidx.wear.watchface.complications.data.PlainComplicationText
import androidx.wear.watchface.complications.data.RangedValueComplicationData
import androidx.wear.watchface.complications.data.ShortTextComplicationData
import androidx.wear.watchface.complications.datasource.ComplicationRequest
import androidx.wear.watchface.complications.datasource.SuspendingComplicationDataSourceService
import dagger.hilt.android.AndroidEntryPoint
import tmg.hourglass.wearos.data.WearosCountdownRepository
import java.time.LocalDateTime
import javax.inject.Inject

@AndroidEntryPoint
class HourglassComplicationService : SuspendingComplicationDataSourceService() {

    @Inject
    lateinit var repository: WearosCountdownRepository

    override suspend fun onComplicationRequest(request: ComplicationRequest): ComplicationData? {
        if (!repository.isSchemaSupported()) {
            return buildFallbackComplication(request.complicationType, title = "Hourglass", label = "Update")
        }

        val countdownId = repository.getComplicationBinding(request.complicationInstanceId)
        var countdown = countdownId?.let { repository.getCountdown(it) }

        if (countdown == null && countdownId != null) {
            repository.fetchCountdownsFromDataClient()
            countdown = repository.getCountdown(countdownId)
        }

        if (countdown == null) {
            return buildFallbackComplication(request.complicationType)
        }

        val now = LocalDateTime.now()
        val progress = countdown.getProgress(now)
        val label = countdown.getLabel(progress)
        val fullTitle = if (!countdown.emoji.isNullOrBlank()) {
            "${countdown.emoji} ${countdown.name}"
        } else {
            countdown.name
        }
        val title = formatComplicationTitle(fullTitle)

        return buildComplicationData(request.complicationType, title = title, label = label, progress = progress)
    }

    override fun getPreviewData(type: ComplicationType): ComplicationData? {
        return buildComplicationData(type, title = "Countdown", label = "10 days", progress = 0.5f)
    }

    private fun buildFallbackComplication(
        type: ComplicationType,
        title: String = "Hourglass",
        label: String = "Select"
    ): ComplicationData? {
        return buildComplicationData(type, title = title, label = label, progress = 0f)
    }

    private fun buildComplicationData(
        type: ComplicationType,
        title: String,
        label: String,
        progress: Float
    ): ComplicationData? {
        return when (type) {
            ComplicationType.RANGED_VALUE -> {
                RangedValueComplicationData.Builder(
                    value = (progress * 100f).coerceIn(0f, 100f),
                    min = 0f,
                    max = 100f,
                    contentDescription = PlainComplicationText.Builder(title).build()
                )
                    .setText(PlainComplicationText.Builder(label).build())
                    .setTitle(PlainComplicationText.Builder(title).build())
                    .build()
            }
            ComplicationType.SHORT_TEXT -> {
                ShortTextComplicationData.Builder(
                    text = PlainComplicationText.Builder(label).build(),
                    contentDescription = PlainComplicationText.Builder(title).build()
                )
                    .setTitle(PlainComplicationText.Builder(title).build())
                    .build()
            }
            ComplicationType.LONG_TEXT -> {
                LongTextComplicationData.Builder(
                    text = PlainComplicationText.Builder(label).build(),
                    contentDescription = PlainComplicationText.Builder("$title: $label").build()
                )
                    .setTitle(PlainComplicationText.Builder(title).build())
                    .build()
            }
            else -> null
        }
    }

    companion object {
        fun formatComplicationTitle(title: String, maxLength: Int = 7): String {
            if (title.length <= maxLength) return title
            val substring = title.take(maxLength)
            val lastSpaceIndex = substring.lastIndexOf(' ')
            return if (lastSpaceIndex > 0) {
                substring.substring(0, lastSpaceIndex).trimEnd()
            } else {
                substring.trimEnd()
            }
        }
    }
}
