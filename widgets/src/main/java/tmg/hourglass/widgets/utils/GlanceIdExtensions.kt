package tmg.hourglass.widgets.utils

import androidx.glance.GlanceId

val GlanceId.appWidgetId: Int
    get() = this.toString().filter { it.isDigit() }.toInt()