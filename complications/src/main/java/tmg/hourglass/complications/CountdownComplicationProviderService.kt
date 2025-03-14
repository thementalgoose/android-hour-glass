package tmg.hourglass.complications

import android.util.Log
import androidx.wear.watchface.complications.data.ComplicationData
import androidx.wear.watchface.complications.data.ComplicationType
import androidx.wear.watchface.complications.data.PlainComplicationText
import androidx.wear.watchface.complications.data.ShortTextComplicationData
import androidx.wear.watchface.complications.datasource.ComplicationDataSourceService
import androidx.wear.watchface.complications.datasource.ComplicationRequest

class CountdownComplicationProviderService: ComplicationDataSourceService() {
    override fun getPreviewData(type: ComplicationType): ComplicationData? {
        Log.i("Wear", "CountdownComplicationProviderService onComplicationRequest $type")
        return ShortTextComplicationData
            .Builder(
                text = PlainComplicationText.Builder("A Wear").build(),
                contentDescription = PlainComplicationText.Builder("A Wear").build()
            )
            .build()
    }

    override fun onComplicationRequest(
        request: ComplicationRequest,
        listener: ComplicationRequestListener
    ) {
        Log.i("Wear", "CountdownComplicationProviderService onComplicationRequest $request")
        listener.onComplicationData(
            ShortTextComplicationData
                .Builder(
                    text = PlainComplicationText.Builder("A Wear").build(),
                    contentDescription = PlainComplicationText.Builder("A Wear").build()
                )
                .build()
        )
    }

    override fun onComplicationActivated(complicationInstanceId: Int, type: ComplicationType) {
        super.onComplicationActivated(complicationInstanceId, type)
    }

    override fun onComplicationDeactivated(complicationInstanceId: Int) {
        super.onComplicationDeactivated(complicationInstanceId)
    }
}