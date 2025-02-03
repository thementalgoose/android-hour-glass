package tmg.hourglass.domain.connectors

import tmg.hourglass.domain.model.WidgetReference

interface WidgetConnector {

    fun saveSync(widgetReference: WidgetReference)

    fun saveSync(appWidgetId: Int, countdownId: String) {
        val model = WidgetReference(
            appWidgetId = appWidgetId,
            countdownId = countdownId,
            openAppOnClick = false
        )
        saveSync(model)
    }

    fun getSync(appWidgetId: Int): WidgetReference?
}