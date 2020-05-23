package tmg.hourglass.data.connectors

import tmg.hourglass.data.models.Countdown
import tmg.hourglass.data.models.WidgetReference

interface WidgetConnector {
    fun saveSync(appWidgetId: Int, countdownId: String)
    fun getWidgetReferenceSync(appWidgetId: Int): WidgetReference?
    fun getCountdownModelSync(appWidgetId: Int): Countdown?
}