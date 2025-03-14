package tmg.hourglass.complications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class CountdownBroadcastReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i("Wear", "CountdownBroadcastReceiver onReceive")
    }
}