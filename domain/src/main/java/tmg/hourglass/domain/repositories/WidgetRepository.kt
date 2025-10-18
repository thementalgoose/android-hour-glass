package tmg.hourglass.domain.repositories

import tmg.hourglass.domain.model.WidgetReference

interface WidgetRepository {

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