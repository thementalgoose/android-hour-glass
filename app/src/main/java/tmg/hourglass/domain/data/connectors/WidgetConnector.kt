package tmg.hourglass.domain.data.connectors

import tmg.hourglass.domain.data.models.Countdown
import tmg.hourglass.domain.data.models.WidgetReference

interface WidgetConnector {
    fun saveSync(appWidgetId: Int, countdownId: String)
    fun getWidgetReferenceSync(appWidgetId: Int): WidgetReference?
    fun getCountdownModelSync(appWidgetId: Int): Countdown?
}