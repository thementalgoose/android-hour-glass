package tmg.hourglass.domain.connectors

import tmg.hourglass.domain.model.Countdown
import tmg.hourglass.domain.model.WidgetReference

interface WidgetConnector {
    fun saveSync(widgetReference: WidgetReference)
    fun saveSync(appWidgetId: Int, countdownId: String) {
        val model = WidgetReference(
            appWidgetId = appWidgetId,
            countdownId = countdownId
        )
        saveSync(model)
    }
    fun getSync(appWidgetId: Int): WidgetReference?

    // TODO: Remove this
    fun getCountdownModelSync(appWidgetId: Int): Countdown?
}