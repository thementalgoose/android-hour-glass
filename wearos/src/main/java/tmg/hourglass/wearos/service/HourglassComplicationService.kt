package tmg.hourglass.wearos.service

import androidx.wear.watchface.complications.data.ComplicationData
import androidx.wear.watchface.complications.data.ComplicationType
import androidx.wear.watchface.complications.data.LongTextComplicationData
import androidx.wear.watchface.complications.data.PlainComplicationText
import androidx.wear.watchface.complications.data.RangedValueComplicationData
import androidx.wear.watchface.complications.data.ShortTextComplicationData
import androidx.wear.watchface.complications.datasource.ComplicationRequest
import androidx.wear.watchface.complications.datasource.SuspendingComplicationDataSourceService
import tmg.hourglass.wearos.data.WearosCountdownRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import java.time.LocalDateTime

@AndroidEntryPoint
class HourglassComplicationService : SuspendingComplicationDataSourceService() {

    @Inject
    lateinit var repository: WearosCountdownRepository

    override suspend fun onComplicationRequest(request: ComplicationRequest): ComplicationData? {
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
        val title = countdown.name

        return when (request.complicationType) {
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
            else -> buildFallbackComplication(request.complicationType)
        }
    }

    override fun getPreviewData(type: ComplicationType): ComplicationData? {
        val sampleTitle = "Countdown"
        val sampleLabel = "10 days"
        val sampleProgress = 50f

        return when (type) {
            ComplicationType.RANGED_VALUE -> {
                RangedValueComplicationData.Builder(
                    value = sampleProgress,
                    min = 0f,
                    max = 100f,
                    contentDescription = PlainComplicationText.Builder(sampleTitle).build()
                )
                    .setText(PlainComplicationText.Builder(sampleLabel).build())
                    .setTitle(PlainComplicationText.Builder(sampleTitle).build())
                    .build()
            }
            ComplicationType.SHORT_TEXT -> {
                ShortTextComplicationData.Builder(
                    text = PlainComplicationText.Builder(sampleLabel).build(),
                    contentDescription = PlainComplicationText.Builder(sampleTitle).build()
                )
                    .setTitle(PlainComplicationText.Builder(sampleTitle).build())
                    .build()
            }
            ComplicationType.LONG_TEXT -> {
                LongTextComplicationData.Builder(
                    text = PlainComplicationText.Builder(sampleLabel).build(),
                    contentDescription = PlainComplicationText.Builder("$sampleTitle: $sampleLabel").build()
                )
                    .setTitle(PlainComplicationText.Builder(sampleTitle).build())
                    .build()
            }
            else -> null
        }
    }

    private fun buildFallbackComplication(type: ComplicationType): ComplicationData? {
        val label = "Select"
        val title = "Hourglass"
        return when (type) {
            ComplicationType.RANGED_VALUE -> {
                RangedValueComplicationData.Builder(
                    value = 0f,
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
                    contentDescription = PlainComplicationText.Builder(title).build()
                )
                    .setTitle(PlainComplicationText.Builder(title).build())
                    .build()
            }
            else -> null
        }
    }
}
