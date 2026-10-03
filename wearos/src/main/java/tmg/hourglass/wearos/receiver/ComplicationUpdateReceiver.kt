package tmg.hourglass.wearos.receiver

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.wear.watchface.complications.datasource.ComplicationDataSourceUpdateRequester
import tmg.hourglass.wearos.service.HourglassComplicationService

class ComplicationUpdateReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        val requester = ComplicationDataSourceUpdateRequester.create(
            context,
            ComponentName(context, HourglassComplicationService::class.java)
        )
        requester.requestUpdateAll()
    }
}
