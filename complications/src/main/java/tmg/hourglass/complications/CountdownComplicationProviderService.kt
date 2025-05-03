package tmg.hourglass.complications

import android.content.res.Configuration
import androidx.wear.watchface.complications.data.ComplicationData
import androidx.wear.watchface.complications.data.ComplicationType
import androidx.wear.watchface.complications.data.PlainComplicationText
import androidx.wear.watchface.complications.data.RangedValueComplicationData
import androidx.wear.watchface.complications.datasource.ComplicationRequest
import androidx.wear.watchface.complications.datasource.SuspendingComplicationDataSourceService
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CountdownComplicationProviderService: SuspendingComplicationDataSourceService() {

    override fun getPreviewData(type: ComplicationType): ComplicationData? {
        return RangedValueComplicationData
            .Builder(
                value = 1f,
                min = 0f,
                max = 2f,
                contentDescription = PlainComplicationText.Builder("A Wear").build()
            )
            .setTitle(PlainComplicationText.Builder("A Wear").build())
            .build()
    }

    override fun onComplicationActivated(complicationInstanceId: Int, type: ComplicationType) {
        super.onComplicationActivated(complicationInstanceId, type)
    }

    override fun onComplicationDeactivated(complicationInstanceId: Int) {
        super.onComplicationDeactivated(complicationInstanceId)
    }

    override suspend fun onComplicationRequest(request: ComplicationRequest): ComplicationData? {
        return RangedValueComplicationData
            .Builder(
                value = 1.7f,
                min = 0f,
                max = 2f,
                contentDescription = PlainComplicationText.Builder("A Wear").build()
            )
            .setTitle(PlainComplicationText.Builder("A Wear").build())
            .build()
    }
}